package org.itri.ccma.tarsan.hibernate;
// default package
// Generated 2015/12/7 �U�� 03:35:46 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Barcode.
 * @see .Barcode
 * @author Hibernate Tools
 */
@Stateless
public class BarcodeHome {

	private static final Log log = LogFactory.getLog(BarcodeHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Barcode transientInstance) {
		log.debug("persisting Barcode instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Barcode persistentInstance) {
		log.debug("removing Barcode instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Barcode merge(Barcode detachedInstance) {
		log.debug("merging Barcode instance");
		try {
			Barcode result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Barcode findById(long id) {
		log.debug("getting Barcode instance with id: " + id);
		try {
			Barcode instance = entityManager.find(Barcode.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
