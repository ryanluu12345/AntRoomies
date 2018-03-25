package com.example.ryanluu2017.roomright;

/**
 * Created by Ryanluu2017 on 3/20/2018.
 */

//TODO Add user likes, favorites, etc.
public class User {

    public String uid;
    public String name;
    public String email;
    public String about;
    public String roommatePrefs;
    public String picture;
    public String party;
    public String clean;
    public String drink;
    public String cleanliness;
    public String sleep;
    public String housing;
    public String friends;

    //Default constructor
    public User(){


    }

    //Constructor that takes in preferences
    public User(String uid,String name,String email, String about, String roommatePrefs, String picture){
        this.uid=uid;
        this.name=name;
        this.email=email;
        this.about=about;
        this.roommatePrefs=roommatePrefs;
        this.picture=picture;
        this.party="";
        this.clean="";
        this.drink="";
        this.cleanliness="";
        this.sleep="";
        this.housing="";
        this.friends="";
    }

    /* Getters and setters for each
    piece of user information */

    public String getUid(){
        return this.uid;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getAbout(){
        return this.about;
    }

    public String getRoommatePrefs(){
        return this.roommatePrefs;
    }

    public String getPicture(){
        return this.picture;
    }

    public String getParty(){
        return this.party;
    }

    public String getClean(){
        return this.clean;
    }

    public String getDrink(){
        return this.drink;
    }

    public String getCleanliness(){
        return this.cleanliness;
    }

    public String getSleep(){
        return this.sleep;
    }

    public String getHousing(){
        return this.housing;
    }

    public String getFriends(){
        return this.friends;
    }
}
