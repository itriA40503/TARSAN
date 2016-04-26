package org.itri.ccma.tarsan.test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.registry.infomodel.User;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.BoPattern;
import org.itri.ccma.tarsan.hibernate.*;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;

import junit.framework.TestCase;

public class newer_test extends TestCase {
	private Logger logger = Logger.getLogger(newer_test.class);

	public newer_test() {
		super();
		logger.setLevel(Level.ALL);
	}

	// @SuppressWarnings("rawtypes")
	public void test() {
		logger.info("TEST START!");
		logger.info("***********************");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			
			List list;
			String test_str;
			// long users_id = 336;
			// Date current = new Date();
			// Users user = new Users();
			// Users user = (Users) session.get(Users.class, users_id);
			// user.setUsersId(users_id);
			// user.setTempkey("new_test");
			// user.setAccount("itri_test");
			// user.setPassword("new_test");
			// user.setCreatedDateTime(current);
			// user.setLastActiveDateTime(current);
			// session.update(user);
			// tx.commit();

			// # list of users
			// list = session.createCriteria(Userevent.class).list(); // create
			// list

			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.add(Restrictions.like("urlHost", "%apple%"));
			Iterator user = criteria.list().iterator();
			while (user.hasNext()) {
				Userevent users = (Userevent) user.next();
				logger.info(users.getUsereventId());
			}

		} catch (Exception e) {
			logger.debug(e);
			tx.rollback();
		} finally {
			session.close();
			logger.info("***********************");
			logger.info("TEST FINISH!");
		}

	}

	public void add_test(String user_account, String user_psw) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			List list;
			Date current = new Date();
			Users user = new Users();
			user.setAccount(user_account);
			user.setPassword(user_psw);
			user.setCreatedDateTime(current);
			user.setLastActiveDateTime(current);
			session.save(user);
			tx.commit();

		} catch (Exception e) {
			logger.debug(e);
			tx.rollback();
		} finally {
			session.close();
		}
	}

	public void list() {
		logger.info("***********TEST START!************");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			List list;
			String test_str;
			// # list of users
			list = session.createCriteria(Users.class).list(); // create list

			if (list.isEmpty()) {
				System.err
						.println("Abort testing on touching data -- No data in the database");
				return;
			} else {
				int count = list.size(); // get numbers of list
				int i = 0;
				while (i < list.size()) {
					test_str = ((Users) list.get(i)).getAccount().toString();
					logger.info(i + ":" + test_str);
					i++;
				}
			}
		} catch (Exception e) {
			logger.debug(e);
			tx.rollback();
		} finally {
			session.close();
			logger.info("***********TEST FINISH!************");
		}
	}

	public void user_delete(long user_id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Users user = new Users();
			user.setUsersId(user_id);
			session.delete(user);
			tx.commit();
		} catch (Exception e) {
			logger.debug(e);
			tx.rollback();
		} finally {
			session.close();
		}
	}
	
	public void update(long id,String username){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		logger.info("Upate START!");
		logger.info("***********************");
		try {
			Class k=Users.class;
			Users user = (Users)session.load(k, id);
			logger.info("User account : "+user.getAccount());
			logger.info("account change : "+username);
			Date current = new Date();
			user.setAccount(username);
			user.setLastActiveDateTime(current);
			session.update(user);
			tx.commit();

		} catch (Exception e) {
			logger.debug(e);
			tx.rollback();
		} finally {
			session.close();
			logger.info("***********************");
			logger.info("Update FINISH!");
		}
	}
	
	public void update2(long id,String username){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		logger.info("Upate2 START!");
		logger.info("***********************");
		try {
			Class k=Users.class;
			Criteria criteria = session.createCriteria(k);
			criteria.add(Restrictions.eq("usersId", id));	
			Users user = (Users) criteria.uniqueResult();
			logger.info("User account : "+user.getAccount());
			logger.info("account change : "+username);
			Date current = new Date();
			user.setAccount(username);
			user.setLastActiveDateTime(current);
			session.persist(user);
			tx.commit();

		} catch (Exception e) {
			logger.debug(e);
			tx.rollback();
		} finally {
			session.close();
			logger.info("***********************");
			logger.info("Update FINISH!");
		}
	}

	public void search(String str) {
		logger.info("TEST START!");
		logger.info("***********************");
		logger.info("Search:"+str);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Users.class);
			criteria.add(Restrictions.like("account", "%"+str+"%"));
			Iterator user = criteria.list().iterator();
			while (user.hasNext()) {
				Users users = (Users) user.next();				
				logger.info(users.getUsersId()+":"+users.getAccount());
			}

		} catch (Exception e) {
			logger.debug(e);
			tx.rollback();
		} finally {
			session.close();
			logger.info("***********************");
			logger.info("TEST FINISH!");
		}
	}
	
	public void check(String account){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Users.class);
			criteria.add(Restrictions.like("account", account));
			Iterator user = criteria.list().iterator();
			if (user.hasNext()) {
				logger.info("This acoount is already registed!");				
			}
			else{
				logger.info("You can use this account ~");
			}
		} catch (Exception e) {
			logger.debug(e);
			tx.rollback();
		} finally {
			session.close();
		}
	}

}
