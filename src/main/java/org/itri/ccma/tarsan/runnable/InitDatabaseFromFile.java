package org.itri.ccma.tarsan.runnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.BoPattern;
import org.itri.ccma.tarsan.biz.BoUser;
import org.itri.ccma.tarsan.hibernate.PatternType;
import org.itri.ccma.tarsan.hibernate.WebType;
//import org.itri.ccma.tarsan.test.BoUserTest;
import org.itri.ccma.tarsan.util.HibernateUtil;

public class InitDatabaseFromFile {
	//private static Logger logger = Logger.getLogger(BoUserTest.class);

	private static String patternFilename = "pattern.txt";
	private static String userFilename = "user.txt";
	private static String patternTypeFilename = "pattern_type.txt";
	private static String webTypeFilename = "web_type.txt";

	private static final int CODE_PATTERN = 100;
	private static final int CODE_USER = 200;
	private static final int CODE_PATTERN_TYPE = 300;
	private static final int CODE_WEB_TYPE = 400;

	public static void main(String[] args) {
		InitDatabaseFromFile var = new InitDatabaseFromFile();
		//logger.setLevel(Level.ALL);

		String separator = ",";
		ClassLoader classLoader = var.getClass().getClassLoader();
		File file;
		file = new File(classLoader.getResource(userFilename).getFile());
		var.initData(file.toString(), CODE_USER, separator);

		file = new File(classLoader.getResource(patternFilename).getFile());
		var.initData(file.toString(), CODE_PATTERN, separator);

		file = new File(classLoader.getResource(patternTypeFilename).getFile());
		var.initData(file.toString(), CODE_PATTERN_TYPE, separator);

		file = new File(classLoader.getResource(webTypeFilename).getFile());
		var.initData(file.toString(), CODE_WEB_TYPE, separator);
	}

	private List<String> readFile(String filename) {
		BufferedReader in = null;
		List<String> result = new ArrayList<String>();
		try {
			in = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = in.readLine()) != null) {
				result.add(line);
			}
			return result;
		} catch (FileNotFoundException e) {
			//logger.error(e);
		} catch (IOException e) {
			//logger.error(e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					//logger.error(e);
				}
		}
		return null;
	}

	private void initData(String filename, int code, String separator) {
		List<String> rows = this.readFile(filename);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		System.out.println(filename);
		for (String s : rows) {
			System.out.println(s);
			String[] split = s.split(separator);
			switch (code) {
			case CODE_PATTERN:
				BoPattern.getInstance().createSearchPattern(null, split[1], split[2], Integer.parseInt(split[4]),
						split[3], 1);
				break;
			case CODE_USER:
				BoUser.getInstance().createUser(null, split[0], split[1]);
				break;
			case CODE_PATTERN_TYPE:
				PatternType pt = new PatternType();
				pt.setPatternTypeId(Integer.parseInt(split[0]));
				pt.setName(split[1]);
				pt.setRemarks(split[2]);
				Criteria criteria = session.createCriteria(PatternType.class);
				criteria.add(Restrictions.eq("name", pt.getName()));
				criteria.add(Restrictions.eq("remarks", pt.getRemarks()));
				List result = criteria.list();
				if (result.isEmpty())
					session.save(pt);
				break;
			case CODE_WEB_TYPE:
				WebType wt = new WebType();
				wt.setWebTypeId(Integer.parseInt(split[0]));
				wt.setName(split[1]);
				wt.setRemarks(split[2]);
				criteria = session.createCriteria(WebType.class);
				criteria.add(Restrictions.eq("name", wt.getName()));
				criteria.add(Restrictions.eq("remarks", wt.getRemarks()));
				result = criteria.list();
				if (result.isEmpty())
					session.save(wt);
				break;
			default:
				break;
			}
		}
		// Commit to save
		try {
			tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception ex) {

			}
		} finally {
			try {
				session.close();
			} catch (Exception e) {

			}
		}
	}
}
