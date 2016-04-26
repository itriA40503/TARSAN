package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class PatternType.
 * @see org.itri.ccma.tarsan.hibernate.PatternType
 * @author Hibernate Tools
 */
@Stateless
public class PatternTypeHome {

	private static final Log log = LogFactory.getLog(PatternTypeHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(PatternType transientInstance) {
		log.debug("persisting PatternType instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(PatternType persistentInstance) {
		log.debug("removing PatternType instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public PatternType merge(PatternType detachedInstance) {
		log.debug("merging PatternType instance");
		try {
			PatternType result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PatternType findById(long id) {
		log.debug("getting PatternType instance with id: " + id);
		try {
			PatternType instance = entityManager.find(PatternType.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
