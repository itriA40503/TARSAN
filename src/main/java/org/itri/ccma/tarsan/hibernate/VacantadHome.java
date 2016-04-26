package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Vacantad.
 * @see org.itri.ccma.tarsan.hibernate.Vacantad
 * @author Hibernate Tools
 */
@Stateless
public class VacantadHome {

	private static final Log log = LogFactory.getLog(VacantadHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Vacantad transientInstance) {
		log.debug("persisting Vacantad instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Vacantad persistentInstance) {
		log.debug("removing Vacantad instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Vacantad merge(Vacantad detachedInstance) {
		log.debug("merging Vacantad instance");
		try {
			Vacantad result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Vacantad findById(long id) {
		log.debug("getting Vacantad instance with id: " + id);
		try {
			Vacantad instance = entityManager.find(Vacantad.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
