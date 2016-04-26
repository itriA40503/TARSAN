package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class AdGroup.
 * @see org.itri.ccma.tarsan.hibernate.AdGroup
 * @author Hibernate Tools
 */
@Stateless
public class AdGroupHome {

	private static final Log log = LogFactory.getLog(AdGroupHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(AdGroup transientInstance) {
		log.debug("persisting AdGroup instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(AdGroup persistentInstance) {
		log.debug("removing AdGroup instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public AdGroup merge(AdGroup detachedInstance) {
		log.debug("merging AdGroup instance");
		try {
			AdGroup result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AdGroup findById(long id) {
		log.debug("getting AdGroup instance with id: " + id);
		try {
			AdGroup instance = entityManager.find(AdGroup.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
