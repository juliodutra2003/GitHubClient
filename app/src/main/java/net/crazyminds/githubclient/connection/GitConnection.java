package net.crazyminds.githubclient.connection;

/**
 * Created by julio on 10/01/2017.
 */
public class GitConnection {
    private static GitConnection ourInstance = new GitConnection();

    public static GitConnection getInstance() {
        return ourInstance;
    }

    private GitConnection() {
    }
}
