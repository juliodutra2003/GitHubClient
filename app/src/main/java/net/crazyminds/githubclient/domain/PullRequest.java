package net.crazyminds.githubclient.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by julio on 10/01/2017.
 */

public class PullRequest implements Parcelable {

    private int id;
    private String creationDate;
    private String title;
    private String ownerName;

    public PullRequest(){
    }

    public PullRequest(int idparam, String creationDateparam, String titleparam, String ownerNameparam)
    {
        id = idparam;
        creationDate = creationDateparam;
        title = titleparam;
        ownerName = ownerNameparam;
    }

    protected PullRequest(Parcel in) {
        id = in.readInt();
        creationDate = in.readString();
        title = in.readString();
        ownerName = in.readString();
    }

    public static final Creator<PullRequest> CREATOR = new Creator<PullRequest>() {
        @Override
        public PullRequest createFromParcel(Parcel in) {
            return new PullRequest(in);
        }

        @Override
        public PullRequest[] newArray(int size) {
            return new PullRequest[size];
        }
    };

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt( getId());
        dest.writeString( getCreationDate());
        dest.writeString( getTitle());
        dest.writeString( getOwnerName());
    }
}

