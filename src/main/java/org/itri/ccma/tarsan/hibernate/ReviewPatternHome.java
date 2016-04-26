package org.itri.ccma.tarsan.hibernate;
// Generated 2016/1/4 �W�� 09:34:05 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class ReviewPattern.
 * @see .ReviewPattern
 * @author Hibernate Tools
 */
@Stateless
public class ReviewPatternHome {

	private static final Log log = LogFactory.getLog(ReviewPatternHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(ReviewPattern transientInstance) {
		log.debug("persisting ReviewPattern instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(ReviewPattern persistentInstance) {
		log.debug("removing ReviewPattern instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public ReviewPattern merge(ReviewPattern detachedInstance) {
		log.debug("merging ReviewPattern instance");
		try {
			ReviewPattern result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ReviewPattern findById(long id) {
		log.debug("getting ReviewPattern instance with id: " + id);
		try {
			ReviewPattern instance = entityManager
					.find(ReviewPattern.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
