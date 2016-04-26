//package org.itri.ccma.msb.esper.agent;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.itri.ccma.msb.esper.event.BikeEvent;
//import org.itri.ccma.msb.esper.event.ParkingLotEvent;
//import org.itri.ccma.msb.esper.listener.EndBikeEventListener;
//import org.itri.ccma.msb.esper.listener.ParkingLotEventListener;
//import org.itri.ccma.msb.esper.listener.SuggestEventListener;
//
//import com.espertech.esper.client.Configuration;
//import com.espertech.esper.client.EPAdministrator;
//import com.espertech.esper.client.EPServiceProvider;
//import com.espertech.esper.client.EPServiceProviderManager;
//import com.espertech.esper.client.EPStatement;
//
//public class UBikeEventProcessingAgent
//{
//
//	private static Logger logger = Logger.getLogger(UBikeEventProcessingAgent.class);
//
//	private static final EPServiceProvider esperEngine;
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//    private static Map<Long, Long> suggestMap = new HashMap();
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//    public static Map<String, List> nearParkingLotMap = new HashMap();
//
//	static {
//		Configuration config = new Configuration();
//		config.addEventType("ParkingLotEvent", ParkingLotEvent.class);
//		config.addEventType("BikeEvent", BikeEvent.class);
//
//		esperEngine = EPServiceProviderManager.getDefaultProvider(config);
//
//		EPStatement statement = null;
//		String stmt = null;
//		EPAdministrator cepAdm = esperEngine.getEPAdministrator();
//
//		// listen to every ParkingLotEvent
//		stmt = "select id,avalableForRent from ParkingLotEvent";
//		statement = cepAdm.createEPL(stmt);
//		statement.addListener(new ParkingLotEventListener());
//
//		// reterive 3 parkingLot near the bike
//		stmt = "insert into suggestEvent select  bid,pid, 0L as suggestNum from BikeEvent as b, method:org.itri.ccma.ubike.common.ParkingLotLib.getNearParkLot(id, longitude, latitude) as p ";
//		EPStatement cepStatement = cepAdm.createEPL(stmt);
//		stmt = " select * from  suggestEvent  ";
//		cepStatement = cepAdm.createEPL(stmt);
//		cepStatement.addListener(new SuggestEventListener());
//
//		// calculate count of bikes near the parkingLot ( limit under 200m and
//		// the last 30 sec)
//		stmt = "  select bid, pid, count(*) as suggestNum  from suggestEvent as p unidirectional  ,BikeEvent.std:unique(id).win:time(30 sec) b where org.itri.ccma.ubike.common.ParkingLotLib.getDistance(p.pid,b.longitude,b.latitude )<200  ";
//		cepStatement = cepAdm.createEPL(stmt);
//		cepStatement.addListener(new SuggestEventListener());
//
//		// insert endBikeEvent to identify the end.
//		stmt = "insert into endBikeEvent select * from BikeEvent";
//		cepStatement = cepAdm.createEPL(stmt);
//		stmt = "select id as endBikeId from  endBikeEvent";
//		cepStatement = cepAdm.createEPL(stmt);
//		cepStatement.addListener(new EndBikeEventListener());
//
//	}
//
//	private UBikeEventProcessingAgent() {
//	}
//
//	public static void addSuggestMap(long pid, long suggestNum) {
//		suggestMap.put(pid, suggestNum);
//	}
//
//	public static Map<Long, Long> getSuggestMap() {
//		return suggestMap;
//	}
//
//	public static void sendEvent(Object event) {
//		esperEngine.getEPRuntime().sendEvent(event);
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//    public static List<Map> sendBikeEvent(String uid, double longitude, double latitude) {
//
//		logger.debug("##uid:" + uid);
//		nearParkingLotMap.remove(nearParkingLotMap.get(uid));
//
//		BikeEvent bikeEvent = new BikeEvent(uid, longitude, latitude);
//		sendEvent(bikeEvent);
//
//		List resultList = null;
//
//		// try 10 times to prevent from endless loop
//		int count = 0;
//		do {
//			count++;
//
//			resultList = nearParkingLotMap.get(uid);
//
//		} while (resultList == null && count < 10);
//
//		logger.debug("##parkingLotInfo:" + resultList);
//		return resultList;
//	}
//
//	public static void sendParkingLotEvent(long id, int avalableForRent, int totalSpace) {
//		ParkingLotEvent parkingLotEvent = new ParkingLotEvent();
//		parkingLotEvent.setId(id);
//		parkingLotEvent.setAvalableForRent(avalableForRent);
//		parkingLotEvent.setTotalSpace(totalSpace);
//		sendEvent(parkingLotEvent);
//	}
//
//	public static void main(String[] args) {
//
//		for (int i = 0; i < 10000; i++) {
//
//			UBikeEventProcessingAgent.sendBikeEvent("1", 121.041012, 24.777344);
//			UBikeEventProcessingAgent.sendBikeEvent("2", 121.044166, 24.777208);
//			UBikeEventProcessingAgent.sendBikeEvent("3", 121.042149, 24.774968);
//
//		}
//		// UBikeEventProcessingAgent.sendParkingLotEvent(1,5, 20);
//
//		// // A.
//		// bikeLocation[0] = new double[] {24.777344,121.041012};
//		//
//		// // B.
//		// bikeLocation[1] = new double[] {24.777208,121.044166};
//		//
//		// // C.
//		// bikeLocation[2] = new double[] {24.774968,121.042149};
//		//
//		// // D.
//		// bikeLocation[3] = new double[] { 24.776526, 121.04511 };
//		//
//		// // E.
//		// bikeLocation[4] = new double[] { 24.774812, 121.043372 };
//		//
//		// // F.
//		// bikeLocation[5] = new double[] { 24.773311, 121.045346 };
//		//
//		// // G.
//		// bikeLocation[6] = new double[] { 24.77411, 121.047707 };
//		//
//		// // H.
//		// bikeLocation[7] = new double[] { 24.771714, 121.047535 };
//
//	}
//}
