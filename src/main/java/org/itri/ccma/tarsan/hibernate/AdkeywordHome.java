package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Adkeyword.
 * @see org.itri.ccma.tarsan.hibernate.Adkeyword
 * @author Hibernate Tools
 */
@Stateless
public class AdkeywordHome {

	private static final Log log = LogFactory.getLog(AdkeywordHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Adkeyword transientInstance) {
		log.debug("persisting Adkeyword instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Adkeyword persistentInstance) {
		log.debug("removing Adkeyword instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Adkeyword merge(Adkeyword detachedInstance) {
		log.debug("merging Adkeyword instance");
		try {
			Adkeyword result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Adkeyword findById(long id) {
		log.debug("getting Adkeyword instance with id: " + id);
		try {
			Adkeyword instance = entityManager.find(Adkeyword.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
