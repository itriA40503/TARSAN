//package org.itri.ccma.msb.esper.listener;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.itri.ccma.msb.common.ParkingLotLib;
//import org.itri.ccma.msb.esper.agent.UBikeEventProcessingAgent;
//import org.itri.ccma.ubike.hibernate.ParkingLot;
//
//import com.espertech.esper.client.EventBean;
//import com.espertech.esper.client.UpdateListener;
//
//public class EndBikeEventListener implements UpdateListener
//{
//
//	private Logger log = Logger.getLogger(EndBikeEventListener.class);
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//    public void update(EventBean[] newData, EventBean[] oldData) {
//		log.info("##EndBikeListener: " + newData[0].getUnderlying());
//		log.info("send c2dm message" + (String) newData[0].get("endBikeId"));
//
//		Map<Long, Long> cacheMap = UBikeEventProcessingAgent.getSuggestMap();
//		log.info("cacheMap:" + cacheMap);
//		Iterator keyIte = cacheMap.keySet().iterator();
//
//		String firstStr = new String();
//		String secondStr = new String();
//		String lastStr = new String();
//		String fullStr = new String();
//
//		List resultList = new LinkedList();
//		while (keyIte.hasNext()) {
//
//			Long pid = (Long) keyIte.next();
//			Long suggestNum = cacheMap.get(pid);
//			ParkingLot parkingLot = ParkingLotLib.getParkingLotById(pid);
//
//			int leftNum = parkingLot.getTotalSpace() - parkingLot.getAvalableForRent();
//
//			Map pMap = new HashMap();
//			pMap.put("partLotId", pid);
//			pMap.put("parkLotName", parkingLot.getParkingLotName());
//			pMap.put("parkSpaceNum", leftNum);
//			resultList.add(pMap);
//
//			String memo = "(LeftSpace:" + leftNum + ",bikes count less then 200m :" + suggestNum + ")";
//
//			if (leftNum == 0) {
//				fullStr += parkingLot.getParkingLotName() + " full " + memo + "\n";
//			} else if (leftNum > suggestNum) {
//				firstStr += "suggest parking no:" + parkingLot.getParkingLotName() + memo + "\n";
//			} else if (leftNum == suggestNum) {
//				secondStr += parkingLot.getParkingLotName() + " is going to be full " + memo + "\n";
//			} else {
//				lastStr += parkingLot.getParkingLotName() + " is going to be full " + memo + "\n";
//			}
//		}
//
//		String sendMessage = firstStr + secondStr + lastStr + fullStr;
//		log.info("SendMessage:\n" + sendMessage);
//
//		cacheMap.clear();
//
//		UBikeEventProcessingAgent.nearParkingLotMap.put((String) newData[0].get("endBikeId"), resultList);
//		log.info("update end uid:" + (String) newData[0].get("endBikeId"));
//
//	}
//
//}
