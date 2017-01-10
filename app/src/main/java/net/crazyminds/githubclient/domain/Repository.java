package net.crazyminds.githubclient.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by julio on 10/01/2017.
 */

public class Repository implements Parcelable {

    private int id;
    private String name;
    private String fullName;
    private String ownerName;
    private String Description;
    private String Language;
    private String HomePage;

    public Repository(){

    }

    protected Repository(Parcel in) {
        id = in.readInt();
        name = in.readString();
        fullName = in.readString();
        ownerName = in.readString();
        Description = in.readString();
        Language = in.readString();
        HomePage = in.readString();
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };

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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getHomePage() {
        return HomePage;
    }

    public void setHomePage(String homePage) {
        HomePage = homePage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( getId());
        dest.writeString( getName());
        dest.writeString( getFullName());
        dest.writeString( getOwnerName());
        dest.writeString( getDescription());
        dest.writeString( getLanguage());
        dest.writeString( getHomePage());

    }
}
