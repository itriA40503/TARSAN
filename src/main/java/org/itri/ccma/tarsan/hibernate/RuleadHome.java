package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/10 �W�� 09:36:51 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Rulead.
 * @see org.itri.ccma.tarsan.hibernate.Rulead
 * @author Hibernate Tools
 */
@Stateless
public class RuleadHome {

	private static final Log log = LogFactory.getLog(RuleadHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Rulead transientInstance) {
		log.debug("persisting Rulead instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Rulead persistentInstance) {
		log.debug("removing Rulead instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Rulead merge(Rulead detachedInstance) {
		log.debug("merging Rulead instance");
		try {
			Rulead result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Rulead findById(long id) {
		log.debug("getting Rulead instance with id: " + id);
		try {
			Rulead instance = entityManager.find(Rulead.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
