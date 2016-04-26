package org.itri.ccma.tarsan.hibernate;
// default package
// Generated 2015/12/7 �U�� 03:35:46 by Hibernate Tools 3.4.0.CR1

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class Record.
 * @see .Record
 * @author Hibernate Tools
 */
@Stateless
public class RecordHome {

	private static final Log log = LogFactory.getLog(RecordHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(Record transientInstance) {
		log.debug("persisting Record instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(Record persistentInstance) {
		log.debug("removing Record instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public Record merge(Record detachedInstance) {
		log.debug("merging Record instance");
		try {
			Record result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Record findById(RecordId id) {
		log.debug("getting Record instance with id: " + id);
		try {
			Record instance = entityManager.find(Record.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
