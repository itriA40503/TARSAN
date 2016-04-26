package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Buyad.
 * @see org.itri.ccma.tarsan.hibernate.Buyad
 * @author Hibernate Tools
 */
@Stateless
public class BuyadHome {

	private static final Log log = LogFactory.getLog(BuyadHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Buyad transientInstance) {
		log.debug("persisting Buyad instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Buyad persistentInstance) {
		log.debug("removing Buyad instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Buyad merge(Buyad detachedInstance) {
		log.debug("merging Buyad instance");
		try {
			Buyad result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Buyad findById(long id) {
		log.debug("getting Buyad instance with id: " + id);
		try {
			Buyad instance = entityManager.find(Buyad.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
