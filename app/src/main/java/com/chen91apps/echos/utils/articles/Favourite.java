package com.chen91apps.echos.utils.articles;

import java.util.List;

public class Favourite {

    /**
     * state : 1
     * data : [{"follow_id":2,"user":1,"note":"baidu","content":"web::https://www.baidu.com/","create_time":1566984540},{"follow_id":1,"user":1,"note":"google","content":"web::https://www.google.com/","create_time":1566984522}]
     */

    private int state;
    private List<DataBean> data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * follow_id : 2
         * user : 1
         * note : baidu
         * content : web::https://www.baidu.com/
         * create_time : 1566984540
         */

        private int follow_id;
        private int user;
        private String note;
        private String content;
        private String create_time;

        public int getFollow_id() {
            return follow_id;
        }

        public void setFollow_id(int follow_id) {
            this.follow_id = follow_id;
        }

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
