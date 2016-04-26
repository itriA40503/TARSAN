package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/29 �U�� 05:29:39 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Logad.
 * @see org.itri.ccma.tarsan.hibernate.Logad
 * @author Hibernate Tools
 */
@Stateless
public class LogadHome {

	private static final Log log = LogFactory.getLog(LogadHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Logad transientInstance) {
		log.debug("persisting Logad instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Logad persistentInstance) {
		log.debug("removing Logad instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Logad merge(Logad detachedInstance) {
		log.debug("merging Logad instance");
		try {
			Logad result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Logad findById(long id) {
		log.debug("getting Logad instance with id: " + id);
		try {
			Logad instance = entityManager.find(Logad.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
