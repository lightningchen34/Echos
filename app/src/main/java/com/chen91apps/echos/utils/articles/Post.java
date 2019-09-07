package com.chen91apps.echos.utils.articles;

import java.util.List;

public class Post
{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * post_id : 10
         * author : 1
         * title : Hello2
         * create_time : 1567143750
         */

        private int post_id;
        private int author;
        private String title;
        private int create_time;

        public int getPost_id() {
            return post_id;
        }

        public void setPost_id(int post_id) {
            this.post_id = post_id;
        }

        public int getAuthor() {
            return author;
        }

        public void setAuthor(int author) {
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }
    }
}
