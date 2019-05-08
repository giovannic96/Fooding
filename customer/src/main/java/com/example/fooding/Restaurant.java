package com.example.fooding;

import java.io.Serializable;

public class Restaurant implements Serializable {

    public String name;
    public String type;
    public String photoUrl;
    public String uid;
    public String uri;

    public Restaurant() {};

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {this.uri=uri;
    }

    public String getUri() {
        return uri;
    }
}
