package com.dita.xd.util.server;
public class MessageProtocol {
	// 서버한테 ID, CHAT(1대1대화),
	// CHATALL, CHATLIST(챗 참여자), MESSAGE(쪽지보내기)

	// 클라이언트->서버로 보낼때 ID:aaa(값)를 보내게 됨
	// 서버-> 클라이언트로 보낼때 CHATLIST : aaa;bbb;ccc;(접속자리스트)
	public static final String ID = "ID";

	// 클라이언트->서버로 보낼때 CHAT:받는아이디;메세지를 보내게 됨
	// ex) CHAT:bbb;밥먹자
	// 서버-> 클라이언트 CHAT:보내는아이디;메세지
	// ex) CHAT:aaa;밥먹자
	public static final String CHAT = "CHAT";

	//(C->S)CHATALL: 메시지
	//(S->C)CHATALL:[보내는아이디]메세지
	public static final String CHAT_ALL = "CHATALL";

	//(S->C)CHATLIST:aaa;bbb;ccc
	public static final String CHAT_LIST = "CHATLIST";

	// 클라이언트->서버로 보낼때 CHAT:받는아이디;메세지를 보내게 됨
	// ex) CHAT:bbb;밥먹자
	// 서버-> 클라이언트 CHAT:보내는아이디;메세지
	// ex) CHAT:aaa;밥먹자
	public static final String MESSAGE = "MESSAGE";

	//(C->S) MSGLIST:id
	//(S->C) MSGLIST:fid,tid,msg;fid,tid,msg;...
	//(S->C) MSGLIST:aaa,bbb,밥먹자;bbb,ccc,하이...
	public  static final String MSG_LIST = "MSGLIST";

	public static final String SEPARATOR = ":";

}
