package com.tunc.xlocal.model;

public class User {

    public String name;
    public String username ;
    public String surname;
    public String gender;
    public String profilUrl ;
    public String role;
    long countOfComment;
    long countOfConfirm;
    long countOfJoin;
    long countOfLike;

    public User(String name,String username, String surname, String gender,String profileUrl){

        this.name=name;
        this.profilUrl=profileUrl;
        this.surname=surname;
        this.username = username;
        this.gender = gender;
    }
    public User(){

    }
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public String getProfilUrl() {
        return profilUrl;
    }

    public long getCountOfComment() {
        return countOfComment;
    }

    public long getCountOfConfirm() {
        return countOfConfirm;
    }

    public long getCountOfJoin() {
        return countOfJoin;
    }

    public long getCountOfLike() {
        return countOfLike;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setProfilUrl(String profilUrl) {
        this.profilUrl = profilUrl;
    }

    public void setCountOfComment(long countOfComment) {
        this.countOfComment = countOfComment;
    }

    public void setCountOfConfirm(long countOfConfirm) {
        this.countOfConfirm = countOfConfirm;
    }

    public void setCountOfJoin(long countOfJoin) {
        this.countOfJoin = countOfJoin;
    }

    public void setCountOfLike(long countOfLike) {
        this.countOfLike = countOfLike;
    }
}
