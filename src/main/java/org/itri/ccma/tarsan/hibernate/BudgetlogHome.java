package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Budgetlog.
 * @see org.itri.ccma.tarsan.hibernate.Budgetlog
 * @author Hibernate Tools
 */
@Stateless
public class BudgetlogHome {

	private static final Log log = LogFactory.getLog(BudgetlogHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Budgetlog transientInstance) {
		log.debug("persisting Budgetlog instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Budgetlog persistentInstance) {
		log.debug("removing Budgetlog instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Budgetlog merge(Budgetlog detachedInstance) {
		log.debug("merging Budgetlog instance");
		try {
			Budgetlog result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Budgetlog findById(long id) {
		log.debug("getting Budgetlog instance with id: " + id);
		try {
			Budgetlog instance = entityManager.find(Budgetlog.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
