package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Postad.
 * @see org.itri.ccma.tarsan.hibernate.Postad
 * @author Hibernate Tools
 */
@Stateless
public class PostadHome {

	private static final Log log = LogFactory.getLog(PostadHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Postad transientInstance) {
		log.debug("persisting Postad instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Postad persistentInstance) {
		log.debug("removing Postad instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Postad merge(Postad detachedInstance) {
		log.debug("merging Postad instance");
		try {
			Postad result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Postad findById(PostadId id) {
		log.debug("getting Postad instance with id: " + id);
		try {
			Postad instance = entityManager.find(Postad.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
