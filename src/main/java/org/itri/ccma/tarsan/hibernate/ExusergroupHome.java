package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Exusergroup.
 * @see org.itri.ccma.tarsan.hibernate.Exusergroup
 * @author Hibernate Tools
 */
@Stateless
public class ExusergroupHome {

	private static final Log log = LogFactory.getLog(ExusergroupHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Exusergroup transientInstance) {
		log.debug("persisting Exusergroup instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Exusergroup persistentInstance) {
		log.debug("removing Exusergroup instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Exusergroup merge(Exusergroup detachedInstance) {
		log.debug("merging Exusergroup instance");
		try {
			Exusergroup result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Exusergroup findById(long id) {
		log.debug("getting Exusergroup instance with id: " + id);
		try {
			Exusergroup instance = entityManager.find(Exusergroup.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
