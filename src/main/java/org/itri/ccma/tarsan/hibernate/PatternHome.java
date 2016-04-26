package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Pattern.
 * @see org.itri.ccma.tarsan.hibernate.Pattern
 * @author Hibernate Tools
 */
@Stateless
public class PatternHome {

	private static final Log log = LogFactory.getLog(PatternHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Pattern transientInstance) {
		log.debug("persisting Pattern instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Pattern persistentInstance) {
		log.debug("removing Pattern instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Pattern merge(Pattern detachedInstance) {
		log.debug("merging Pattern instance");
		try {
			Pattern result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Pattern findById(long id) {
		log.debug("getting Pattern instance with id: " + id);
		try {
			Pattern instance = entityManager.find(Pattern.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
