package org.itri.ccma.tarsan.biz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;






import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.InputValidator;
import org.itri.ccma.tarsan.util.MessageUtil;

import com.fasterxml.jackson.databind.ObjectMapper;


public class BoClassify {
	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoClassify() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoClassify getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoClassify INSTANCE = new BoClassify();
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getCategory(String sessionId, String ip){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		if(!InputValidator.validator("ipv4", ip)){
			return  MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_ERROR, methodName,
					sessionId, "IP ERROR");
		}
//		logger.error(InputValidator.validator("ipv4", ip));
		try{
//			SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar cal = Calendar.getInstance();
//			Date current = new Date();			
//			cal.add(Calendar.DAY_OF_YEAR, 1);
//			Date date1 = originalFormat.parse(originalFormat.format(current));
//			Date date2 = cal.getTime();
//			Criteria criteria = session.createCriteria(Userevent.class);
//			criteria.addOrder(Order.desc("urlPath"));			
//			//# get ip
//			criteria.add(Restrictions.eq("ip", ip));
//			//# get today data
//			criteria.add(Restrictions.ge("createdDateTime", date1));
//			criteria.add(Restrictions.lt("createdDateTime", date2));
//			logger.info(date1+"==="+date2);
////			criteria.setMaxResults(20000);
//			List<Userevent> list = criteria.list();
//			List<String> output = new ArrayList();
//			ObjectMapper mapper = new ObjectMapper();
//			Map executionMap = new LinkedHashMap();
//			HashMap dateList = new HashMap();
//			
//			if(list.size()==0){
//				return resultList =  MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
//						sessionId, "no data");
//			} 
			
//			logger.info(list.size());
//			ArrayList<String> kwList = new ArrayList();
//			ArrayList<String> shops = getShopList();
//			String kw ="";
//			for (Userevent p : list) {
//				String url = p.getUrlPath();
//				logger.info(url);
//				if(ecommerceWeb(url,shops)){
//					kw = matchKeyword(url);
//					if(matchKeyword(url)!="0"){
//						if(kwList.contains(kw) == false){
//							kwList.add(kw);							
//						}
//					}
//				}			
//			}
//			logger.info(kwList.size());
//			for (String kw1: kwList) {
//			    logger.info(kw1);
//			}

//			SortedSet<String> keys = new TreeSet<String>(dateList.keySet());
//			for (String key : keys) { 
//			   String value = dateList.get(key).toString();
//			   String[] tmp = key.toString().split("-");
//				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
//						 dateList.get(key).toString()));
//				 //logger.info(key.toString()+":"+value);
//			}
//			executionMap.put("IP", output);
//			resultList.add(executionMap);
			ArrayList<String> types = new ArrayList();
			types.add("food");
			types.add("clothing");
			types.add("housing");
			types.add("transportation");
			types.add("education");
			types.add("entertainment");
			Random randomGenerator = new Random();
			int rand = (int)(Math.random()*6);
			Collections.shuffle(types);
			String randTypes="";
			logger.info("rand:"+rand);
			if(rand == 0)return resultList =  MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "No Data");
				
			for(int i = 0; i < rand; i++){
				if(i==0){
					randTypes = types.get(i);
				}else{
					randTypes = randTypes + "," +types.get(i);	
				}				
			}
			resultList =  MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, randTypes);
			
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
	
	private String matchKeyword(String url){
		String regex ="\\?q=(.*?)[#&]|\\?q=(.*)|nkw=(.*?)&|term=(.*)|query=(.*?)[\\W]|keywords=(.*?)&|keywords=(.*)|&k=(.*?)&|Keyword=(.*?)[&]|keyword=(.*?)[\\W]|\\?p=(.*?)&|&q=(.*?)&|\\?key=(.*?)&|&k=(.*)|\\?q=(.*?)&|Keyword=(.*)|c_name=(.*?)[\\W]|search=(.*)|rakuten.com.tw\\/search\\/(.*?)\\/|searchword=(.*)|st=(.*?)&_|&origkw=(.*?)&|[\\?]Ntt=(.*)|[\\W]Ntt=(.*?)[#&]|keyword%5B%5D=(.*?)&";
        Pattern pattern = null;
        Matcher matcher = null;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(url);
        String kw="";
        if(matcher.find())
        {
            int i=1;
            for(i=1;i<25;i++)
            {
                if(matcher.group(i)!=null)
                {
                    break;
                }
            }
            try
            {               
                kw = java.net.URLDecoder.decode(matcher.group(i), "utf-8");
            }
            catch(Exception e){ } 
//            logger.error(kw);
           return kw;           
        }
        else
        {
        	return "0";
        }
		
	}
	
	private boolean ecommerceWeb(String url, List<String> shoplist){
	    for(String shop : shoplist){
	        Pattern pattern = Pattern.compile(shop);
	        Matcher matcher = pattern.matcher(url);
	        if(matcher.find()){
//	        	logger.info(matcher.toString());
	            //System.err.println("true");
	            return true;
	        }
	    }
	    //System.err.println("false");
	    return false;
	}
	
	private ArrayList getShopList(){
		String filename = "docs/ShopWebSite.txt";
	    ArrayList shoplist = new ArrayList();
	    try {
	        // get Canonical Path   
	        File file = new File(".");
	        String path = file.getCanonicalPath();       
	    
	        // set file name
	        String fileSeparator = System.getProperty("file.separator");
	        String fileName = path + fileSeparator + filename;
	        //System.err.println(fileName);
	        FileReader fr = new FileReader(fileName);
	        BufferedReader br = new BufferedReader(fr);
	        String line="";    // Query line            
	        while ((line = br.readLine()) != null) {
	            shoplist.add(line);
	            //System.err.println(line);
	        }
	        return shoplist;
	    } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }      
	    return shoplist;
	}  
}
