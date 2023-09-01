package com.dita.xd.runner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import com.dita.xd.controller.MessageController;
import com.dita.xd.model.ChatMessageBean;
import com.dita.xd.util.server.MessageProtocol;

public class XdChatServer {
	private static final int PORT = 8003;			// 포트번호 설정

	protected Vector<Client> clients;
	protected ServerSocket server;
	protected MessageController controller;

	public XdChatServer() {
		try {
			clients = new Vector<>();                            // Client3 의 형식을 담을 수 있는 벡터 vc 생성
			server = new ServerSocket(PORT);                     // socket 서버(포트 : 8003) 를 만듦
			controller = new MessageController();
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
				clients.addElement(ct);                                      // vc에 유저를 더해준다.
			}
		} catch (Exception e) {
			System.err.println("Error in Socket");
			e.printStackTrace();
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////

	public void sendAllMessage(String msg) {
		for (Client ct : clients) {		// 서버에 접속한 유저들 모두한테
			ct.sendMessage(msg);		// 메세지를 전달한다.
		}
	}

	public void removeClient(Client ct) {
		clients.remove(ct);				//  유저 삭제(접속한 유저를 담은 벡터에서 유저 삭제)
	}

/////////////////////////////////////////////////////////////////////////////////////////////////

	// 접속된 모든 id 리스트 리턴 ex) aaa;bbb;ccc;ddd;홍길동;
	public String getIdList() {
		StringBuilder ids = new StringBuilder();

		for (Client ct : clients) { // 서버에 접속한 유저의 수만큼 반복
			ids.append(ct.userId).append(";");                      // 서버에 접속한 유저 아이디를 ';'를 두고 ids에 저장
		}
		return ids.toString();
	}

/////////////////////////////////////////////////////////////////////////////////////////////////

	// 매개변수 id값으로 Client3를 검색
	public Client findClient(String id) {  // 매개변수 id와 서버에 접속한 유저의 아이디가 같은지 비교
		Client ct = null;

		for (Client client : clients) {
			ct = client;

			if (ct.userId.equals(id)) {                 // 매개변수(접속한 유저의) id와 Client의 id와 같다면...
				break;
			}
		} // --for
		return ct;
	}// --findClient

//	//////////////////////////////////////////////////////////////////////////

	class Client extends Thread {
		private Socket sock;
		private BufferedReader in;
		private PrintWriter out;
		private String userId;
		private int chatroomId;

		public Client(Socket sock) {
			try {
				this.sock = sock;             // 소켓 : 클라이언트를 생각하면됨.
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				// 클라이언트가 입력한 데이터를 1바이트 단위로 문자단위로 처리하는데 BufferedReader를 통해 여러 데이터를 입력 받을 수 있게.
				out = new PrintWriter((sock.getOutputStream()), true);
				// 클라리언트가입력한 데이터들을 다른 클라이언트들에게 보내주는 것
				System.out.println(sock + "[Anonymous] 접속됨...");

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
						process(line);                 // 아니면 루틴(데이터)
				}
			} catch (Exception e) {              // 서버와 클라이언트의 연결 중 에러가 발생한다면
				//sendAllMessage(ChatProtocol3.CHATALL+
				//ChatProtocol3.MODE+this.id +"님이 나가셨습니다.");
				removeClient(this);               // 그 클라이언트 제거
				System.err.println(sock + "[" + userId + "] 끊어짐...");
			}
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////
		public void process(String line) {
			System.out.println("line:" + line); //데이터
			int idx = line.indexOf(MessageProtocol.SEPARATOR);  // ChatProtocol3.MODE = ':'를 뜻함.
			String cmd = line.substring(0, idx);    // cmd : 클라이언트가 서버에 보낸 데이터의 모드(ID, chat, chatall, message 등)
			String data = line.substring(idx + 1); // data : 클라이언트가 서버에 보낸 데이터의 값

			// 유저가 보낸 채팅 메시지를 저장해서 db로 보내기 위함

			//ID:aaa;1234
			switch (cmd) {
				case MessageProtocol.ID -> {     // 클라이언트가 보낸 데이터의 모드가 id 일때(로그인때문에 서버에 입력)
					idx = data.indexOf(';');
					cmd = data.substring(0, idx);            // aaa
					data = data.substring(idx + 1);        // 1234

					userId = cmd;
					chatroomId = Integer.parseInt(data);

					sendMessage(MessageProtocol.ID +
							MessageProtocol.SEPARATOR + "T");
					sendAllMessage(MessageProtocol.CHAT_LIST +
							MessageProtocol.SEPARATOR + getIdList());
				}
				case MessageProtocol.CHAT_ALL -> {  // 방식이 chatall 일때, 전체채팅일때
					idx = data.indexOf(';'); // message:이름; 문자열
					String userid = data.substring(0, idx);
					String content = data.substring(idx + 1);
					sendAllMessage(MessageProtocol.CHAT_ALL + MessageProtocol.SEPARATOR + "[" + userId + "]" + content); // CHATALL:[ID]할말

					//System.out.println("userid : "+userid);
					//System.out.println("content : "+content);
					ChatMessageBean bean = new ChatMessageBean();
					bean.setContent(content);                //  ChatMessage 내용
					bean.setChatroomId(chatroomId);            // 채팅방 세션의 아이디(int형), 일단 0으로 지정했음.
					bean.setUserId(userid);                        // 작성자 아이디
					bean.setReadState("0");

					//bean.setCreatedAt(createdat);                // 작성된 시간
					//bean.setReadState(readstate);             	// 읽음 상태 0으로 지정
					System.out.println("Database injected: " + controller.appendMessage(bean));
					//mgr.insertChatMessage(bean);                     // DB에 저장
				}
////////////////////////////////////////////////////////////////////////////////////////////////
				case MessageProtocol.MSG_LIST -> {  // 방식이 msglist일 때, 채팅방에서 채팅할 때
					//MSGLIST:aaa,bbb,밥먹자;bbb,ccc,하이;...
					Vector<ChatMessageBean> messages = controller.getMessages(chatroomId);
					//Vector<ChatMessageBean> vlist = mgr.getMsgList(chatroomId, userid);
					StringBuilder str = new StringBuilder();

					for (ChatMessageBean bean : messages) {
						str.append(bean.getId()).append(",");                                       // ChatMessage 기본키
						str.append(bean.getContent()).append(",");                             // ChatMessage 내용
						str.append(bean.getChatroomId()).append(",");                       // ChatMessage 채팅 세션의 고유 id
						str.append(bean.getUserId()).append(",");                                // 작성자 id
						str.append(bean.getCreatedAt()).append(",");                             // 언제 작성 되었는지
						str.append(bean.getReadState()).append(";");                           // 읽어졌는지
					}
					sendMessage(MessageProtocol.MESSAGE +      // 누가,누구에게,어떤내용을;누가,누구에게,어떤내용을;.....
							MessageProtocol.SEPARATOR + str);
				}
			}
		}

		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
}
