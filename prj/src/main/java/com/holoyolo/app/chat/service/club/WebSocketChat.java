package com.holoyolo.app.chat.service.club;


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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holoyolo.app.config.ServerEndpointConfigurator;
import com.holoyolo.app.member.service.MemberService;
import com.holoyolo.app.member.service.MemberVO;


@Service 
@ServerEndpoint(value="/chat", configurator = ServerEndpointConfigurator.class)
public class WebSocketChat {
	private static Map<Object, Set<Session>> clientsMap = Collections.synchronizedMap(new HashMap<Object, Set<Session>>());

	@Autowired
	ChatService chatService;
	
	@Autowired
	MemberService memberService;
	 
	//회원으로부터 메시지가 도착했을 때 처리하는 메소드
	@OnMessage
	public void onMessage(String msg, Session session) throws Exception{

	       //회원 세션의 URI에서 ClubId를 추출
			URI uri = session.getRequestURI();
		    int clubId = extractClubIdFromURI(uri);
		    

		    //해당 ClubId를 가진 회원들의 세션 집합을 가져옴
	        Set<Session> sessions = clientsMap.get(clubId);
	        
	        //현재 접속중인 회원 수를 계산해서 문자열 형태로 변환
			int size = sessions.size();
			String sizestr = "{ \"size\":"+" \""+size+"\", \"type\":\"size\"}";
			
	        if (sessions != null) {
	        	//해당 알뜰모임의 채팅방에 접속 중인 모든 회원들에게 메시지 전송
	        	//각 회원들에게 현재 접속 인원수와 들어온 메시지 보냄
	            for (Session s : sessions) {
	                s.getBasicRemote().sendText(sizestr); //접속 인원수
	                s.getBasicRemote().sendText(msg); //수신 메시지
	            }
	        }
		
	}
	
	//처음 접속
	@OnOpen
	public void onOpen(Session s, EndpointConfig config) throws Exception {
		System.out.println("소켓");
		System.out.println(s.getUserPrincipal().getName());
		MemberVO info = memberService.selectUser(s.getUserPrincipal().getName());

		URI uri = s.getRequestURI();
		System.out.println("uri" +uri);
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
		    int size = sessions.size();
			String sizestr = "{ \"size\":"+" \""+size+"\", \"type\":\"size\"}";
		    if (sessions != null) {
	            for (Session se : sessions) {
	            	String msg = "{ \"msg\" : "+"\""+info.getNickname()+"님이 입장하였습니다.\", \"type\" : \"enter\"}";
	                System.out.println("send data : " + msg);
	                se.getBasicRemote().sendText(sizestr);
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
		    MemberVO info = memberService.selectUser(s.getUserPrincipal().getName());
		    
		    Set<Session> sessions = clientsMap.get(clubId);
			
		    //해당 방 회원들에게 퇴장알림
		    if (sessions.contains(s)) {
			    sessions.remove(s);
			    int size = sessions.size();
				String sizestr = "{ \"size\":"+" \""+size+"\", \"type\":\"size\"}";
			    if (sessions != null) {
		            for (Session se : sessions) {
		            	String msg = "{ \"msg\" : "+"\""+info.getNickname()+"님이 퇴장하였습니다.\", \"type\" : \"enter\"}";
		                System.out.println("send data : " + msg);
		                se.getBasicRemote().sendText(sizestr);
		                se.getBasicRemote().sendText(msg);
		            }
		        }
			} 
		
	}
	
	private int extractClubIdFromURI(URI uri) {
	    String query = uri.getQuery();
	    System.out.println(query);
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
