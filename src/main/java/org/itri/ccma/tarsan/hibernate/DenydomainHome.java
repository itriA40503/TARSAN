package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Denydomain.
 * @see org.itri.ccma.tarsan.hibernate.Denydomain
 * @author Hibernate Tools
 */
@Stateless
public class DenydomainHome {

	private static final Log log = LogFactory.getLog(DenydomainHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Denydomain transientInstance) {
		log.debug("persisting Denydomain instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Denydomain persistentInstance) {
		log.debug("removing Denydomain instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Denydomain merge(Denydomain detachedInstance) {
		log.debug("merging Denydomain instance");
		try {
			Denydomain result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Denydomain findById(long id) {
		log.debug("getting Denydomain instance with id: " + id);
		try {
			Denydomain instance = entityManager.find(Denydomain.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
