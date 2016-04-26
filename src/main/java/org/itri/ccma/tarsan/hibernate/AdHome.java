package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Ad.
 * @see org.itri.ccma.tarsan.hibernate.Ad
 * @author Hibernate Tools
 */
@Stateless
public class AdHome {

	private static final Log log = LogFactory.getLog(AdHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Ad transientInstance) {
		log.debug("persisting Ad instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Ad persistentInstance) {
		log.debug("removing Ad instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Ad merge(Ad detachedInstance) {
		log.debug("merging Ad instance");
		try {
			Ad result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Ad findById(long id) {
		log.debug("getting Ad instance with id: " + id);
		try {
			Ad instance = entityManager.find(Ad.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
