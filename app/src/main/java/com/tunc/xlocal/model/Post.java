package com.tunc.xlocal.model;

import java.util.Date;

public class Post {


    public String description;
    public String userUudi;
    public String postImageDownloadUrl;
    public double latitute;
    public double longitute;
    public Date  date;
    public long countOfComment;
    public long countOfConfirm;
    public long countOfJoin;
    public long countOfLike;

    public Post(String description, String userUudi, String postImageDownloadUrl, double latitute, double longitute, Date date, int countOfComment, int countOfConfirm, int countOfJoin, int countOfLike) {
        this.description = description;
        this.userUudi = userUudi;
        this.postImageDownloadUrl = postImageDownloadUrl;
        this.latitute = latitute;
        this.longitute = longitute;
        this.date = date;
        this.countOfComment = countOfComment;
        this.countOfConfirm = countOfConfirm;
        this.countOfJoin = countOfJoin;
        this.countOfLike = countOfLike;
    }

    public Post(){

    }



}
