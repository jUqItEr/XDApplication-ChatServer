package com.dita.xd.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.dita.xd.model.ChatMessageBean;
import com.dita.xd.controller.ChatMessageMgr;
import com.dita.xd.util.ChatProtocol3;
public class ChatServer {

	Vector<Client> vc;
	ServerSocket server;
	int port = 8003;                                                        // 포트번호 설정
	ChatMessageMgr mgr;

	public ChatServer() {

		try {
			vc = new Vector<Client>();                            // Client3 의 형식을 담을 수 있는 벡터 vc 생성
			server = new ServerSocket(port);                     // socket 서버(포트 : 8003) 를 만듦
			mgr = new ChatMessageMgr();                                     // 챗 관리자도 생성
		} catch (Exception e) {                                         // 3 중 하나라도 문제 생기면 에러 뜨고 종료
			System.err.println("Error in Server");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("****************************************");
		System.out.println("*Welcome Chat Server 4.0...");
		System.out.println("*클라이언트 접속을 기다리고 있습니다.");
		System.out.println("****************************************");
		try {
			while (true) {
				Socket sock = server.accept();                     // 서버에서 sock(클라이언트)을 접근대기
				Client ct = new Client(sock);                   // 새로운 클라이언트를 만든다
				ct.start();                                                     // 클라이언트(user) 시작
				vc.addElement(ct);                                      // vc에 유저를 더해준다.
			}
		} catch (Exception e) {
			System.err.println("Error in Socket");
			e.printStackTrace();
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////

	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) { // 서버에 접속한 유저들 모두한테
			Client ct = vc.elementAt(i);
			ct.sendMessage(msg);               // 메세지를 전달한다.
		}
	}

	public void removeClient(Client ct) {
		vc.remove(ct);                                 //  유저 삭제(접속한 유저를 담은 벡터에서 유저 삭제)
	}

/////////////////////////////////////////////////////////////////////////////////////////////////

	// 접속된 모든 id 리스트 리턴 ex) aaa;bbb;ccc;ddd;홍길동;
	public String getIdList() {
		String ids = "";
		for (int i = 0; i < vc.size(); i++) { // 서버에 접속한 유저의 수만큼 반복
			Client ct = vc.get(i);
			ids += ct.id + ";";                      // 서버에 접속한 유저 아이디를 ';'를 두고 ids에 저장
		}
		return ids;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////

	// 매개변수 id값으로 Client3를 검색
	public Client findClient(String id) {  // 매개변수 id와 서버에 접속한 유저의 아이디가 같은지 비교
		Client ct = null;
		for (int i = 0; i < vc.size(); i++) {
			ct = vc.get(i);
			if (ct.id.equals(id)) {                 // 매개변수(접속한 유저의) id와 Client의 id와 같다면...
				break;
			}
		} // --for
		return ct;
	}// --findClient

//	//////////////////////////////////////////////////////////////////////////

	class Client extends Thread {

		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id = "익명";

		public Client(Socket sock) {
			try {
				this.sock = sock;             // 소켓 : 클라이언트를 생각하면됨.
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				// 클라이언트가 입력한 데이터를 1바이트 단위로 문자단위로 처리하는데 BufferedReader를 통해 여러 데이터를 입력 받을 수 있게.
				out = new PrintWriter((sock.getOutputStream()), true);
				// 클라리언트가입력한 데이터들을 다른 클라이언트들에게 보내주는 것
				System.out.println(sock + " 접속됨...");

				//서버 기준으로 in = 클라이언트에서 서버로 보낸 데이터를 받은 것
				//서버 기준으로 out = 서버에서 클라이언트로 보낼 데이터를 모아둔 것


				//System.out.println("in : "+in);
				//System.out.println("out : "+out);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////
		@Override
		public void run() {
			try {
				while (true) {
					String line = in.readLine(); // 서버에서 받은 데이터가
					//System.out.println("in : "+in);
					//System.out.println("readline : "+in.readLine());
					if (line == null)                  // 널인경우 브레이크
						break;
					else
						routine(line);                 // 아니면 루틴(데이터)
				}
			} catch (Exception e) {              // 서버와 클라이언트의 연결 중 에러가 발생한다면
				//sendAllMessage(ChatProtocol3.CHATALL+
				//ChatProtocol3.MODE+this.id +"님이 나가셨습니다.");
				removeClient(this);               // 그 클라이언트 제거
				System.err.println(sock + "[" + id + "] 끊어짐...");
			}
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////
		public void routine(String line) {
			System.out.println("line:" + line); //데이터
			int idx = line.indexOf(ChatProtocol3.MODE);  // ChatProtocol3.MODE = ':'를 뜻함.
			String cmd = line.substring(0, idx);    // cmd : 클라이언트가 서버에 보낸 데이터의 모드(ID, chat, chatall, message 등)
			String data = line.substring(idx + 1); // data : 클라이언트가 서버에 보낸 데이터의 값


			int chatid = 0;                     // 유저가 보낸 채팅 메시지를 저장해서 db로 보내기 위함
			String content = null;               // 유저가 보낸 채팅 메시지를 저장해서 db로 보내기 위함
			int chatroomid = 0;                // 유저가 보낸 채팅 메시지를 저장해서 db로 보내기 위함
			String userid = null;                   // 유저가 보낸 채팅 메시지를 저장해서 db로 보내기 위함
			Timestamp createdat = null;// 유저가 보낸 채팅 메시지를 저장해서 db로 보내기 위함
			char readstate = '0';                  // 유저가 보낸 채팅 메시지를 저장해서 db로 보내기 위함


			//ID:aaa;1234
			if (cmd.equals(ChatProtocol3.ID)) {    // 클라이언트가 보낸 데이터의 모드가 id 일때(로그인때문에 서버에 입력)
				//data = aaa;1234
				idx = data.indexOf(';');
				cmd = data.substring(0, idx); 			// aaa
				data = data.substring(idx + 1); 		// 1234

				if(mgr.loginChk(cmd, data)) { // id,pw  //로그인 체크하는 부분, 일단 aaa 계정만 로그인 되도록 만듬
	              /*
	            	Client ct = findClient(cmd);               // 이중접속 잡아내기
	               if(ct!=null && ct.id.equals(cmd)) {// 이중접속 잡아내기

	                  sendMessage(ChatProtocol3.ID+
	                        ChatProtocol3.MODE+"C");
	               }else { // 접속성공
	               */
					id = cmd;
					sendMessage(ChatProtocol3.ID+
							ChatProtocol3.MODE+"T");
					sendAllMessage(ChatProtocol3.CHATLIST+
							ChatProtocol3.MODE+getIdList());
					//sendAllMessage(ChatProtocol3.CHATALL+
					// ChatProtocol3.MODE+"["+id+"]님이 입장하였습니다");

				}else { // 로그인 실패
					sendMessage(ChatProtocol3.ID+
							ChatProtocol3.MODE+"F");
				}
			} else if (cmd.equals(ChatProtocol3.CHATALL)) { // 방식이 chatall 일때, 전체채팅일때

				//System.out.println("data"+data);
	        	 /*
	        	 idx = data.indexOf(';');
		         chatid = Integer.parseInt(data.substring(0,idx)); // id
		         data = data.substring(idx + 1);                            // data = content;chatroom_tbl_id;userid;createdat;readstate

		         idx = data.indexOf(';');
		         content = data.substring(0,idx);                          // content
		         data = data.substring(idx + 1);                            // data = chatroom_tbl_id;userid;createdat;readstate

	        	//CHATALL:aaa;1234
	        	 // CHATALL:id;content;chatroom_tbl_id;userid;createdat;readstate

	             // data = aaa;1234
	            // data = id;content;chatroom_tbl_id;userid;createdat;readstate


		         idx = data.indexOf(';');
		         chatroomid = Integer.parseInt(data.substring(0,idx)); //  chatroom_tbl_id
		         data = data.substring(idx + 1);                            // data = userid;createdat;readstate

		         idx = data.indexOf(';');
		         userid = data.substring(0,idx);                             //  userid
		         data = data.substring(idx + 1);                            // data = createdat;readstate

		         idx = data.indexOf(';');
		         createdat = Timestamp.valueOf(data.substring(0,idx)); //  createdat
		         data = data.substring(idx + 1);                            // data = readstate

		         readstate = data.charAt(0);                                  //
		         */


				idx = data.indexOf(';'); // message:이름; 문자열
				userid = data.substring(0,idx);
				content = data.substring(idx + 1);
				sendAllMessage(ChatProtocol3.CHATALL + ChatProtocol3.MODE + "[" + id + "]" + content); // CHATALL:[ID]할말
				//System.out.println("userid : "+userid);
				//System.out.println("content : "+content);
				ChatMessageBean bean = new ChatMessageBean();
				bean.setContent(content);      		 	//  ChatMessage 내용
				bean.setChatroomId(1);      		// 채팅방 세션의 아이디(int형), 일단 0으로 지정했음.
				bean.setUserId(userid);                   	    // 작성자 아이디
				//bean.setCreatedAt(createdat);                // 작성된 시간
				bean.setReadState(readstate);             	// 읽음 상태 0으로 지정
				mgr.insertChatMessage(bean);        			 // DB에 저장
			}
////////////////////////////////////////////////////////////////////////////////////////////////
			else if (cmd.equals(ChatProtocol3.MSGLIST)) { // 방식이 msglist일 때, 채팅방에서 채팅할 때

				//MSGLIST:aaa,bbb,밥먹자;bbb,ccc,하이;...
				Vector<ChatMessageBean> vlist = mgr.getMsgList(chatroomid,userid);
				String str = "";
				for (ChatMessageBean bean : vlist) {
					str+= bean.getId()+",";                                       // ChatMessage 기본키
					str+= bean.getContent()+",";                             // ChatMessage 내용
					str+= bean.getChatroomId()+",";                       // ChatMessage 채팅 세션의 고유 id
					str+= bean.getUserId()+",";                                // 작성자 id
					str+= bean.getCreatedAt()+",";                             // 언제 작성 되었는지
					str+= bean.getReadState()+";";                           // 읽어졌는지
				}
				sendMessage(ChatProtocol3.MESSAGE+      // 누가,누구에게,어떤내용을;누가,누구에게,어떤내용을;.....
						ChatProtocol3.MODE + str);

			}
		}

		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		new ChatServer();
	}
}
