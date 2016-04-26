package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Exuser.
 * @see org.itri.ccma.tarsan.hibernate.Exuser
 * @author Hibernate Tools
 */
@Stateless
public class ExuserHome {

	private static final Log log = LogFactory.getLog(ExuserHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Exuser transientInstance) {
		log.debug("persisting Exuser instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Exuser persistentInstance) {
		log.debug("removing Exuser instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Exuser merge(Exuser detachedInstance) {
		log.debug("merging Exuser instance");
		try {
			Exuser result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Exuser findById(long id) {
		log.debug("getting Exuser instance with id: " + id);
		try {
			Exuser instance = entityManager.find(Exuser.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
