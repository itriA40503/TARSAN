package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Exuserlog.
 * @see org.itri.ccma.tarsan.hibernate.Exuserlog
 * @author Hibernate Tools
 */
@Stateless
public class ExuserlogHome {

	private static final Log log = LogFactory.getLog(ExuserlogHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Exuserlog transientInstance) {
		log.debug("persisting Exuserlog instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Exuserlog persistentInstance) {
		log.debug("removing Exuserlog instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Exuserlog merge(Exuserlog detachedInstance) {
		log.debug("merging Exuserlog instance");
		try {
			Exuserlog result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Exuserlog findById(long id) {
		log.debug("getting Exuserlog instance with id: " + id);
		try {
			Exuserlog instance = entityManager.find(Exuserlog.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
