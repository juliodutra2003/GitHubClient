package net.crazyminds.githubclient.domain;

/**
 * Created by julio on 09/01/2017.
 */

public class RepositoryResume {

    private int id;
    private String name;
    private String ownerName;

    public RepositoryResume(){
    }

    public RepositoryResume(int idparam, String nameparam, String ownerparam)
    {
        id = idparam;
        name = nameparam;
        ownerName = ownerparam;
    }

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
        name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        ownerName = ownerName;
    }
}
