package com.dita.xd;

import com.dita.xd.runner.XdChatServer;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(XdChatServer::new);
    }
}