package org.itri.ccma.tarsan.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.exceptions.LogicException;
import org.itri.ccma.tarsan.hibernate.Barcode;
import org.itri.ccma.tarsan.hibernate.Record;
import org.itri.ccma.tarsan.hibernate.RecordId;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.MessageUtil;
import org.itri.ccma.tarsan.util.SignatureUtil;

/**
 * The Class BoTest.
 */
public class BoTest {

	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoTest() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoTest getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoTest INSTANCE = new BoTest();
	}

	/****************************************************************************************/
	/*
	 * Utility function
	 */
	/****************************************************************************************/

	@SuppressWarnings("rawtypes")
	public String randombarcode(){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = session.beginTransaction();
		try {
			List list;
			Random ran = new Random();
			// # list of barcode
			list = session.createCriteria(Barcode.class).list(); // create list
			int i = ran.nextInt((list.size()))+1; // random(size)= 0~size-1
			return String.valueOf(i);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		
		return "";
	}
	
	public String getbarcode(String code_id) {

		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = session.beginTransaction();

		try {
			Criteria criteria = session.createCriteria(Barcode.class);
			criteria.add(Restrictions.eq("barcodeId", Long.parseLong(code_id)));
			Barcode barcode = (Barcode) criteria.uniqueResult();
			String shop = barcode.getShop().toString();
			return "This barcode : " + code_id + " from " + shop;

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		} finally {
			session.close();
		}

		return null;
	}	
	
	/****************************************************************************************/
	/*
	 * Logical function
	 */
	/****************************************************************************************/
	
	public String createUser(String username, String password) {

		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {

//			Criteria criteria_account = session.createCriteria(Users.class);
//			criteria_account.add(Restrictions.eq("account", username));

//			List result_account = criteria_account.list();

//			if (result_account.size() != 0) {
//				throw new LogicException(String.format(
//						"Account with name [%s] is already registered",
//						username), Configurations.CODE_ERROR);
//			}

			Date currentDate = new Date();
			Users user = new Users();
			user.setAccount(username);
			user.setPassword(password);
			user.setCreatedDateTime(currentDate);
			user.setLastActiveDateTime(currentDate);
			session.save(user);
			tx.commit();
			logger.info("User already been created.");

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}

		} finally {
			session.close();
		}
		return "DONE!";

	}
	@SuppressWarnings({ "rawtypes" })
	public String DeleteUser(String username, String password) {

		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			Criteria criteria_account = session.createCriteria(Users.class);
			criteria_account.add(Restrictions.eq("account", username));
			criteria_account.add(Restrictions.eq("password", password));
			List result_account = criteria_account.list();
			Users user = (Users) criteria_account.uniqueResult();
			if (result_account.size() == 0) {
				throw new LogicException(String.format(
						"Database don't have this account : [%s] ", username),
						Configurations.CODE_ERROR);
			}

			session.delete(user);
			tx.commit();

			return "Del DONE!";

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}

		} finally {
			session.close();
		}

		return "DONE";

	}

	public String madehashkey(String password, int i) {
		String algorithm = null;
		if(i==0){
			algorithm = "md5";
		}
		else if(i==1){
			algorithm = "sha512";
		}		
		String hashkey = SignatureUtil.createHash(algorithm, password);
		return hashkey;
	}
	@SuppressWarnings({ "rawtypes" })
	public String check_key(String password){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			Criteria criteria_account = session.createCriteria(Users.class);
			criteria_account.add(Restrictions.eq("password", password));

			List result_account = criteria_account.list();
			
			if(result_account.size()==0){
				throw new LogicException(String.format(
						"PASSWORD NOT EXIST"), Configurations.CODE_ERROR);
			}else{
				return "PASSWORD EXIST";
			}			
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}			
		}finally{
			session.close();
		}		
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List<?> record_use_barcode(String sessionId,String user_id, String barcode_id, String coor_x, String coor_y){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			RecordId recordId = new RecordId();
			recordId.setBarcodeId(Long.parseLong(barcode_id));
			recordId.setUsersId(Long.parseLong(user_id));			
			Date currentDate = new Date();
			Record record = new Record();
			record.setId(recordId);
			record.setRecordDateTime(currentDate);
			record.setCoordinateX(coor_x);
			record.setCoordinateY(coor_y);
			session.save(record);
			tx.commit();
			resultList=MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "User(" + user_id + ") use barcode", "barcodeId",barcode_id);
			return resultList;
//			return "User("+user_id+") use barcode("+barcode_id+") in x:"+coor_x+" y:"+coor_y;
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		}finally{
			session.close();

		}		
		return resultList;
		
	}
	@SuppressWarnings("rawtypes")
	public String setBarcodeShop(String shop){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try{			
			List list = session.createCriteria(Barcode.class).list(); // create list
			list.size();
			Barcode barcode = new Barcode();
			barcode.setShop(shop);
			barcode.setBarcodeId(list.size()+1);
			session.save(barcode);
			tx.commit();
			return "Save Barcode Shop:"+shop;
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		
		return "";
		
	}
	
	

}
