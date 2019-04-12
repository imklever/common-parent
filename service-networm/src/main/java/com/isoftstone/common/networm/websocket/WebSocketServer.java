package com.isoftstone.common.networm.websocket;

import com.isoftstone.common.networm.db.CommonDB;
import com.isoftstone.common.networm.service.OnLineRobot.RobotService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


@ServerEndpoint("/ws/asset/{param}")
@Component
public class WebSocketServer {

	private static RobotService robotService;
	@Autowired
	public void setChatService(RobotService robotService) {
		WebSocketServer.robotService = robotService;
	}

	private static CommonDB commonDB;
	@Autowired
	public void setDBService(CommonDB commonDB) {
		WebSocketServer.commonDB = commonDB;
	}

	private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

	private static final AtomicInteger OnlineCount = new AtomicInteger(0);
	// concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
	private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("param") String param) {
		SessionSet.add(session);
		int cnt = OnlineCount.incrementAndGet(); // 在线数加1
		log.info("有连接加入，当前连接数为：{}", cnt);
		//SendMessage(session, "连接成功");
//		if(ZsyWarningPromptPush.dataList != null)
//			SendMessage(session, ZsyWarningPromptPush.dataList);
//		else
//		robotService.insertSession(session.getId(),ip);
		param = "{"+param+"}";
		Map m = (Map)JSONObject.fromObject(param);
		String ip = m.get("ip").toString();
		String type = m.get("wtype").toString();
		String robotType = m.get("robotType").toString();
		Map<String,Object> map = new HashMap<>();
		map.put("sid",session.getId());
		map.put("uip",ip);
		map.put("type",type);
		map.put("robotType",robotType);
		if("0".equals(type)) {
			commonDB.insertMethod(map,"S12050");
			List<Map<String,Object>> robotList = commonDB.selectMethod("S12072",map);
			map.put("msg", robotList.size() == 1 ? robotList.get(0).get("robotMsg").toString() : "");
			map.put("type", 0);
			SendMessage(session, JSONObject.fromObject(map).toString());
		}else{
			String usid = m.get("usid").toString();
			map.put("state",1);
			map.put("id",usid);
			List<Map<String,Object>> list = commonDB.selectMethod("S12056",map);
			if(list.size() != 1){
				map.put("usid",usid);
				commonDB.insertMethod(map,"S12050");
				map.put("id",usid);
				map.put("state",2);
				commonDB.updateMethond(map,"S12055");
				map.put("msg", "您已连接到"+usid+"用户上,ip:"+ ip);
				map.put("type", 2);
				SendMessage(session, JSONObject.fromObject(map).toString());
			}else{
				map.put("msg", "您无法连接到"+usid+"用户,该用户应该已经离开。");
				map.put("type", 2);
				SendMessage(session, JSONObject.fromObject(map).toString());
			}
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session,@PathParam("param") String param) {
		SessionSet.remove(session);
		int cnt = OnlineCount.decrementAndGet();
		Map<String,Object> paraMap = new HashMap<>();
		param = "{"+param+"}";
		Map m = (Map)JSONObject.fromObject(param);
		String ip = m.get("ip").toString();
		String type = m.get("wtype").toString();
		paraMap.put("sid",session.getId());
		paraMap.put("uip",ip);
		paraMap.put("type",type);
		commonDB.updateMethond(paraMap,"S12051");
		if(Integer.parseInt(type) == 1){
			String id = m.get("usid").toString();
			paraMap.put("id",id);
			paraMap.put("state",2);
			List<Map<String,Object>> list = commonDB.selectMethod("S12056",paraMap);
			if(list.size() == 1){
				paraMap.put("state",0);
				commonDB.updateMethond(paraMap,"S12055");
			}
		}
		log.info("有连接关闭，当前连接数为：{}", cnt);
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session,@PathParam("param") String param) {
		log.info("来自客户端的消息：{}",message);
//	        SendMessage(session, "收到消息，消息内容："+message);
		try{
			String sessionId = session.getId();
			Map m = (Map)JSONObject.fromObject(message);
			int type = Integer.parseInt(m.get("type").toString());
			Map<String,Object> map = new HashMap<>();
			String msg = m.get("msg").toString();
			param = "{"+param+"}";
			Map mm = (Map)JSONObject.fromObject(param);
			String ip = mm.get("ip").toString();
			String wType = mm.get("wtype").toString();
			String answerType = mm.get("answerType").toString();
			String robotType = mm.get("robotType").toString();
			switch (type){
				case 0:
					Map<String,Object> paraMap = new HashMap<>();
					paraMap.put("sid",sessionId);
					paraMap.put("uip",ip);
					paraMap.put("type",wType);
					map.put("type",type);
					int id = 0;
					List<Map<String,Object>> idList = commonDB.selectMethod("S12058",paraMap);
					if(idList.size() == 1){
						id = Integer.parseInt(idList.get(0).get("id").toString());
					}
					Map<String,Object> sesMap = new HashMap<>();
					if("0".equals(wType)){
						sesMap.put("session_id",id);
						sesMap.put("msg_info",msg);
						sesMap.put("msg_type",0);
						sesMap.put("answer_id",0);
						commonDB.insertMethod(sesMap,"I12057");
						map = robotService.getAnswers(msg,answerType,robotType);
						map.put("type",type);
						List<Map<String,Object>> userList = commonDB.selectMethod("S12054",paraMap);
						if(userList.size() == 1 && Integer.parseInt(userList.get(0).get("status").toString()) == 2){
							String abc = map.get("msg").toString();
							if(!"".equals(abc)){
								SendMessage(session, JSONObject.fromObject(map).toString());
								sesMap.put("session_id",id);
								sesMap.put("msg_info",map.get("msg"));
								sesMap.put("msg_type",1);
								sesMap.put("answer_id",Integer.parseInt(map.get("answer_id").toString()));
								commonDB.insertMethod(sesMap,"I12057");
							}
							List<Map<String,Object>> list = commonDB.selectMethod("S12052",paraMap);
							String rengongSid = "";
							if(list.size() == 1){
								rengongSid = list.get(0).get("session_id").toString();
							}
							map.put("msg",msg);
							SendMessage(rengongSid,JSONObject.fromObject(map).toString());
							map.put("msg",abc);
							map.put("type",3);
							SendMessage(rengongSid, JSONObject.fromObject(map).toString());
						}else{
							SendMessage(session, JSONObject.fromObject(map).toString());
							sesMap.put("session_id",id);
							sesMap.put("msg_info","".equals(map.get("msg")) ? "不好意思，我不是很理解您的问题，请您换一种方式描述您的问题，谢谢。" : map.get("msg"));
							sesMap.put("msg_type",1);
							sesMap.put("answer_id",Integer.parseInt(map.get("answer_id").toString()));
							commonDB.insertMethod(sesMap,"I12057");
						}
					}else if("1".equals(wType)){
						List<Map<String,Object>> list = commonDB.selectMethod("S12053",paraMap);
						if(list.size() == 1){
							String userSid = list.get(0).get("session_id").toString();
							id = Integer.parseInt(list.get(0).get("id").toString());
							map.put("msg",msg);
							SendMessage(userSid,JSONObject.fromObject(map).toString());
							sesMap.put("session_id",id);
							sesMap.put("msg_info",msg);
							sesMap.put("msg_type",2);
							sesMap.put("answer_id",0);
							commonDB.insertMethod(sesMap,"I12057");
						}
					}
					break;
				case 1:
					List<String> tips = robotService.getTipsList(msg,answerType,robotType);
					map.put("type",type);
					if(tips.size() == 0)
						map.put("tipsList","[]");
					else
						map.put("tipsList",tips);
					SendMessage(session, JSONObject.fromObject(map).toString());
					break;
			}
//			SendMessage(session,message);
		} catch (Exception e) {
			log.error("发送消息出错：{}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 出现错误
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error,@PathParam("param") String param) {
		log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
		param = "{"+param+"}";
		Map m = (Map)JSONObject.fromObject(param);
		String ip = m.get("ip").toString();
		String type = m.get("wtype").toString();
		Map<String,Object> paraMap = new HashMap<>();
		paraMap.put("sid",session.getId());
		paraMap.put("uip",ip);
		paraMap.put("type",type);
		commonDB.updateMethond(paraMap,"S12051");
		error.printStackTrace();
	}

	/**
	 * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
	 * @param session
	 * @param message
	 */
	public static void SendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(String.format("%s",message));
		} catch (IOException e) {
			log.error("发送消息出错：{}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 群发消息
	 * @param message
	 * @throws IOException
	 */
	public static void BroadCastInfo(String message) throws IOException {
		for (Session session : SessionSet) {
			if(session.isOpen()){
				SendMessage(session, message);
			}
		}
	}

	/**
	 * 指定Session发送消息
	 * @param sessionId
	 * @param message
	 * @throws IOException
	 */
	public static void SendMessage(String sessionId,String message) throws IOException {
		Session session = null;
		for (Session s : SessionSet) {
			if(s.getId().equals(sessionId)){
				session = s;
				break;
			}
		}
		if(session!=null){
			SendMessage(session, message);
		}
		else{
			log.warn("没有找到你指定ID的会话：{}",sessionId);
		}
	}

}
