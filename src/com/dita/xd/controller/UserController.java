package com.dita.xd.controller;

import com.dita.xd.service.implementation.UserServiceImpl;

public class UserController {
    private final UserServiceImpl svc;

    public UserController() {
        svc = new UserServiceImpl();
    }

    public boolean dismissUser(int chatroomId, String userId) {
        return svc.dismissUser(chatroomId, userId);
    }
}
