package org.itri.ccma.tarsan.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * The Class HibernateUtil.
 */
@SuppressWarnings("deprecation")
public class HibernateUtil {

	/** The session factory. */
	private static SessionFactory sessionFactory;
	static {
		try {
			sessionFactory = new AnnotationConfiguration().configure()
					.buildSessionFactory();
			
		} catch (Throwable ex) {
			System.out.println(ex.toString());
			throw new ExceptionInInitializerError(ex);
			
		}
	}

	/**
	 * Gets the session factory.
	 *
	 * @return the session factory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Shutdown.
	 */
	public static void shutdown() {
		getSessionFactory().close();
	}
}
