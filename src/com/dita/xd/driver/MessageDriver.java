package com.dita.xd.driver;

import com.dita.xd.service.implementation.MessageServiceImpl;

import java.sql.Timestamp;

/**
 * @deprecated For testing DB query.
 * */
public class MessageDriver {
    public static void main(String[] args) {
        MessageServiceImpl impl = new MessageServiceImpl();

//        System.out.println(impl.getMessage(2));
//
//        impl.getMessages(1).forEach(bean -> {
//            System.out.printf("[%s(@%s)]%-20s \t%s\n",
//                    bean.getUserNickname(), bean.getUserId(), bean.getContent(), bean.getCreatedAt());
//        });
//        impl.getMessages(1, "bbb").forEach( bean -> {
//            System.out.printf("[%s(@%s)]%-20s \t%s\n",
//                    bean.getUserNickname(), bean.getUserId(), bean.getContent(), bean.getCreatedAt());
//        });
        impl.getMessages(1, "aaa", Timestamp.valueOf("2023-08-31 23:59:59")).forEach(bean -> {
            System.out.printf("[%s(@%s)]%-20s \t%s\n",
                    bean.getUserNickname(), bean.getUserId(), bean.getContent(), bean.getCreatedAt());
        });
    }
}
