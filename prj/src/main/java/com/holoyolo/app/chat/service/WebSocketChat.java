package com.holoyolo.app.chat.service;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service 
@ServerEndpoint(value="/chat")
public class WebSocketChat {
	private static Map<Integer, Set<Session>> clientsMap = Collections.synchronizedMap(new HashMap<Integer, Set<Session>>());

	@Autowired
	ChatService chatService;
	 
	//메시지왔을 때
	@OnMessage
	public void onMessage(String msg, Session session) throws Exception{

		
	       System.out.println("receive message : " + msg);

	       //uri에서 clubid가져옴
			URI uri = session.getRequestURI();
		    int clubId = extractClubIdFromURI(uri);
		    

		    //그 클럽아이디에 해당하는 세션들 가져와서 메시지 전송
	        Set<Session> sessions = clientsMap.get(clubId);
	        if (sessions != null) {
	            for (Session s : sessions) {
	                System.out.println("send data : " + msg);
	                
	                s.getBasicRemote().sendText(msg);
	            }
	        }
		
	}
	
	//처음 접속
	@OnOpen
	public void onOpen(Session s, EndpointConfig config) throws Exception {
		System.out.println(s.getUserPrincipal().getName());

		URI uri = s.getRequestURI();
	    int clubId = extractClubIdFromURI(uri);
		System.out.println(s.getRequestURI());
		System.out.println(clubId);


		if (!clientsMap.containsKey(clubId)) {
		    clientsMap.put(clubId, Collections.synchronizedSet(new HashSet<Session>()));
		}

		Set<Session> sessions = clientsMap.get(clubId);

		//해당 방에 있는 회원들에게 입장알림 전송
		if (!sessions.contains(s)) {
		    sessions.add(s);
		    if (sessions != null) {
	            for (Session se : sessions) {
	            	String msg = "{ \"msg\" : "+"\""+s.getUserPrincipal().getName()+"님이 입장하였습니다.\", \"type\" : \"enter\"}";
	                System.out.println("send data : " + msg);
	                se.getBasicRemote().sendText(msg);
	            }
	        }
		} else {
		    System.out.println("이미 연결된 session 임!!!");
		}
	}
	
	//연결끊김
	@OnClose
	public void onClose(Session s) throws Exception {
			URI uri = s.getRequestURI();
		    int clubId = extractClubIdFromURI(uri);
		    
		    Set<Session> sessions = clientsMap.get(clubId);

		    //해당 방 회원들에게 퇴장알림
		    if (sessions.contains(s)) {
			    sessions.remove(s);
			    if (sessions != null) {
		            for (Session se : sessions) {
		            	String msg = "{ \"msg\" : "+"\""+s.getUserPrincipal().getName()+"님이 퇴장하였습니다.\", \"type\" : \"enter\"}";
		                System.out.println("send data : " + msg);
		                se.getBasicRemote().sendText(msg);
		            }
		        }
			} 
		
	}
	
	private int extractClubIdFromURI(URI uri) {
	    String query = uri.getQuery();
	    String[] params = query.split("&");
	    for (String param : params) {
	        String[] keyValue = param.split("=");
	        if (keyValue.length == 2 && "clubId".equals(keyValue[0])) {
	            return Integer.parseInt(keyValue[1]);
	        }
	    }
	    throw new IllegalArgumentException("clubId not found in URI");
	}
}
