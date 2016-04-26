package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Price.
 * @see org.itri.ccma.tarsan.hibernate.Price
 * @author Hibernate Tools
 */
@Stateless
public class PriceHome {

	private static final Log log = LogFactory.getLog(PriceHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Price transientInstance) {
		log.debug("persisting Price instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Price persistentInstance) {
		log.debug("removing Price instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Price merge(Price detachedInstance) {
		log.debug("merging Price instance");
		try {
			Price result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Price findById(long id) {
		log.debug("getting Price instance with id: " + id);
		try {
			Price instance = entityManager.find(Price.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
