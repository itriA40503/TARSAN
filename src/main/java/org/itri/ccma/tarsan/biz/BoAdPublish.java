package org.itri.ccma.tarsan.biz;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.itri.ccma.tarsan.hibernate.Logad;
import org.itri.ccma.tarsan.hibernate.Postad;
import org.itri.ccma.tarsan.hibernate.PostadId;
import org.itri.ccma.tarsan.hibernate.Runad;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
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
}
