package com.dita.xd.service;

import com.dita.xd.model.UserBean;

import java.util.Vector;

public interface UserService extends Service {
    boolean dismissUser(int chatroomId, String userId);

    Vector<UserBean> getUsers(int chatroomId);
}
