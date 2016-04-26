package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class WebType.
 * @see org.itri.ccma.tarsan.hibernate.WebType
 * @author Hibernate Tools
 */
@Stateless
public class WebTypeHome {

	private static final Log log = LogFactory.getLog(WebTypeHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(WebType transientInstance) {
		log.debug("persisting WebType instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(WebType persistentInstance) {
		log.debug("removing WebType instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public WebType merge(WebType detachedInstance) {
		log.debug("merging WebType instance");
		try {
			WebType result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public WebType findById(long id) {
		log.debug("getting WebType instance with id: " + id);
		try {
			WebType instance = entityManager.find(WebType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
