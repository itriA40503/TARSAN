package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Users.
 * @see org.itri.ccma.tarsan.hibernate.Users
 * @author Hibernate Tools
 */
@Stateless
public class UsersHome {

	private static final Log log = LogFactory.getLog(UsersHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Users transientInstance) {
		log.debug("persisting Users instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Users persistentInstance) {
		log.debug("removing Users instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Users merge(Users detachedInstance) {
		log.debug("merging Users instance");
		try {
			Users result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Users findById(long id) {
		log.debug("getting Users instance with id: " + id);
		try {
			Users instance = entityManager.find(Users.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
