package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Ad2user.
 * @see org.itri.ccma.tarsan.hibernate.Ad2user
 * @author Hibernate Tools
 */
@Stateless
public class Ad2userHome {

	private static final Log log = LogFactory.getLog(Ad2userHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Ad2user transientInstance) {
		log.debug("persisting Ad2user instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Ad2user persistentInstance) {
		log.debug("removing Ad2user instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Ad2user merge(Ad2user detachedInstance) {
		log.debug("merging Ad2user instance");
		try {
			Ad2user result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Ad2user findById(long id) {
		log.debug("getting Ad2user instance with id: " + id);
		try {
			Ad2user instance = entityManager.find(Ad2user.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
