package com.dita.xd.runner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import com.dita.xd.controller.MessageController;
import com.dita.xd.controller.UserController;
import com.dita.xd.model.ChatMessageBean;
import com.dita.xd.util.server.MessageProtocol;

public class XdChatServer {
	private static final int PORT = 8003;

	protected Vector<XdClient> clients;
	protected ServerSocket server;
	protected MessageController messageController;
	protected UserController userController;

	public XdChatServer() {
		try {
			clients = new Vector<>();
			server = new ServerSocket(PORT);
			messageController = new MessageController();
			userController = new UserController();
		} catch (Exception e) {
			System.err.println("Error in Server");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("****************************************");
		System.out.println("* XDApplication Chatting Server");
		System.out.println("* Waiting for connection of client...");
		System.out.println("****************************************");

		try {
			while (true) {
				Socket sock = server.accept();
				XdClient ct = new XdClient(sock);
				ct.start();
				clients.addElement(ct);
			}
		} catch (Exception e) {
			System.err.println("Error in Socket");
			e.printStackTrace();
		}
	}

	public void sendAllMessage(String msg) {
		for (XdClient ct : clients) {
			ct.sendMessage(msg);
		}
	}

	public void removeClient(XdClient ct) {
		clients.remove(ct);
	}

	public String getIdList() {
		StringBuilder ids = new StringBuilder();

		for (XdClient ct : clients) {
			ids.append(ct.userId).append(";");
		}
		return ids.toString();
	}

	class XdClient extends Thread {
		private BufferedReader in;
		private PrintWriter out;
		private Socket sock;

		private String userId;
		private int chatroomId;

		public XdClient(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter((sock.getOutputStream()), true);
				System.out.println(sock + " connected");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					String line = in.readLine();

					if (line == null) {
						break;
					} else {
						process(line);
					}
				}
			} catch (Exception e) {
				removeClient(this);
				System.err.println(sock + "[" + chatroomId + ',' + userId + "] has been disconnected");
			}
		}

		public void process(String line) throws Exception {
			String[] token = line.split(MessageProtocol.SEPARATOR);
			String cmd = token[0];
			String data = token[1];

			System.out.println("Client line: " + line);

			switch (cmd) {
				case MessageProtocol.ID -> {
					token = data.split(";");
					userId = token[0];
					chatroomId = Integer.parseInt(token[1]);

					sendMessage(MessageProtocol.ID +
							MessageProtocol.SEPARATOR + "T");
					sendAllMessage(MessageProtocol.CHAT_LIST +
							MessageProtocol.SEPARATOR + getIdList());
				}
				case MessageProtocol.BYE -> throw new Exception("User want close");
				case MessageProtocol.CHAT_ALL -> {
					token = data.split(";");
					String userId = token[0];
					String content = token[2];
					int chatroomId = Integer.parseInt(token[1]);

					if (this.chatroomId == chatroomId) {
						ChatMessageBean bean = new ChatMessageBean();
						bean.setContent(content);
						bean.setChatroomId(chatroomId);
						bean.setUserId(userId);
						bean.setReadState("0");

						System.out.println("Database injected: " + messageController.appendMessage(bean));
						sendAllMessage(line);
					}
				}
				case MessageProtocol.DISMISS -> {
					token = data.split(";");
					String userId = token[0];
					int chatroomId = Integer.parseInt(token[1]);
					userController.dismissUser(chatroomId, userId);
					sendAllMessage(line);
				}
				case MessageProtocol.MSG_LIST -> {
					Vector<ChatMessageBean> messages = messageController.getMessages(chatroomId);
					StringBuilder str = new StringBuilder();

					for (ChatMessageBean bean : messages) {
						str.append(bean.getId()).append(",");
						str.append(bean.getContent()).append(",");
						str.append(bean.getChatroomId()).append(",");
						str.append(bean.getUserId()).append(",");
						str.append(bean.getCreatedAt()).append(",");
						str.append(bean.getReadState()).append(";");
					}
					sendMessage(MessageProtocol.MESSAGE + MessageProtocol.SEPARATOR + str);
				}
			}
		}

		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
}
