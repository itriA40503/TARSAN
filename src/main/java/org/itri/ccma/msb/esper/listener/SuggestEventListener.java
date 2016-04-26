//package org.itri.ccma.msb.esper.listener;
//
//import org.apache.log4j.Logger;
//import org.itri.ccma.msb.esper.agent.UBikeEventProcessingAgent;
//
//import com.espertech.esper.client.EventBean;
//import com.espertech.esper.client.UpdateListener;
//
//public class SuggestEventListener implements UpdateListener {
//	
//	private Logger logger = Logger.getLogger(SuggestEventListener.class);
//	
//	public void update(EventBean[] newData, EventBean[] oldData) {
//		
//		String bid = (String) newData[0].get("bid");
//		Long pid = (Long) newData[0].get("pid");
//		Long suggestNum = (Long) newData[0].get("suggestNum");
//
//		logger.debug("##bid["+bid+"],pid["+pid+"],suggestNum["+suggestNum+"]");
//		
//		UBikeEventProcessingAgent.addSuggestMap(pid, suggestNum);
//	}
//}
