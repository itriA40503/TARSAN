package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Budgetpool.
 * @see org.itri.ccma.tarsan.hibernate.Budgetpool
 * @author Hibernate Tools
 */
@Stateless
public class BudgetpoolHome {

	private static final Log log = LogFactory.getLog(BudgetpoolHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Budgetpool transientInstance) {
		log.debug("persisting Budgetpool instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Budgetpool persistentInstance) {
		log.debug("removing Budgetpool instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Budgetpool merge(Budgetpool detachedInstance) {
		log.debug("merging Budgetpool instance");
		try {
			Budgetpool result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Budgetpool findById(long id) {
		log.debug("getting Budgetpool instance with id: " + id);
		try {
			Budgetpool instance = entityManager.find(Budgetpool.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
