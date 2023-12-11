package com.holoyolo.app.chat.service.trade;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;

@Service 
@ServerEndpoint(value="/tradeChat")
public class WebSocketTrade {
	private static Map<Object, Set<Session>> clientsMap = Collections.synchronizedMap(new HashMap<Object, Set<Session>>());
	
	
	//처음 접속
		@OnOpen
		public void onOpen(Session s, EndpointConfig config) throws Exception {
			System.out.println("소켓");
			System.out.println(s.getUserPrincipal().getName());

			URI uri = s.getRequestURI();
			System.out.println("uri" +uri);
		    int roomId = extractTradeIdFromURI(uri);
			System.out.println(s.getRequestURI());
			System.out.println(roomId+"거래 채팅 입장");


			if (!clientsMap.containsKey(roomId)) {
			    clientsMap.put(roomId, Collections.synchronizedSet(new HashSet<Session>()));
			}

			Set<Session> sessions = clientsMap.get(roomId);
			
			//해당 방에 있는 회원들에게 입장알림 전송
			if (!sessions.contains(s)) {
			    sessions.add(s);
			} else {
			    System.out.println("이미 연결된 session 임!!!");
			}
		}
		
		//메시지왔을 때
		@OnMessage
		public void onMessage(String msg, Session session) throws Exception{

			
		       System.out.println("receive message : " + msg);

		       //uri에서 clubid가져옴
				URI uri = session.getRequestURI();
			    int roomId = extractTradeIdFromURI(uri);
			    

			    //그 클럽아이디에 해당하는 세션들 가져와서 메시지 전송
		        Set<Session> sessions = clientsMap.get(roomId);


		        if (sessions != null) {
		            for (Session s : sessions) {
		                System.out.println("send data : " + msg);
		                s.getBasicRemote().sendText(msg);
		            }
		        }
			
		}
		
		//연결끊김
		@OnClose
		public void onClose(Session s) throws Exception {
				URI uri = s.getRequestURI();
			    int roomId = extractTradeIdFromURI(uri);
			    
			    Set<Session> sessions = clientsMap.get(roomId);
				
			    //해당 방 회원들에게 퇴장알림
			    if (sessions.contains(s)) {
				    sessions.remove(s);
				} 
			
		}
		
		private int extractTradeIdFromURI(URI uri) {
		    String query = uri.getQuery();
		    System.out.println(query);
		    String[] params = query.split("&");
		    for (String param : params) {
		        String[] keyValue = param.split("=");
		        if (keyValue.length == 2 && "tradeId".equals(keyValue[0])) {
		            return Integer.parseInt(keyValue[1])+1000;
		        }
		    }
		    throw new IllegalArgumentException("clubId not found in URI");
		}
		
		
}
