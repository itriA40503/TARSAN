package org.itri.ccma.tarsan.hibernate;
// Generated 2016/7/1 ¤W¤È 11:08:27 by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class SplashSchedule.
 * @see .SplashSchedule
 * @author Hibernate Tools
 */
public class SplashScheduleHome {

	private static final Log log = LogFactory.getLog(SplashScheduleHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(SplashSchedule transientInstance) {
		log.debug("persisting SplashSchedule instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SplashSchedule instance) {
		log.debug("attaching dirty SplashSchedule instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SplashSchedule instance) {
		log.debug("attaching clean SplashSchedule instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SplashSchedule persistentInstance) {
		log.debug("deleting SplashSchedule instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SplashSchedule merge(SplashSchedule detachedInstance) {
		log.debug("merging SplashSchedule instance");
		try {
			SplashSchedule result = (SplashSchedule) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SplashSchedule findById(long id) {
		log.debug("getting SplashSchedule instance with id: " + id);
		try {
			SplashSchedule instance = (SplashSchedule) sessionFactory
					.getCurrentSession().get("SplashSchedule", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SplashSchedule instance) {
		log.debug("finding SplashSchedule instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("SplashSchedule")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
