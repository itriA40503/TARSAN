package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Pattern2ad.
 * @see org.itri.ccma.tarsan.hibernate.Pattern2ad
 * @author Hibernate Tools
 */
@Stateless
public class Pattern2adHome {

	private static final Log log = LogFactory.getLog(Pattern2adHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Pattern2ad transientInstance) {
		log.debug("persisting Pattern2ad instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Pattern2ad persistentInstance) {
		log.debug("removing Pattern2ad instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Pattern2ad merge(Pattern2ad detachedInstance) {
		log.debug("merging Pattern2ad instance");
		try {
			Pattern2ad result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Pattern2ad findById(long id) {
		log.debug("getting Pattern2ad instance with id: " + id);
		try {
			Pattern2ad instance = entityManager.find(Pattern2ad.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
