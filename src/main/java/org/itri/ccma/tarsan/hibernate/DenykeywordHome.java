package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Denykeyword.
 * @see org.itri.ccma.tarsan.hibernate.Denykeyword
 * @author Hibernate Tools
 */
@Stateless
public class DenykeywordHome {

	private static final Log log = LogFactory.getLog(DenykeywordHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Denykeyword transientInstance) {
		log.debug("persisting Denykeyword instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Denykeyword persistentInstance) {
		log.debug("removing Denykeyword instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Denykeyword merge(Denykeyword detachedInstance) {
		log.debug("merging Denykeyword instance");
		try {
			Denykeyword result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Denykeyword findById(long id) {
		log.debug("getting Denykeyword instance with id: " + id);
		try {
			Denykeyword instance = entityManager.find(Denykeyword.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
