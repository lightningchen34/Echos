package com.chen91apps.echos.utils.articles;

import java.util.List;

public class Post_Comments {


    /**
     * state : 1
     * data : [{"comment_id":10,"post":{"post_id":1,"author":"lightning34","title":"Title1","content":"Content","create_time":"2019-08-28 17:05:54"},"author":"lightning34","content":"","quote":{"author":"lightning34","content":"","create_time":1567928353},"create_time":1567928364}]
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
         * comment_id : 10
         * post : {"post_id":1,"author":"lightning34","title":"Title1","content":"Content","create_time":"2019-08-28 17:05:54"}
         * author : lightning34
         * content :
         * quote : {"author":"lightning34","content":"","create_time":1567928353}
         * create_time : 1567928364
         */

        private int comment_id;
        private Post.DataBean post;
        private String author;
        private String content;
        private QuoteBean quote;
        private String create_time;

        public int getComment_id() {
            return comment_id;
        }

        public void setComment_id(int comment_id) {
            this.comment_id = comment_id;
        }

        public Post.DataBean getPost() {
            return post;
        }

        public void setPost(Post.DataBean post) {
            this.post = post;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public QuoteBean getQuote() {
            return quote;
        }

        public void setQuote(QuoteBean quote) {
            this.quote = quote;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public static class PostBean {
            /**
             * post_id : 1
             * author : lightning34
             * title : Title1
             * content : Content
             * create_time : 2019-08-28 17:05:54
             */

            private int post_id;
            private String author;
            private String title;
            private String content;
            private String create_time;

            public int getPost_id() {
                return post_id;
            }

            public void setPost_id(int post_id) {
                this.post_id = post_id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

        public static class QuoteBean {
            /**
             * author : lightning34
             * content :
             * create_time : 1567928353
             */

            private String author;
            private String content;
            private String create_time;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
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
}
