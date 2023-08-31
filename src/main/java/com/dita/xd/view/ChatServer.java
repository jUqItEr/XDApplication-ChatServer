package com.dita.xd.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.net.ssl.SSLContext;

import com.dita.xd.ChatServer.Client;
import com.dita.xd.model.ChatMessageBean;

public class ChatServer {

	Vector<Client> vc;
	ServerSocket server;
	int port = 8003;                                                        // ��Ʈ��ȣ ����
	ChatMessageMgr mgr;

	public ChatServer() {
		
		try {
			vc = new Vector<Client>();                            // Client3 �� ������ ���� �� �ִ� ���� vc ����
			server = new ServerSocket(port);                     // socket ����(��Ʈ : 13307) �� ����
			mgr = new ChatMessageMgr();                                     // ê �����ڵ� ���� 
		} catch (Exception e) {                                         // 3 �� �ϳ��� ���� ����� ���� �߰� ����
			System.err.println("Error in Server");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("****************************************");
		System.out.println("*Welcome Chat Server 4.0...");
		System.out.println("*Ŭ���̾�Ʈ ������ ��ٸ��� �ֽ��ϴ�.");
		System.out.println("****************************************");
		try {
			while (true) {
				Socket sock = server.accept();                     // �������� sock(Ŭ���̾�Ʈ)�� ���ٴ��
				Client ct = new Client(sock);                   // ���ο� Ŭ���̾�Ʈ�� �����
				ct.start();                                                     // Ŭ���̾�Ʈ(user) ����
				vc.addElement(ct);                                      // vc�� ������ �����ش�.									
			}
		} catch (Exception e) {
			System.err.println("Error in Socket");
			e.printStackTrace();
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) { // ������ ������ ������ �������
			Client ct = vc.elementAt(i);
			ct.sendMessage(msg);               // �޼����� �����Ѵ�.
		}
	}

	public void removeClient(Client ct) {
		vc.remove(ct);                                 //  ���� ����(������ ������ ���� ���Ϳ��� ���� ����)
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	
	// ���ӵ� ��� id ����Ʈ ���� ex) aaa;bbb;ccc;ddd;ȫ�浿;
	public String getIdList() {
		String ids = "";
		for (int i = 0; i < vc.size(); i++) { // ������ ������ ������ ����ŭ �ݺ�
			Client ct = vc.get(i);
			ids += ct.id + ";";                      // ������ ������ ���� ���̵� ';'�� �ΰ� ids�� ����
		}
		return ids;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	
	// �Ű����� id������ Client3�� �˻�
	public Client findClient(String id) {  // �Ű����� id�� ������ ������ ������ ���̵� ������ ��
		Client ct = null;
		for (int i = 0; i < vc.size(); i++) {
			ct = vc.get(i);
			if (ct.id.equals(id)) {                 // �Ű�����(������ ������) id�� Client�� id�� ���ٸ�...
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
		String id = "�͸�";

		public Client(Socket sock) {
			try {
				this.sock = sock;             // ���� : Ŭ���̾�Ʈ�� �����ϸ��.
				in = new BufferedReader(new InputStreamReader(sock.getInputStream())); 
				// Ŭ���̾�Ʈ�� �Է��� �����͸� 1����Ʈ ������ ���ڴ����� ó���ϴµ� BufferedReader�� ���� ���� �����͸� �Է� ���� �� �ְ�.
				out = new PrintWriter((sock.getOutputStream()), true);
				// Ŭ�󸮾�Ʈ���Է��� �����͵��� �ٸ� Ŭ���̾�Ʈ�鿡�� �����ִ� ��
				System.out.println(sock + " ���ӵ�...");
				
				//���� �������� in = Ŭ���̾�Ʈ���� ������ ���� �����͸� ���� ��
				//���� �������� out = �������� Ŭ���̾�Ʈ�� ���� �����͸� ��Ƶ� ��
				
				
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
					String line = in.readLine(); // �������� ���� �����Ͱ�
					//System.out.println("in : "+in);
					//System.out.println("readline : "+in.readLine());
					if (line == null)                  // ���ΰ�� �극��ũ
						break;
					else
						routine(line);                 // �ƴϸ� ��ƾ(������)
				}
			} catch (Exception e) {              // ������ Ŭ���̾�Ʈ�� ���� �� ������ �߻��Ѵٸ�
				//sendAllMessage(ChatProtocol3.CHATALL+
                        //ChatProtocol3.MODE+this.id +"���� �����̽��ϴ�.");
				removeClient(this);               // �� Ŭ���̾�Ʈ ����
				System.err.println(sock + "[" + id + "] ������...");
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////////////
		public void routine(String line) {
			 System.out.println("line:" + line); //������
	         int idx = line.indexOf(ChatProtocol3.MODE);  // ChatProtocol3.MODE = ':'�� ����.
	         String cmd = line.substring(0, idx);    // cmd : Ŭ���̾�Ʈ�� ������ ���� �������� ���(ID, chat, chatall, message ��)
	         String data = line.substring(idx + 1); // data : Ŭ���̾�Ʈ�� ������ ���� �������� ��
	         
	         
	         int chatid = 0;                     // ������ ���� ä�� �޽����� �����ؼ� db�� ������ ���� 
	         String content = null;               // ������ ���� ä�� �޽����� �����ؼ� db�� ������ ���� 
	         int chatroomid = 0;                // ������ ���� ä�� �޽����� �����ؼ� db�� ������ ���� 
	         String userid = null;                   // ������ ���� ä�� �޽����� �����ؼ� db�� ������ ���� 
	         Timestamp createdat = null;// ������ ���� ä�� �޽����� �����ؼ� db�� ������ ���� 
	         char readstate = '0';                  // ������ ���� ä�� �޽����� �����ؼ� db�� ������ ���� 
	         
	       //ID:aaa;1234
	         if (cmd.equals(ChatProtocol3.ID)) {    // Ŭ���̾�Ʈ�� ���� �������� ��尡 id �϶�(�α��ζ����� ������ �Է�)
	        	 //data = aaa;1234
	            idx = data.indexOf(';');
	            cmd = data.substring(0, idx); 			// aaa 
	            data = data.substring(idx + 1); 		// 1234
	            
	            if(mgr.loginChk(cmd, data)) { // id,pw  //�α��� üũ�ϴ� �κ�, �ϴ� aaa ������ �α��� �ǵ��� ����
	              /*
	            	Client ct = findClient(cmd);               // �������� ��Ƴ���
	               if(ct!=null && ct.id.equals(cmd)) {// �������� ��Ƴ���
	                  
	                  sendMessage(ChatProtocol3.ID+
	                        ChatProtocol3.MODE+"C");
	               }else { // ���Ӽ���
	               */
	                  id = cmd;
	                  sendMessage(ChatProtocol3.ID+
	                        ChatProtocol3.MODE+"T");
	                  sendAllMessage(ChatProtocol3.CHATLIST+
	                        ChatProtocol3.MODE+getIdList());
	                  //sendAllMessage(ChatProtocol3.CHATALL+
	                       // ChatProtocol3.MODE+"["+id+"]���� �����Ͽ����ϴ�");
	               
	            }else { // �α��� ����
	               sendMessage(ChatProtocol3.ID+
	                     ChatProtocol3.MODE+"F");
	            }
	         } else if (cmd.equals(ChatProtocol3.CHATALL)) { // ����� chatall �϶�, ��üä���϶�
	        	 
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
	        	 
	        	 
		         idx = data.indexOf(';'); // message:�̸�; ���ڿ�
		         userid = data.substring(0,idx);
		         content = data.substring(idx + 1);
		         sendAllMessage(ChatProtocol3.CHATALL + ChatProtocol3.MODE + "[" + id + "]" + content); // CHATALL:[ID]�Ҹ�
		         //System.out.println("userid : "+userid);
		         //System.out.println("content : "+content);
		         ChatMessageBean bean = new ChatMessageBean();
		         bean.setContent(content);      		 	//  ChatMessage ����
		         bean.setChatroomId(1);      		// ä�ù� ������ ���̵�(int��), �ϴ� 0���� ��������. 
		         bean.setUserId(userid);                   	    // �ۼ��� ���̵�
		         //bean.setCreatedAt(createdat);                // �ۼ��� �ð�
		         bean.setReadState(readstate);             	// ���� ���� 0���� ����
		         mgr.insertChatMessage(bean);        			 // DB�� ���� 
	         }
////////////////////////////////////////////////////////////////////////////////////////////////
	         else if (cmd.equals(ChatProtocol3.MSGLIST)) { // ����� msglist�� ��, ä�ù濡�� ä���� ��
	        	 
	        	 //MSGLIST:aaa,bbb,�����;bbb,ccc,����;...
	        	 Vector<ChatMessageBean> vlist = mgr.getMsgList(chatroomid,userid);
	        	 String str = "";
	        	 for (ChatMessageBean bean : vlist) {
	        		str+= bean.getId()+",";                                       // ChatMessage �⺻Ű
					str+= bean.getContent()+",";                             // ChatMessage ����
					str+= bean.getChatroomId()+",";                       // ChatMessage ä�� ������ ���� id
					str+= bean.getUserId()+",";                                // �ۼ��� id
					str+= bean.getCreatedAt()+",";                             // ���� �ۼ� �Ǿ�����
					str+= bean.getReadState()+";";                           // �о�������
				}
	        	 sendMessage(ChatProtocol3.MESSAGE+      // ����,��������,�������;����,��������,�������;.....
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
