package net.crazyminds.githubclient.connection;

/**
 * Created by julio on 10/01/2017.
 *
 * This class gets a repository from GitHub asynchronously and anonymously
 *
 * The result is paged and have a cap of 20 objects
 *
 * Returns to the caller by broadcast
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import net.crazyminds.githubclient.domain.Repository;
import net.crazyminds.githubclient.domain.RepositoryResume;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.HttpException;
import org.kohsuke.github.PagedIterator;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GetRepositoryAsyncTask extends AsyncTask<Object, Object, Void> {

    public static final String GETREPOSITORY_ASYNCTASK_RESULT = "net.crazyminds.GETREPOSITORY_ASYNCTASK_RESULT";

    GetRepositoryAsyncTaskResult result = new GetRepositoryAsyncTaskResult();
    LocalBroadcastManager broadcaster;
    String repositoryFullName = "";


    /**
     * GetRepositoryAsyncTask Constructor
     *
     * @param context
     *      Context activity of the caller.
     * @param repositoryfullname
     *      The target repository full name
     */
    public GetRepositoryAsyncTask(Context context, String repositoryfullname)
    {
        broadcaster = LocalBroadcastManager.getInstance(context);
        repositoryFullName = repositoryfullname;
    }

    @Override
    protected Void doInBackground(Object... params) {
        GitHub github = null;

        try {
            github = GitHub.connectAnonymously();

            GHRepository rep = github.getRepository(repositoryFullName);
            Repository repository = new Repository();
            repository.setId(rep.getId());
            repository.setName(rep.getName());
            repository.setFullName(rep.getFullName());
            repository.setOwnerName(rep.getOwnerName());
            repository.setDescription(rep.getDescription());
            repository.setLanguage(rep.getLanguage());
            repository.setHomePage(rep.getHomepage());

            result.setRepository( repository);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute (Void result)
    {
        sendResult();
    }

    private void sendResult() {
        Intent intent = new Intent(GETREPOSITORY_ASYNCTASK_RESULT);
        if(result != null)
        {
            intent.putExtra(GETREPOSITORY_ASYNCTASK_RESULT, result);
            broadcaster.sendBroadcast(intent);
        }
    }

    public class GetRepositoryAsyncTaskResult  implements Serializable
    {
        Repository repository;

        public Repository getRepository() {
            return repository;
        }

        public void setRepository(Repository repository) {
            this.repository = repository;
        }
    }

}
