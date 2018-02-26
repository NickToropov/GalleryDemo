package com.example.gallerydemo.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class PhotoAuthor {

    @SerializedName("firstname")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("fullname")
    private String fullName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }
}
