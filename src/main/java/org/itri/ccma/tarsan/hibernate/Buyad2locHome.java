package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Buyad2loc.
 * @see org.itri.ccma.tarsan.hibernate.Buyad2loc
 * @author Hibernate Tools
 */
@Stateless
public class Buyad2locHome {

	private static final Log log = LogFactory.getLog(Buyad2locHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Buyad2loc transientInstance) {
		log.debug("persisting Buyad2loc instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Buyad2loc persistentInstance) {
		log.debug("removing Buyad2loc instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Buyad2loc merge(Buyad2loc detachedInstance) {
		log.debug("merging Buyad2loc instance");
		try {
			Buyad2loc result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Buyad2loc findById(long id) {
		log.debug("getting Buyad2loc instance with id: " + id);
		try {
			Buyad2loc instance = entityManager.find(Buyad2loc.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
