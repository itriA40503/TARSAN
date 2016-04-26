//package org.itri.ccma.msb.esper.listener;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Restrictions;
//import org.itri.ccma.msb.common.ParkingLotLib;
//import org.itri.ccma.msb.util.AuthenticationUtil;
//import org.itri.ccma.msb.util.HibernateUtil;
//import org.itri.ccma.msb.util.MessageUtil;
//import org.itri.ccma.ubike.hibernate.Users;
//
//import com.espertech.esper.client.EventBean;
//import com.espertech.esper.client.UpdateListener;
//
//public class ParkingLotEventListener implements UpdateListener
//{
//
//	private Logger logger = Logger.getLogger(ParkingLotEventListener.class);
//
//	public void update(EventBean[] newData, EventBean[] oldData) {
//
//		long id = (Long) newData[0].get("id");
//		int avalableForRent = (Integer) newData[0].get("avalableForRent");
//
//		// send 12 parkingLot info by C2DM
//		ParkingLotLib.updateAvalableForRent(id, avalableForRent);
//
//		// reterives all users in users table having registerId
//		sendMessageToUsers(ParkingLotLib.getParkingLotListJSON());
//
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//    private void sendMessageToUsers(String jsonStr) {
//
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		try {
//			Criteria criteria = session.createCriteria(Users.class);
//			criteria.add(Restrictions.and(Restrictions.ne("registerId", ""), Restrictions.isNotNull("registerId")));
//
//			List result = criteria.list();
//			logger.info("##size:" + result.size());
//			Map dataMap = new HashMap();
//			dataMap.put("payload", jsonStr);
//			String authToken = AuthenticationUtil.getToken();
//
//			for (Iterator itr = result.iterator(); itr.hasNext();) {
//				Users user = (Users) itr.next();
//				String registerId = user.getRegisterId();
//				MessageUtil.sendMessage(authToken, registerId, dataMap);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			HibernateUtil.getSessionFactory().getCurrentSession().close();
//		}
//	}
//}
