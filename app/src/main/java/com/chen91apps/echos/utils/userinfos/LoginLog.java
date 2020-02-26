package com.chen91apps.echos.utils.userinfos;


public class LoginLog {

    private int state;
    private String log;
    private UserInfo user;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
