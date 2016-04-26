package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Runad.
 * @see org.itri.ccma.tarsan.hibernate.Runad
 * @author Hibernate Tools
 */
@Stateless
public class RunadHome {

	private static final Log log = LogFactory.getLog(RunadHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Runad transientInstance) {
		log.debug("persisting Runad instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Runad persistentInstance) {
		log.debug("removing Runad instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Runad merge(Runad detachedInstance) {
		log.debug("merging Runad instance");
		try {
			Runad result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Runad findById(RunadId id) {
		log.debug("getting Runad instance with id: " + id);
		try {
			Runad instance = entityManager.find(Runad.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
