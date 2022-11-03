package com.tunc.xlocal.model;

public class Comment {
    public String userName;
    public String imageUrl;
    public String comment;
    public String userUuid;
    public Comment(String userName, String imageUrl, String comment, String userUuid) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.comment = comment;
        this.userUuid = userUuid;
    }

    public Comment(){

    }


}
