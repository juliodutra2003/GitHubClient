package net.crazyminds.githubclient.domain;

import java.io.Serializable;

/**
 * Created by julio on 10/01/2017.
 */

public class Repository implements Serializable {

    private int id;
    private String name;
    private String fullName;
    private String ownerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
