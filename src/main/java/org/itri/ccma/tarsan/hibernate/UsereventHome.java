package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Userevent.
 * @see org.itri.ccma.tarsan.hibernate.Userevent
 * @author Hibernate Tools
 */
@Stateless
public class UsereventHome {

	private static final Log log = LogFactory.getLog(UsereventHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Userevent transientInstance) {
		log.debug("persisting Userevent instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Userevent persistentInstance) {
		log.debug("removing Userevent instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Userevent merge(Userevent detachedInstance) {
		log.debug("merging Userevent instance");
		try {
			Userevent result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Userevent findById(long id) {
		log.debug("getting Userevent instance with id: " + id);
		try {
			Userevent instance = entityManager.find(Userevent.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
