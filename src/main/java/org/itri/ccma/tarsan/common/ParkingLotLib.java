package org.itri.ccma.tarsan.common;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.apache.log4j.Logger;
//import org.hibernate.Criteria;
//import org.hibernate.HibernateException;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.hibernate.criterion.Order;
//import org.itri.ccma.tarsan.util.HibernateUtil;
//import org.itri.ccma.ubike.hibernate.ParkingLot;
//import org.json.JSONArray;
//
///**
// * The Class ParkingLotLib.
// */
//@SuppressWarnings("unchecked")
//public class ParkingLotLib
//{
//
//	/** The logger. */
//	private static Logger logger = Logger.getLogger(ParkingLotLib.class);
//
//	/** The parking lot list. */
//	@SuppressWarnings("rawtypes")
//	static List<ParkingLot> parkingLotList = new LinkedList();
//
//	static {
//		parkingLotList = genParkingLotList();
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static List genParkingLotList() {
//		List<ParkingLot> pList = new LinkedList();
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = null;
//		try {
//			tx = session.beginTransaction();
//			Criteria criteria = session.createCriteria(ParkingLot.class);
//			criteria.addOrder(Order.asc("parkingLotName"));
//			List result = criteria.list();
//
//			for (Iterator itr = result.iterator(); itr.hasNext();) {
//				ParkingLot eachPL = (ParkingLot) itr.next();
//				pList.add(eachPL);
//			}
//		} catch (Exception e) {
//			if (tx != null && tx.isActive()) {
//				try {
//					logger.error("[ERROR] message: " + e.getMessage(), e);
//					tx.rollback();
//				} catch (HibernateException e1) {
//					logger.error("Error Rolling Back Transaction", e1);
//				} finally {
//					HibernateUtil.getSessionFactory().getCurrentSession().close();
//				}
//			}
//		}
//		HibernateUtil.getSessionFactory().getCurrentSession().close();
//		return pList;
//	}
//
//	public static List<ParkingLot> getParkingLotList() {
//		return parkingLotList;
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static String getParkingLotListJSON() {
//
//		List pList = new ArrayList();
//		for (ParkingLot parkingLot : parkingLotList) {
//			LinkedHashMap parkingLotMap = new LinkedHashMap();
//			parkingLotMap.put("parkLotId", (int) parkingLot.getParkingLotId());
//			parkingLotMap.put("parkLotName", parkingLot.getParkingLotName());
//			// parkingLotMap.put("parkLotLng", parkingLot.getLongitude());
//			// parkingLotMap.put("parkLotLat", parkingLot.getLatitude());
//			// parkingLotMap.put("bikeLeftNum",
//			// parkingLot.getAvalableForRent());
//			// parkingLotMap.put("parkSpaceTotalNum",
//			// parkingLot.getTotalSpace());
//
//			parkingLotMap.put("parkSpaceLeft", parkingLot.getTotalSpace() - parkingLot.getAvalableForRent());
//
//			pList.add(parkingLotMap);
//		}
//
//		JSONArray arrayList = new JSONArray(pList);
//		String jsonStr = arrayList.toString();
//		logger.debug("##jsonStr:" + jsonStr);
//		return jsonStr;
//	}
//
//	/**
//	 * Gets the closest park lot.
//	 * 
//	 * @param latitude
//	 *            the latitude
//	 * @param longitude
//	 *            the longitude
//	 * @return the closest park lot
//	 */
//	public static ParkingLot getClosestParkLot(double latitude, double longitude) {
//
//		double distance = 0f;
//		ParkingLot closetParkLot = null;
//
//		if (parkingLotList.size() == 0) {
//			logger.error("no parkingLot in db");
//			return null;
//		}
//
//		for (int i = 0; i < parkingLotList.size(); i++) {
//			ParkingLot parkLot = parkingLotList.get(i);
//			double tempA = Math.pow(Math.abs(longitude - parkLot.getLongitude()), 2)
//			        + Math.pow(Math.abs(latitude - parkLot.getLatitude()), 2);
//			if (distance == 0f) {
//				distance = tempA;
//				closetParkLot = parkLot;
//			} else {
//				if (tempA < distance) {
//					distance = tempA;
//					closetParkLot = parkLot;
//				}
//			}
//		}
//
//		return closetParkLot;
//	}
//
//	public static void updateAvalableForRent(long id, int avalableForRent) {
//
//		logger.info("##ParkingLotId:" + id);
//
//		getParkingLotById(id).setAvalableForRent(avalableForRent);
//	}
//
//	public static ParkingLot getParkingLotById(long id) {
//		logger.debug("##parkingLotList size:" + parkingLotList.size());
//		for (ParkingLot parkingLot : parkingLotList) {
//			logger.debug("##parkingLot.getParkingLotId():" + parkingLot.getParkingLotId());
//			if (parkingLot.getParkingLotId() == id) {
//				return parkingLot;
//			}
//		}
//		return null;
//	}
//
//	public static ParkingLot getParkingLotByName(String parkingLotName) {
//		for (ParkingLot parkingLot : parkingLotList) {
//			if (parkingLotName.equals(parkingLot.getParkingLotName())) {
//				return parkingLot;
//			}
//		}
//
//		logger.info("parkingLotName(" + parkingLotName + ") doesn't exist in db");
//		ParkingLot parkingLot = new ParkingLot();
//		parkingLot.setParkingLotName(parkingLotName);
//		return parkingLot;
//
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static Map<String, Class> getNearParkLotMetadata() {
//		Map<String, Class> propertyNames = new HashMap<String, Class>();
//		propertyNames.put("pid", Long.class);
//		propertyNames.put("bid", String.class);
//		return propertyNames;
//	}
//
//	/**
//	 * retrieve the parkinglot list by distance
//	 * 
//	 * @param bike
//	 * @param parkLotList
//	 * @return
//	 */
//	@SuppressWarnings({ "rawtypes", "unused" })
//	public static Map<String, Object>[] getNearParkLot(String bid, double longitude, double latitude) {
//
//		logger.debug("##longitude:" + longitude + ",latitude:" + latitude);
//
//		Map orderMap = new TreeMap();
//
//		double distance = 0f;
//		ParkingLot closetParkLot = null;
//		for (int i = 0; i < parkingLotList.size(); i++) {
//			ParkingLot parkingLot = parkingLotList.get(i);
//			double tempA = Math.pow(Math.abs(longitude - parkingLot.getLongitude()), 2)
//			        + Math.pow(Math.abs(latitude - parkingLot.getLatitude()), 2);
//
//			logger.debug("##parkinglot[" + parkingLot.getParkingLotName() + "],distance:" + tempA);
//
//			orderMap.put(tempA, parkingLot.getParkingLotId());
//		}
//		Iterator ite = orderMap.values().iterator();
//
//		Map[] mapArray = new Map[3];
//		for (int i = 0; i < mapArray.length; i++) {
//			mapArray[i] = new HashMap();
//			mapArray[i].put("bid", bid);
//			mapArray[i].put("pid", ite.next());
//
//			logger.info("##mapArray:" + mapArray[i]);
//		}
//
//		return mapArray;
//	}
//
//	public static int getDistance(long pid, Double logtitude, Double latitude) {
//
//		ParkingLot parkingLot = getParkingLotById(pid);
//
//		int distance = (int) getDistance(parkingLot.getLatitude(), parkingLot.getLongitude(), latitude, logtitude);
//
//		return distance;
//	}
//
//	private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
//		double theta = lon1 - lon2;
//		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
//		        * Math.cos(deg2rad(theta));
//		dist = Math.acos(dist);
//		dist = rad2deg(dist);
//		dist = dist * 60 * 1.1515;
//		dist = dist * 1.609344 * 1000;
//		return Math.round(dist);
//	}
//
//	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
//	/* :: This function converts decimal degrees to radians : */
//	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
//	private static double deg2rad(double deg) {
//		return (deg * Math.PI / 180.0);
//	}
//
//	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
//	/* :: This function converts radians to decimal degrees : */
//	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
//	private static double rad2deg(double rad) {
//		return (rad * 180.0 / Math.PI);
//	}
//
//	/**
//	 * The main method.
//	 * 
//	 * @param args
//	 *            the arguments
//	 */
//	public static void main(String[] args) {
//
//		// ParkingLot parkingLot = ParkingLotLib.getClosestParkLot(24.77340,
//		// 121.04350);
//		// logger.debug("##ParkingLotName:" + parkingLot.getParkingLotName() +
//		// ", sosNum:" + parkingLot.getTelNumber());
//		//
//		// parkingLot = ParkingLotLib.getClosestParkLot(24.77440, 121.04400);
//		// logger.debug("##ParkingLotName:" + parkingLot.getParkingLotName() +
//		// ", sosNum:" + parkingLot.getTelNumber());
//		//
//		// parkingLot = ParkingLotLib.getClosestParkLot(24.771714, 121.047535);
//		// logger.debug("##ParkingLotName:" + parkingLot.getParkingLotName() +
//		// ", sosNum:" + parkingLot.getTelNumber());
//		//
//		// parkingLot = ParkingLotLib.getClosestParkLot(24.7737291,
//		// 121.0442163);
//		// logger.debug("##ParkingLotName:" + parkingLot.getParkingLotName() +
//		// ", sosNum:" + parkingLot.getTelNumber());
//
//	}
//
//}
