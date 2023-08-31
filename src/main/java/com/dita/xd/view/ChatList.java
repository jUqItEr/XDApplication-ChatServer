package com.dita.xd.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Vector;
import java.awt.Label;
import com.dita.xd.model.ChatUserBean;

import net.MessageBean3;

public class ChatList extends MFrame implements ActionListener{
	String name = "";
	ChatListMgr mgr;
	Button select = new Button("�����ϱ�");
	List clist = new List();
	BufferedReader in;
	PrintWriter out;
	String id;
	public ChatList(BufferedReader in, PrintWriter out, String id) {
		setSize(200, 400);
		setLayout(new BorderLayout()); // ��ġ������
		this.in = in;      // Ŭ�󸮾�Ʈ �̸�
		this.out = out; // 
		this.id = id;     // Ŭ�󸮾�Ʈ ���̵�
		
		Label title = new Label("ä�ù� ����Ʈ",Label.CENTER);
		add(title,"North");
		add(clist);
		add(select,"South");
		mgr = new ChatListMgr();
		
		Vector<ChatUserBean> content = mgr.getMsgList("aaa");
		 String str = "";
    	 for (ChatUserBean bean : content) {
			str+= bean.getChatroomId()+",";                             // ����
			str+= bean.getUserId();                             // ��������
			clist.add(str);
			str = "";
		}
    	 //System.out.println(str);
    	 select.addActionListener(this);
    	 /*
		for(int i = 0; i < content.size(); i++) {
			Clist.add(content.toString(),i);
		}
		*/
		//Clist.add("�ȳ��ϼ���",1);
		//Clist.add("�ݰ����ϴ�.",0);
		clist.requestFocus();
		validate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		int commaIdx;
		int chatRoomId;
		String chatUserId;
		
		if(obj == select) { // �����ϱ� ��ư�� ������ ��
			String assist = clist.getSelectedItem();
			commaIdx = assist.indexOf(',');
			chatRoomId = Integer.parseInt(assist.substring(0, commaIdx));
			chatUserId = assist.substring(commaIdx+1);
			//System.out.println(chatRoomId+chatUserId);
			new ChatClient(in,out,id);
		}
	}

}
