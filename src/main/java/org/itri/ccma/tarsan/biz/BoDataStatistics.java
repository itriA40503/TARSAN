package org.itri.ccma.tarsan.biz;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.MessageUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BoDataStatistics {
	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoDataStatistics() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoDataStatistics getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoDataStatistics INSTANCE = new BoDataStatistics();
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getDataByDays(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap dateList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				//String urlhost = mapper.writeValueAsString(p.getUrlHost());
				
//				output.add(month);
//				output.add(day);
//				output.add(urlhost);
				if(Integer.parseInt(day)<10){
					if(!dateList.containsKey(month+"-0"+day)){
						dateList.put(month+"-0"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-0"+day);
						dateList.put(month+"-0"+day,value+1);
					}					
				}else{
					if(!dateList.containsKey(month+"-"+day)){
						dateList.put(month+"-"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-"+day);
						dateList.put(month+"-"+day,value+1);
					}
				}
			}
			session.clear();
			list.remove(0);
			System.gc();
			SortedSet<String> keys = new TreeSet<String>(dateList.keySet());
			for (String key : keys) { 
//			   String value = dateList.get(key).toString();
			   String[] tmp = key.toString().split("-");
				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
						 dateList.get(key).toString()));
				 //logger.info(key.toString()+":"+value);

			}
			dateList = null;
			keys=null;
			System.gc();
//			for(Object key : dateList.keySet()){
//				 System.out.println(key + " : " + dateList.get(key));
//				 String[] tmp = key.toString().split("-");
//				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
//						 dateList.get(key).toString()));
//			}
			executionMap.put("days", output);
			output.remove(0);
			System.gc();
			resultList.add(executionMap);
			executionMap = null;
			System.gc();
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getAllHost(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			//HashMap hostList = new HashMap();
			TreeMap hostList = new TreeMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				String urlhost = mapper.writeValueAsString(p.getUrlHost());

				if(!hostList.containsKey(urlhost)){
					hostList.put(urlhost,1);
				}else{
					int value = (Integer)hostList.get(urlhost);
					hostList.put(urlhost,value+1);
				}
			}
			
			Map sortedMap = sortByValues(hostList);
			 
		    // Get a set of the entries on the sorted map
		    Set set = sortedMap.entrySet();
		 
		    // Get an iterator
		    Iterator i = set.iterator();
		 
		    // Display elements
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      //logger.info(me.getKey() + ": "+me.getValue());
		      output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Host",me.getKey(),"Count",
		    		  me.getValue().toString()));
		    }
			executionMap.put("hostData", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getHostByday(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap hostList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				String urlhost = mapper.writeValueAsString(p.getUrlHost());

				if(!hostList.containsKey(urlhost)){
					hostList.put(urlhost,1);
				}else{
					int value = (Integer)hostList.get(urlhost);
					hostList.put(urlhost,value+1);
				}					
				
			}
			
			SortedSet<String> keys = new TreeSet<String>(hostList.keySet());
			for (String key : keys) { 
			   String value = hostList.get(key).toString();
			   String[] tmp = key.toString().split("-");
				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Host",key,"Count",
						 hostList.get(key).toString()));
				 //logger.info(key.toString()+":"+value);

			}
			executionMap.put("hostData", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getAllIp(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			//HashMap hostList = new HashMap();
			TreeMap ipList = new TreeMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				String ip = mapper.writeValueAsString(p.getIp());

				if(!ipList.containsKey(ip)){
					ipList.put(ip,1);
				}else{
					int value = (Integer)ipList.get(ip);
					ipList.put(ip,value+1);
				}
			}
			
			Map sortedMap = sortByValues(ipList);
			 
		    // Get a set of the entries on the sorted map
		    Set set = sortedMap.entrySet();
		 
		    // Get an iterator
		    Iterator i = set.iterator();
		 
		    // Display elements
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      //logger.info(me.getKey() + ": "+me.getValue());
		      output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Ip",me.getKey(),"Count",
		    		  me.getValue().toString()));
		    }
			executionMap.put("IpData", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getIpCountWithDays(String sessionId, String ip){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			criteria.add(Restrictions.eq("ip", ip));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap dateList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				if(Integer.parseInt(day)<10){
					if(!dateList.containsKey(month+"-0"+day)){
						dateList.put(month+"-0"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-0"+day);
						dateList.put(month+"-0"+day,value+1);
					}					
				}else{
					if(!dateList.containsKey(month+"-"+day)){
						dateList.put(month+"-"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-"+day);
						dateList.put(month+"-"+day,value+1);
					}					
				}				
			}			
			SortedSet<String> keys = new TreeSet<String>(dateList.keySet());
			for (String key : keys) { 
			   String value = dateList.get(key).toString();
			   String[] tmp = key.toString().split("-");
				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
						 dateList.get(key).toString()));
				 //logger.info(key.toString()+":"+value);
			}
			executionMap.put("IP", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getHostCountWithDays(String sessionId, String host){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			criteria.add(Restrictions.eq("urlHost", host));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap dateList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				if(Integer.parseInt(day)<10){
					if(!dateList.containsKey(month+"-0"+day)){
						dateList.put(month+"-0"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-0"+day);
						dateList.put(month+"-0"+day,value+1);
					}					
				}else{
					if(!dateList.containsKey(month+"-"+day)){
						dateList.put(month+"-"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-"+day);
						dateList.put(month+"-"+day,value+1);
					}					
				}				
			}			
			SortedSet<String> keys = new TreeSet<String>(dateList.keySet());
			for (String key : keys) { 
			   String value = dateList.get(key).toString();
			   String[] tmp = key.toString().split("-");
				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
						 dateList.get(key).toString()));
				 //logger.info(key.toString()+":"+value);
			}
			executionMap.put("HOST", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getIntervalWithHost(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap StartDateList = new HashMap();
			HashMap EndDateList = new HashMap();
			HashMap hostList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String host = mapper.writeValueAsString(p.getUrlHost());
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				//String year = mapper.writeValueAsString(p.getCreatedDateTime().getYear());
				String date="";
				if(Integer.parseInt(day)<10){
					date=month+"-0"+day;
				}else{
					date=month+"-"+day;
				}	
				
				if(!hostList.containsKey(host)){					
					hostList.put(host,date+"@"+date);					
				}else{
					SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
					String[] cmp = hostList.get(host).toString().split("@");
					String[] tmp = cmp[0].split("-");
					int mon=Integer.parseInt(tmp[0]);					
					String[] tmp2 = cmp[1].split("-");
					int mon2=Integer.parseInt(tmp2[0]);
					Date d = formatter.parse(date);
					Date t = formatter.parse(Integer.toString(mon+1)+"-"+tmp[1]);
					Date t2 = formatter.parse(Integer.toString(mon2+1)+"-"+tmp2[1]);
					if(d.before(t)&&d.before(t2)){
						hostList.put(host,date+"@"+cmp[1]);
					}else if(d.after(t2)&&d.after(t)){
						hostList.put(host,cmp[0]+"@"+date);
					}
				}				
			}
			TreeMap orderList = new TreeMap();
			SortedSet<String> keys = new TreeSet<String>(hostList.keySet());
			for (String key : keys) { 
			   String value = hostList.get(key).toString();
			   String[] tmp = value.toString().split("@");
			   SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
			   Date startDate = formatter.parse(tmp[0]);
			   Date endDate = formatter.parse(tmp[1]);
			   long dif = startDate.getTime()-endDate.getTime();
				 //output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
					//	 hostList.get(key).toString()));
			   if(dif!=0){
				   orderList.put(key, dif);
			   }				 
			}
			Map sortedMap = sortByValues(orderList);			 
		    // Get a set of the entries on the sorted map
		    Set set = sortedMap.entrySet();		 
		    // Get an iterator
		    Iterator i = set.iterator();		 
		    // Display elements
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      //logger.info(me.getKey() + ": "+hostList.get(me.getKey()).toString());
		      String[] tmp = hostList.get(me.getKey()).toString().split("@");
		      String[] sDate= tmp[0].split("-");
		      String[] eDate= tmp[1].split("-");
		      output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("host",me.getKey(),"sMonth",
		    		  sDate[0],"sDay",sDate[1],"eMonth",eDate[0],"eDay",eDate[1]));
		    }
			
			executionMap.put("Interval", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	public List<?>getHoursData(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.asc("createdDateTime"));
//			criteria.add(Restrictions.eq("urlHost", host));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();

			logger.info(list.size());
			String tmpTime="";
			String hostStr="";
			int count=0;
			for (Userevent p : list) {
				String host = mapper.writeValueAsString(p.getUrlHost());
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				String hour = mapper.writeValueAsString(p.getCreatedDateTime().getHours());
				String nowTime=month+"-"+day+"-"+hour;
				if(tmpTime.equals(nowTime)){
					count=count+1;	
					hostStr = hostStr + ","+ host;
				}else if(!tmpTime.equals("")){
					output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",month,"Day",day,"Hour",hour,"Count",count));
					//logger.info(nowTime+":"+count+":"+hostStr);
					tmpTime=nowTime;
					count=1;
					hostStr = host;
				}else{
					tmpTime=nowTime;
					count=1;
					hostStr = host;
				}
			}			
			executionMap.put("HOURS", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	public List<?>getRefAndURL(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.asc("createdDateTime"));
//			criteria.add(Restrictions.eq("urlHost", host));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap HostRefList = new HashMap();
			HashMap LinkCount = new HashMap();
			logger.info(list.size());
			String tmpTime="";
			String hostStr="";

			for (Userevent p : list) {
				String host = mapper.writeValueAsString(p.getUrlHost());
				String url = mapper.writeValueAsString(p.getUrlPath());
				String ref = mapper.writeValueAsString(p.getUrlReferer());
				ref=ref.replace("\"", "");
				host=host.replace("\"", "");
				//logger.info(host);
				if(ref.startsWith("http")){
					URL refUrl = new URL(ref);					
					if(!HostRefList.containsKey(refUrl.getHost())){
						HostRefList.put(refUrl.getHost(), host);
					}else{
						String urls = (String)HostRefList.get(refUrl.getHost());						
						if(!urls.contains(host)){
							urls = urls +","+host;
							HostRefList.put(refUrl.getHost(), urls);
						}						
					}
					if((!LinkCount.containsKey(refUrl.getHost()))&&(!LinkCount.containsKey(host))){
						if(!refUrl.getHost().equals(host)){
							LinkCount.put(refUrl.getHost(), 1);
							LinkCount.put(host, 1);
						}else{
							LinkCount.put(host, 2);
						}
					}else if((LinkCount.containsKey(refUrl.getHost()))&&(LinkCount.containsKey(host))){
						int val = (Integer)LinkCount.get(refUrl.getHost());
						LinkCount.put(refUrl.getHost(), val+2);						
					}else if(LinkCount.containsKey(refUrl.getHost())){
						LinkCount.put(host,1);
						int val = (Integer)LinkCount.get(refUrl.getHost());
						LinkCount.put(refUrl.getHost(), val+1);
					}else if(LinkCount.containsKey(host)){
						LinkCount.put(refUrl.getHost(),1);
						int val = (Integer)LinkCount.get(host);
						LinkCount.put(host, val+1);
					}
				}				
			}
//			for (Object key : LinkCount.keySet()) {
//				logger.info(key + " : " + LinkCount.get(key));
//			}
			for (Object key : HostRefList.keySet()) {
	            //logger.info(key + " : " + HostRefList.get(key));
	            String urls = (String)HostRefList.get(key);
	            String[] url = urls.split(",");
	            int countURL = url.length;
	            if(urls.contains(key.toString())){
	            	countURL = countURL-1;
	            }
	            if(countURL!=0){
	            	output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("REF",key,"sub",countURL,"count",LinkCount.get(key)));
	            	for(String s : url){	
		            	if(!s.equals(key.toString())){
		            		output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("url",s,"ref",key,"UrlCount",LinkCount.get(s),"RefCount",LinkCount.get(key)));
		            	}
		            }
	            }	            
	            
	        }

			executionMap.put("RefUrl", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	public List<?>getDailyDataByDate(String sessionId, String date){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(originalFormat.parse(date));
			cal.add(Calendar.DAY_OF_YEAR, 1);
			Date date1 = originalFormat.parse(date.toString());
			Date date2 = cal.getTime();
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("usereventId"));
			criteria.add(Restrictions.ge("createdDateTime", date1));
			criteria.add(Restrictions.lt("createdDateTime", date2));
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			Map executionMap = new LinkedHashMap();
			ObjectMapper mapper = new ObjectMapper();
			TreeMap orderList = new TreeMap();
			for (Userevent p : list) {
				String host = mapper.writeValueAsString(p.getUrlHost());
				host = host.replace("\"", "");
				if(!orderList.containsKey(host)){
					orderList.put(host, 1);
				}else{
					int value = (Integer)orderList.get(host);
					orderList.put(host, value+1);
				}
			}
			Map sortedMap = sortByValues(orderList);

		    // Get a set of the entries on the sorted map
		    Set set = sortedMap.entrySet();		 
		    // Get an iterator
		    Iterator i = set.iterator();		 
		    // Display elements
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      //logger.info(me.getKey() + ": "+me.getValue());
		      output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("host",me.getKey(),"Count",
		    		  me.getValue().toString()));
		    }
			executionMap.put("DailyData", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	public List<?>getDailyHoursDataByHost(String sessionId, String host){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.asc("createdDateTime"));
			criteria.add(Restrictions.eq("urlHost", host));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();			
			HashMap hourCount = new HashMap();
			for(int h=0;h<24;h++){
				if(h<10){
					hourCount.put("0"+Integer.toString(h), 0);
				}else{
					hourCount.put(Integer.toString(h), 0);
				}
			}
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String hour = mapper.writeValueAsString(p.getCreatedDateTime().getHours());
				if(Integer.parseInt(hour)<10)hour="0"+hour;
				
				if(hourCount.containsKey(hour)){
					int count=(Integer)hourCount.get(hour);
					hourCount.put(hour, count+1);
				}else{
					hourCount.put(hour, 1);
				}
			}
			SortedSet<String> keys = new TreeSet<String>(hourCount.keySet());
			String hours="";
			for (String key : keys){
				//logger.info(key+":"+hourCount.get(key).toString());				
				hours=hours+","+hourCount.get(key).toString();
			}
			hours=hours.substring(1);
			//logger.info(hours);
			output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("hours",hours));
			executionMap.put("WebHours", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;		
	}
	
	
	//Method for sorting the TreeMap based on values
	  public static <K, V extends Comparable<V>> Map<K, V>sortByValues(final Map<K, V> map) {
	    Comparator<K> valueComparator = 
	             new Comparator<K>() {
	      public int compare(K k1, K k2) {
	        int compare = 
	              map.get(k1).compareTo(map.get(k2));
	        if (compare == 0) 
	          return 1;
	        else 
	          return compare;
	      }
	    };
	 
	    Map<K, V> sortedByValues = 
	      new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	  }
}

