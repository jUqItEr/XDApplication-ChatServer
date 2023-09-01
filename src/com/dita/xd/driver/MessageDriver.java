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
//            System.out.printf("%s [%s(@%s)]\t %s\n",
//                    bean.getCreatedAt(), bean.getUserNickname(), bean.getUserId(), bean.getContent());
//        });
//        impl.getMessages(1, "bbb").forEach( bean -> {
//            System.out.printf("%s [%s(@%s)]\t %s\n",
//                    bean.getCreatedAt(), bean.getUserNickname(), bean.getUserId(), bean.getContent());
//        });
        impl.getMessages(1, "aaa", "2023-09-01 23:59:59").forEach(bean -> {
            System.out.printf("%s [%s(@%s)]\t %s\n",
                    bean.getCreatedAt(), bean.getUserNickname(), bean.getUserId(), bean.getContent());
        });
    }
}
