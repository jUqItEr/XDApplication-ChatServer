package com.dita.xd.model;

import java.sql.Date;
import java.sql.Timestamp;

public class UserBean {
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private String profileImage;
    private String headerImage;
    private String address;
    private char gender;
    private String website;
    private Date birthday;
    private String introduce;
    private Timestamp createdAt;

    public UserBean() {
    }

    public UserBean(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public UserBean(String userId, String password, String email, Timestamp createdAt) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UserBean(String userId, String password, String email, String nickname, Timestamp createdAt) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public UserBean(String userId, String password, String email, String nickname, String profileImage,
                    String headerImage, String address, char gender, String website, Date birthday,
                    String introduce, Timestamp createdAt) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.headerImage = headerImage;
        this.address = address;
        this.gender = gender;
        this.website = website;
        this.birthday = birthday;
        this.introduce = introduce;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
