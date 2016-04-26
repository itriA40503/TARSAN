package org.itri.ccma.tarsan.daemon;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartListener implements ServletContextListener {

	private Logger logger = Logger.getLogger(StartListener.class);
	
	private static final long period = 30000;
	private Timer timer = null;
	

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("##contextDestroyed");
		timer.cancel();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("##contextInitialized");
		timer = new Timer();
//		timer.schedule(new ParkingLotSyncTask(), 0, period);
//		timer.schedule(new ReserveRecordSyncTask(), 0, period);
		
		//start up the spring cron tasks
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
	}
}
