package org.itri.ccma.tarsan.biz;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.convert.Converter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.exceptions.LogicException;
import org.itri.ccma.tarsan.common.SessionMap;
import org.itri.ccma.tarsan.hibernate.Budgetlog;
import org.itri.ccma.tarsan.hibernate.Budgetpool;
import org.itri.ccma.tarsan.hibernate.Buyad;
import org.itri.ccma.tarsan.hibernate.ConnectLog;
import org.itri.ccma.tarsan.hibernate.Control;
import org.itri.ccma.tarsan.hibernate.Logad;
import org.itri.ccma.tarsan.hibernate.Postad;
import org.itri.ccma.tarsan.hibernate.PostadId;
import org.itri.ccma.tarsan.hibernate.Price;
import org.itri.ccma.tarsan.hibernate.Runad;
import org.itri.ccma.tarsan.hibernate.RunadId;
import org.itri.ccma.tarsan.hibernate.SplashSchedule;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.hibernate.Vacantad;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.HttpUtil;
import org.itri.ccma.tarsan.util.MessageUtil;
import org.itri.ccma.tarsan.util.SignatureUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BoAdPublish {
	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoAdPublish() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoAdPublish getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoAdPublish INSTANCE = new BoAdPublish();
	}

	/****************************************************************************************/
	/*
	 * Utility function
	 */
	/****************************************************************************************/

	public List<?> logad(String sessionId,long postadId, long buyadId, String searchKeyword, String domain, String host, String ref, String ip, String url, boolean show, boolean click, String clickCode){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		Transaction tx = session.beginTransaction();

		try{
			Criteria criteria = session.createCriteria(Postad.class);
			Logad logad = new Logad();
			PostadId ids = new PostadId();
			ids.setBuyadId(buyadId);
			ids.setPostadId(postadId);
			criteria.add(Restrictions.eq("id", ids));				
			Postad postad = (Postad)criteria.uniqueResult();
				
			logad.setClick(click);
			logad.setCreatedate(currentDate);
			logad.setSearchKeyword(searchKeyword);
			logad.setDomain(domain);
			logad.setIp(ip);
			logad.setShow(show);
			logad.setUrlhost(host);
			logad.setUrlpath(url);
			logad.setUrlreferer(ref);
			logad.setPostad(postad);
			
			if(click){
				Criteria criteriaCount = session.createCriteria(Logad.class);
				criteriaCount.add(Restrictions.eq("hashkey", clickCode));
				criteriaCount.setProjection(Projections.rowCount());
				Long count=(Long) criteriaCount.uniqueResult();
				logger.info("COUNT: "+count);
				
				Criteria criterialog = session.createCriteria(Logad.class);
				criterialog.add(Restrictions.eq("hashkey", clickCode));
				Logad lastad = (Logad)criterialog.uniqueResult();		

				String md5code=SignatureUtil.createHash("md5", "TarsanAd"+lastad.getCreatedate().getTime());
				logger.info(lastad.getCreatedate().getTime()+" : "+md5code);
				if(count==1){
					logger.info("OK CLICK!");
					logad.setHashkey(clickCode);
					session.persist(logad);		
					tx.commit();
					
					countPostAd(postadId, buyadId,"click");
					return resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
							sessionId, "AdLog has been logged", "ClickCode", "0");					
				}					
			}
						
			String Code = SignatureUtil.createHash("md5", "TarsanAd"+currentDate.getTime());
			logad.setHashkey(Code);
			session.persist(logad);		
			
			countPostAd(postadId, buyadId,"show");
			
			tx.commit();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "AdLog has been logged", "ClickCode", Code);
			
		}catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
				
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}
	
	
	/****************************************************************************************/
	/*
	 * Logical function
	 */
	/****************************************************************************************/
	
	public List<?> getRunAd(String sessionId,String zone, String keyword){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date currentTime = new Date();
			String time = Integer.toString(currentTime.getHours());
			logger.info("TIME NOW :"+new java.sql.Timestamp(currentTime.getTime()));
			logger.info("zone :"+zone);
			logger.info("keyword :"+keyword);
			Criteria criteria = session.createCriteria(Runad.class);
			criteria.add(Restrictions.like("location", "%"+zone+"%"));
			criteria.add(Restrictions.eq("close", false));
			criteria.add(Restrictions.like("keywords", "%"+keyword+"\"%"));
			criteria.add(Restrictions.like("intervaltime", "%"+time+"%"));
			criteria.add(Restrictions.le("startdate", new java.sql.Timestamp(currentTime.getTime())));
			criteria.add(Restrictions.ge("enddate", new java.sql.Timestamp(currentTime.getTime())));
			List<Runad> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			for (Runad p : list) {
				String s = mapper.writeValueAsString(p.getContent());
				String s2 = mapper.writeValueAsString(p.getType());
				String PostadId = mapper.writeValueAsString(p.getId().getPostadId());
				String BuyadId = mapper.writeValueAsString(p.getId().getBuyadId());
				//logger.info("RAW:"+s);
				output.add(s+"@"+s2+"@"+PostadId+"@"+BuyadId);
			}
			//logger.info(output.toString());			
			
			if (output.isEmpty()){
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, "RunAd list generated", "size", list.size(), "content", output.toString());
				//resultList.add(output.toString());

			}else {
				// Random
				long seed = System.nanoTime();
				Collections.shuffle(output, new Random(seed));				
				String Url = output.get(0).toString().replaceAll("\"", "");
				String[] tmp = Url.split("@");
				String[] extractLink= tmp[0].split(",");

				if(extractLink.length>1){
					resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, "RunAd list generated", "size", list.size(), "Content",extractLink[0],"Type",tmp[1],"Link",extractLink[1],"pId",tmp[2],"bId",tmp[3]);
					logger.info(list.size()+" - "+tmp[0]);
					logger.info("Content - "+extractLink[0]);
					logger.info("Type - "+tmp[1]);
					logger.info("Link - "+extractLink[1]);
					logger.info("pId - "+tmp[2]);
					logger.info("bId - "+tmp[3]);					
				}else{
					resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
							sessionId, "RunAd list generated", "size", list.size(), "Content", tmp[0],"Type",tmp[1],"pId",tmp[2],"bId",tmp[3]);
					logger.info(list.size()+" - "+tmp[0]);
					logger.info("Type - "+tmp[1]);
					logger.info("pId - "+tmp[2]);
					logger.info("bId - "+tmp[3]);
				}				
			}		
			
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}

		return resultList;
	} 
	
	public List<?> getRunAdKeywords(String sessionId,String zone){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Date currentTime = new Date();
			String time = Integer.toString(currentTime.getHours());
			logger.info("TIME NOW :"+time);
			
			Criteria criteria = session.createCriteria(Runad.class);
			criteria.add(Restrictions.like("location", "%"+zone+"%"));
			criteria.add(Restrictions.eq("close", false));
			criteria.add(Restrictions.like("intervaltime", "%"+time+"%"));
			List<Runad> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			for (Runad p : list) {
				String s = mapper.writeValueAsString(p.getKeywords());
				output.add(s);
			}
			
			// Random
			long seed = System.nanoTime();
			logger.info("RAND:"+seed);
			Collections.shuffle(output, new Random(seed));
			
//			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
//					sessionId, "RunAd list generated", "size", list.size(), "list", output.toString());
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "RunAd list generated", "size", list.size(), "Keywords", output.get(0).toString());
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}

	public List<?> saveHTML(String sessionId,String text){
		ArrayList resultList = new ArrayList();
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html><body>");
		htmlBuilder.append(text);
		htmlBuilder.append("</body></html>\n");
		logger.info(System.getProperty("user.home"));
		FileWriter writer;
		try {
			writer = new FileWriter( "tmpHTML/hello.html");
			writer.write(htmlBuilder.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	    return resultList;
	}
	
	public void countPostAd(long postadId, long buyadId, String ShowOrClick){
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Date currentTime = new Date();
		Criteria criteria = session.createCriteria(Postad.class);
		PostadId ids = new PostadId();
		ids.setBuyadId(buyadId);
		ids.setPostadId(postadId);
		criteria.add(Restrictions.eq("id", ids));
		Postad lastad = (Postad)criteria.uniqueResult();
		
		if(ShowOrClick.equals("show")){
			int show = Integer.parseInt(lastad.getShowtimes()); 
			show++;
			logger.info("show : "+show);
			lastad.setShowtimes(String.valueOf(show));
		}else{
			int click = Integer.parseInt(lastad.getClicktimes());
			click++;
			logger.info("click : "+click);
			lastad.setClicktimes(String.valueOf(click));			
		}
		
		lastad.setUpdatetime(currentTime);
		session.persist(lastad);		
		tx.commit();
		logger.info("Count postAd:"+postadId+","+buyadId+" : "+ShowOrClick);
		Charge(buyadId,ShowOrClick);		
	}
	
	public List<?> listRunAd(String sessionId){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Runad.class);
			criteria.add(Restrictions.eq("close", false));
			List<Runad> list = criteria.list();
			List<String> output = new ArrayList();
			
			ObjectMapper mapper = new ObjectMapper();
			for (Runad p : list) {
				
				String s = mapper.writeValueAsString(p);
//				logger.info(s);
				output.add(s);
				// if (Configurations.IS_DEBUG)
				// logger.debug(s);
				// output.add(PojoMapper.toJson(p, false));
			}

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "RunAd list generated", "size", list.size(), "AD", output.toString());
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}

		return resultList;
	}
	
	public void Charge(long buyadId, String ShowOrClick){
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Buyad.class);
		criteria.add(Restrictions.eq("buyadId", buyadId));
		Buyad buyAd = (Buyad)criteria.uniqueResult();
		Vacantad findPrice = buyAd.getVacantad();
		//logger.info("BID:"+buyadId);
		logger.info("vacantID:"+findPrice.getVacantId());
		try{
			Session session2 = HibernateUtil.getSessionFactory().openSession();
			Transaction tx2 = session2.beginTransaction();
			Criteria criteria2 = session2.createCriteria(Price.class);
			criteria2.add(Restrictions.eq("vacantad.id", findPrice.getVacantId()));
			Price price = (Price)criteria2.uniqueResult();
			
			logger.info("priceID:"+price.getPriceId());
			if(price.getPriceTotal().equals("0")){
				logger.info(price.getPriceUnit()+" : $"+price.getPriceNum());
				BudgetCheckWithCount(buyadId,price.getPriceUnit(),price.getPriceNum(), ShowOrClick);
			}else{
				logger.info("TOTAL:"+price.getPriceTotal());				
			}
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}		
	}
	
	public void BudgetCheckWithCount(long buyadId, String unit, String num, String ShowOrClick){
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		Transaction tx = session.beginTransaction();
		Criteria criteria = session.createCriteria(Budgetpool.class);
		criteria.add(Restrictions.eq("buyad.id", buyadId));
		Budgetpool budget = (Budgetpool)criteria.uniqueResult();
		long budgetId = budget.getBudgetId();
		long currentBudget = budget.getBudgetCount();
		long TotalBudget = budget.getBudgetTotal();
		long thisCount = Long.parseLong(num);
		if(ShowOrClick.equals(unit)){
			if(TotalBudget>=(currentBudget+thisCount)){
				budget.setBudgetCount(currentBudget+thisCount);
				budget.setUpdatetime(currentDate);
				BudgetLog(budget,TotalBudget,(currentBudget+thisCount));
				session.persist(budget);		
				tx.commit();
			}else{
				CloseAd(buyadId);
			}
		}
		
	}
	
	public void BudgetLog(Budgetpool budget, long total, long counting){
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		Transaction tx = session.beginTransaction();
		Criteria criteria = session.createCriteria(Budgetlog.class);
		Budgetlog budgetLog = new Budgetlog();
		 
		try {
			budgetLog.setBudgetpool(budget);
			budgetLog.setBudgetTotal(total);
			budgetLog.setCounting(String.valueOf(counting));
			budgetLog.setCreatedate(currentDate);
			session.persist(budgetLog);		
			tx.commit();
			logger.info("BudgetLog");
			logger.info(budget.getBudgetId());
			logger.info(total);
			logger.info(counting);
			logger.info(currentDate);
			
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}			
		} finally {
			session.close();
		}
	}
	
	public void CloseAd(long buyadId){
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();		
		Date currentDate = new Date();	
		Session session = HibernateUtil.getSessionFactory().openSession();
		Session session2 = HibernateUtil.getSessionFactory().openSession();
		try {
			
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Runad.class);
			criteria.add(Restrictions.eq("id.buyadId", buyadId));
			Runad runAd = (Runad)criteria.uniqueResult();
	
			Transaction tx2 = session2.beginTransaction();
			Criteria criteria2 = session2.createCriteria(Postad.class);
			criteria2.add(Restrictions.eq("buyad.id", buyadId));
			Postad postAd = (Postad)criteria2.uniqueResult();
			
			runAd.setClose(true);
			runAd.setUpdatetime(currentDate);
			tx.commit();
			logger.info("#CloseAd#");
			
			postAd.setClose(true);			
			postAd.setUpdatetime(currentDate);
			tx2.commit();
			logger.info("buyadId : "+buyadId);
			
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}			
		} finally {
			session.close();
			session2.close();
		}
	}

	public List getUserNameByMac(String sessionId,String macAddr){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();	
		try {
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Control.class);
			String matchStr= macAddr.replace(":", "");
			criteria.add(Restrictions.eq("macAddr", matchStr));
			Control con = (Control) criteria.uniqueResult();
			
			if (con == null){
//				throw new LogicException("MacAddres does not exist", Configurations.CODE_NOT_EXIST, "MacAddres", macAddr);
				Control newcon = new Control();
				newcon.setMacAddr(matchStr);
				newcon.setUserId("tmp");
				newcon.setPageUrl("http://tarsanad.ddns.net/splash/TempPage.html,false");
				newcon.setSessionTime("300");
				newcon.setLastActiveTime(currentDate);
				session.persist(newcon);
				tx.commit();
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, "tmp", "Info", "This Not regular user.");
				
			}else{				
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, con.getUserId());
			}

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}

		return resultList;		
	}
	
	public List getPageByName(String sessionId,String username){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Control.class);
			criteria.add(Restrictions.eq("userId", username));
			Control con = (Control) criteria.uniqueResult();
			
			if (con == null){
				throw new LogicException("The Name does not exist", Configurations.CODE_NOT_EXIST, "Name", username);
			}else{
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, con.getPageUrl());
			}			
			tx.commit();

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}

		return resultList;		
	}
	
	public List getAllMachine(String sessionId){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Control.class);
			criteria.addOrder(Order.asc("userId"));
			List<Control> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			for (Control p : list) {
				String user = mapper.writeValueAsString(p.getUserId());
				String url = mapper.writeValueAsString(p.getPageUrl());
				String mac = mapper.writeValueAsString(p.getMacAddr());
				String time = mapper.writeValueAsString(p.getSessionTime());
				url = url.replace(",", "@");
				output.add(user+"@"+url+"@"+mac+"@"+time);
			}
			String result = output.toString().replaceAll("\"", "");
			logger.info(result);
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					"AllMachine",result);
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}
		return resultList;	
	}
	
	public List getMachineByUser(String sessionId, String owner){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Control.class);
			if(!owner.equals("itri"))criteria.add(Restrictions.eq("owner", owner));
			criteria.addOrder(Order.asc("userId"));
			List<Control> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			for (Control p : list) {
				String user = mapper.writeValueAsString(p.getUserId());
				String url = mapper.writeValueAsString(p.getPageUrl());
				String mac = mapper.writeValueAsString(p.getMacAddr());
				String time = mapper.writeValueAsString(p.getSessionTime());
				String lastTime =  mapper.writeValueAsString(p.getLastActiveTime().toString());
				url = url.replace(",", "@");
				output.add(user+"@"+url+"@"+mac+"@"+time+"@"+lastTime);
			}
			String result = output.toString().replaceAll("\"", "");
			logger.info(result);
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					"MachineByUser",result);
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}
		return resultList;	
	}
	
	public List getLastActive(String sessionId,String username){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Control.class);
			criteria.add(Restrictions.eq("userId", username));
			Control user = (Control) criteria.uniqueResult();
			String result = user.getLastActiveTime().toString();
			logger.info(result);
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId,result);
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}
	
	public List<?> Machinelogin(String sessionId, String account) {
		// public JSONObject login(String sessionId, String encryptedText) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		account = HttpUtil.getInstance().HTML_Decoding(account);

		try {
			Date currentDate = new Date();

			Criteria criteria_account = session.createCriteria(Control.class);
			criteria_account.add(Restrictions.eq("userId", account));
//			criteria_account.add(Restrictions.eq("deletedFlag", false));
			Control user = (Control) criteria_account.uniqueResult();

			// password = SignatureUtil.createHash("md5", password);
			if (user == null)
				throw new LogicException("This Machine does not exist", Configurations.CODE_NOT_EXIST, "machine Name", account);

			user.setLastActiveTime(currentDate);
			session.persist(user);
			//session.update(user);
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "Machine " + user.getUserId() + " successfully logged in");

			SessionMap.addSession(sessionId, account);

			tx.commit();
		} catch (LogicException e) {
			if (Configurations.IS_DEBUG) {
				logger.debug("[FAILURE] methodName: " + methodName);
				logger.debug("[FAILURE] message: " + MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(),
						methodName, null, e.getMessage(), e.getParams()));
			}
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(), methodName, null,
					e.getMessage(), e.getParams());
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}
	
	public List setPageUrl(String sessionId, String username, String url ){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String [] users = username.split(",");
			for(String userid:users){
				Transaction tx = session.beginTransaction();
				Criteria criteria = session.createCriteria(Control.class);
				criteria.add(Restrictions.eq("userId", userid));
				Control con = (Control) criteria.uniqueResult();				
				if (con == null){
					throw new LogicException(userid+" does not exist", Configurations.CODE_NOT_EXIST, "Name", username);
				}else{					
					con.setPageUrl(url+",false");
					session.update(con);					
				}
				tx.commit();
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, username+"'s page already update.","URL",url);
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					sessionId, e.getMessage());
		} finally {
			session.close();
		}

		return resultList;		
	}
	
	public List getSessionTimebyMac(String sessionId,String macAddr){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Control.class);
			String matchStr= macAddr.replace(":", "");
			criteria.add(Restrictions.eq("macAddr", matchStr));
			Control con = (Control) criteria.uniqueResult();
			
			if (con == null){
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_FORBIDDEN, methodName,
						sessionId, "3600", "Info", "This Not regular user.");				
			}else{				
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, con.getSessionTime());
			}

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}

		return resultList;	
	}
	
	public List setSessionTime(String sessionId, String username, String time){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Control.class);
			criteria.add(Restrictions.eq("userId", username));
			Control con = (Control) criteria.uniqueResult();
			
			if (con == null){
				throw new LogicException("The Name does not exist", Configurations.CODE_NOT_EXIST, "Name", username);
			}else{
				
				if(time.matches("[-+]?\\d*\\.?\\d+")){
					con.setSessionTime(time);
					session.update(con);
					resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
							sessionId, username+"'s SessionTime already update.","SessionTime",time);
				}  else{
					resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
							sessionId, "String is not number.");
				}
			}
			tx.commit();

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					sessionId, e.getMessage());
		} finally {
			session.close();
		}

		return resultList;
	}
	
	public List setSplashSchedule(String sessionId, String username, String url, String date){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		try {						
			SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
			Date scheduleDate = originalFormat.parse(date.toString());
			String createInfo="";
			String updateInfo="";
			String [] users = username.split(",");
			for(String userid:users){
				Transaction tx = session.beginTransaction();
				Criteria criteria = session.createCriteria(SplashSchedule.class);
				criteria.add(Restrictions.eq("del", false));
				criteria.add(Restrictions.eq("scheduleTime", scheduleDate));
				criteria.add(Restrictions.eq("userId", userid));
				if(criteria.uniqueResult()!=null){					
					SplashSchedule schedule = (SplashSchedule)criteria.uniqueResult();
					schedule.setScheduleTime(scheduleDate);
					schedule.setPageUrl(url);
					schedule.setUpdateTime(currentDate);
					session.update(schedule);
					tx.commit();					
//					resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,sessionId, username+"'s Schedule already update.","Schedule Time",date, "UpdateTime",  currentDate);
					if(updateInfo.equals("")){
						updateInfo = userid;
					}else{
						updateInfo = updateInfo + "," + userid;
					}
				}else{
					SplashSchedule schedule = new SplashSchedule();
					schedule.setScheduleTime(scheduleDate);
					schedule.setPageUrl(url);
					schedule.setDel(false);
					schedule.setUserId(userid);
					schedule.setCreateTime(currentDate);
					schedule.setUpdateTime(currentDate);
					session.save(schedule);
					tx.commit();
//					resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,sessionId, username+"'s Schedule already create.","Schedule Time",date, "CreateTime",  currentDate);
					if(createInfo.equals("")){
						createInfo = userid;
					}else{
						createInfo = createInfo + "," + userid;
					}
				}
			}		
			if(createInfo.equals("")){
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, updateInfo+"'s Schedule already update.","Schedule Time",date, "UpdateTime",  currentDate);
			}else if(updateInfo.equals("")){
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, createInfo+"'s Schedule already create.","Schedule Time",date, "CreateTime",  currentDate);
			}else{
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, createInfo+"'s Schedule already create.","Schedule Time",date, "CreateTime",  currentDate,updateInfo+"'s Schedule already update.","Schedule Time",date, "UpdateTime",  currentDate);
			}			
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					sessionId, e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}
	
	public List getSplashSchedule(String sessionId){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
		logger.info(originalFormat.format(cal.getTime()));
		String dateStr =originalFormat.format(cal.getTime());
		
		try {			
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(SplashSchedule.class);
			Date date = originalFormat.parse(dateStr.toString());
			criteria.add(Restrictions.eq("del", false));
			criteria.add(Restrictions.eq("scheduleTime", date));
			List<SplashSchedule> list = criteria.list();
			List<String> output = new ArrayList();			
			ObjectMapper mapper = new ObjectMapper();
			String userStr = "";
			String urlStr = "";
			for (SplashSchedule p : list) {				
				String user = mapper.writeValueAsString(p.getUserId());
				if(userStr.equals("")){
					userStr = user;
				}else{
					userStr = userStr + "@" + user;
				}
				String url = mapper.writeValueAsString(p.getPageUrl());
				if(urlStr.equals("")){
					urlStr = url;
				}else{
					urlStr = urlStr + "@" + url;
				}
			}
			userStr = userStr.replaceAll("\"", "");
			urlStr = urlStr.replaceAll("\"", "");
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId,"ok","users",userStr,"urls",urlStr);
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}

		return resultList;	
	}
	
	public List delSplashSchedule(String sessionId, String username,  String date){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		try {
			Transaction tx = session.beginTransaction();			
			SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
			Date scheduleDate = originalFormat.parse(date.toString());			
			Criteria criteria = session.createCriteria(SplashSchedule.class);
			
			criteria.add(Restrictions.eq("scheduleTime", scheduleDate));
			criteria.add(Restrictions.eq("userId", username));
			if(criteria.uniqueResult()!=null){
				SplashSchedule schedule = (SplashSchedule)criteria.uniqueResult();
				schedule.setDel(true);
				schedule.setUpdateTime(currentDate);
				session.update(schedule);
				tx.commit();				
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, username+"'s Schedule already delete.","Delete Time",date);
			}else{				
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, username+"or this schedule time is not found.");
			}			
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					sessionId, e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}
	
	public List getScheduleByUsername(String sessionId, String username){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		try {
			Transaction tx = session.beginTransaction();			
			Criteria criteria = session.createCriteria(SplashSchedule.class);
			criteria.add(Restrictions.eq("userId", username));
			criteria.addOrder(Order.asc("scheduleTime"));
			criteria.add(Restrictions.eq("del", false));
			List<SplashSchedule> list = criteria.list();
			ObjectMapper mapper = new ObjectMapper();

			String schedules = "";
			String urls = "";

			for (SplashSchedule p : list) {
				String schedule = mapper.writeValueAsString(p.getScheduleTime());
				String url = mapper.writeValueAsString(p.getPageUrl());						
				if(schedules.equals("")){
					schedules = schedule;
				}else{
					schedules = schedules + "@" + schedule ;
				}
				
				if(urls.equals("")){
					urls = url;
				}else{
					urls = urls + "@" + url ;
				}
			}
			urls = urls.replaceAll("\"", "");
			if(!criteria.list().isEmpty()){
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, "", "schedule", schedules, "url", urls, "currentTime",currentDate);	
			}else{
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
						sessionId, "Not found schedules.");
			}
			
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					sessionId, e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}
	
	public List getConnectLogByUsername(String sessionId, String username){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		try {
			Transaction tx = session.beginTransaction();			
			Criteria criteria = session.createCriteria(ConnectLog.class);
			criteria.add(Restrictions.eq("machine", username));
			criteria.addOrder(Order.desc("createdate"));
			List<ConnectLog> list = criteria.list();
			ObjectMapper mapper = new ObjectMapper();
			String connectlog = "";
			for (ConnectLog p : list) {
				String ip = mapper.writeValueAsString(p.getIp());
				String time = mapper.writeValueAsString(p.getCreatedate().toString());
				String type = mapper.writeValueAsString(p.getDeviceType());
				if(connectlog.equals("")){
					connectlog = ip+"@"+time+"@"+type;
				}else{
					connectlog = connectlog + "," + ip+"@"+time+"@"+type;
				}				
			}
			connectlog = connectlog.replaceAll("\"", "");
			if(!criteria.list().isEmpty()){
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, connectlog );	
			}else{
				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
						sessionId, "Not found log.");
			}			
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					sessionId, e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}
	
	public List connectLog(String sessionId, String ip, String type, String os, String browser, String brand, String mac, String ssid, String machine, String url){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Date currentDate = new Date();
		try {
			Transaction tx = session.beginTransaction();		
			
			Criteria criteria = session.createCriteria(ConnectLog.class);

			ConnectLog log = new ConnectLog();
			log.setIp(ip);
			log.setDeviceType(type);
			log.setOs(os);
			log.setBrand(brand);
			log.setBrowser(browser);
			log.setMac(mac);
			log.setMachine(machine);
			log.setSsid(ssid);
			log.setUrl(url);
			log.setCreatedate(currentDate);
			session.save(log);
			tx.commit();
			
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "ConnectLog already create.", "CreateTime",  currentDate);
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					sessionId, e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}	
}
