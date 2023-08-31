package com.dita.xd.model;

public class ChatProtocol3 {
	// �������� ID, CHAT(1��1��ȭ), 
	// CHATALL, CHATLIST(ê ������), MESSAGE(����������)
	
	// Ŭ���̾�Ʈ->������ ������ ID:aaa(��)�� ������ ��
	// ����-> Ŭ���̾�Ʈ�� ������ CHATLIST : aaa;bbb;ccc;(�����ڸ���Ʈ)
	public static final String ID = "ID";
	
	// Ŭ���̾�Ʈ->������ ������ CHAT:�޴¾��̵�;�޼����� ������ ��
	// ex) CHAT:bbb;�����
	// ����-> Ŭ���̾�Ʈ CHAT:�����¾��̵�;�޼���
	// ex) CHAT:aaa;�����
	public static final String CHAT = "CHAT";
	
	//(C->S)CHATALL: �޽���
	//(S->C)CHATALL:[�����¾��̵�]�޼���
	public static final String CHATALL = "CHATALL";
	
	//(S->C)CHATLIST:aaa;bbb;ccc
	public static final String CHATLIST = "CHATLIST";
	
	// Ŭ���̾�Ʈ->������ ������ CHAT:�޴¾��̵�;�޼����� ������ ��
	// ex) CHAT:bbb;�����
	// ����-> Ŭ���̾�Ʈ CHAT:�����¾��̵�;�޼���
	// ex) CHAT:aaa;�����
	public static final String MESSAGE = "MESSAGE";
	
	//(C->S) MSGLIST:id
	//(S->C) MSGLIST:fid,tid,msg;fid,tid,msg;...
	//(S->C) MSGLIST:aaa,bbb,�����;bbb,ccc,����...
	public  static final String MSGLIST = "MSGLIST";
	
	public static final String MODE = ":";

}
