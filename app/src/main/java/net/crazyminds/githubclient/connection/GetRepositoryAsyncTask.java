package net.crazyminds.githubclient.connection;

/**
 * Created by julio on 10/01/2017.
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

    public GetRepositoryAsyncTask(Context context, String repositoryfullname)
    {
        broadcaster = LocalBroadcastManager.getInstance(context);
        repositoryFullName = repositoryfullname;
    }

    @Override
    protected Void doInBackground(Object... params) {
        GitHub github = null;

        Log.d("GetRepAsyncTask", "GetRepositoryAsyncTask - doInBackground repositoryName " + repositoryFullName);
        try {
            //github = GitHub.connectAnonymously();
            github = GitHub.connectUsingPassword("juliodutra2003", "lort2t,2185");

            Log.d("GetRepAsyncTask", "GetRepositoryAsyncTask - github.isCredentialValid() " + github.isCredentialValid());
            Log.d("GetRepAsyncTask", "GetRepositoryAsyncTask - github.isAnonymous() " + github.isAnonymous());
            Log.d("GetRepAsyncTask", "GetRepositoryAsyncTask - github.isOffline() " + github.isOffline());

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
            Log.d("GetRepAsyncTask", "GetRepositoryAsyncTask - HttpException ERROR " + e.getMessage());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            Log.d("GetRepAsyncTask", "GetRepositoryAsyncTask - SocketTimeoutException ERROR " + e.getMessage());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.d("GetRepAsyncTask", "GetRepositoryAsyncTask - NoSuchElementException ERROR " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("GetRepAsyncTask", "GetRepositoryAsyncTask - Exception ERROR " + e.getMessage() + " ");
        }

        return null;
    }

    @Override
    protected void onPostExecute (Void result)
    {
        Log.d("GetRepAsyncTask" , "GetRepositoryAsyncTask onPostExecute " + result);
        sendResult();
    }

    private void sendResult() {
        Intent intent = new Intent(GETREPOSITORY_ASYNCTASK_RESULT);
        if(result != null)
        {
            Log.d("GetRepAsyncTask" , "sendResult " + result);
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
