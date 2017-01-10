package net.crazyminds.githubclient.connection;

/**
 * Created by julio on 10/01/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import net.crazyminds.githubclient.domain.PullRequest;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestQueryBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.HttpException;
import org.kohsuke.github.PagedIterator;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ListRepositoryPullAsyncTask extends AsyncTask<Object, Object, Void> {

    public static final String REPOSITORYPULLLIST_ASYNCTASK_RESULT = "net.crazyminds.REPOSITORYPULLLIST_ASYNCTASK_RESULT";

    RepositoryPullListAsyncTaskResult result = new RepositoryPullListAsyncTaskResult();
    LocalBroadcastManager broadcaster;
    String since = "0";
    String repositoryFullName = "";

    public ListRepositoryPullAsyncTask(Context context, String repositoryfullname) {
        broadcaster = LocalBroadcastManager.getInstance(context);
        repositoryFullName = repositoryfullname;
    }

    public ListRepositoryPullAsyncTask(Context context, String sinceparam, String repositoryfullname)
    {
        broadcaster = LocalBroadcastManager.getInstance(context);
        since = sinceparam;
        repositoryFullName = repositoryfullname;
    }

    @Override
    protected Void doInBackground(Object... params) {
        GitHub github = null;

        Log.d("ListPoolAsyncTask", "ListRepositoryPullAsyncTask - doInBackground");
        try {
            //github = GitHub.connectAnonymously();
            github = GitHub.connectUsingPassword("juliodutra2003", "lort2t,2185");

            Log.d("ListPoolAsyncTask", "ListRepositoryPullAsyncTask - github.isCredentialValid() " + github.isCredentialValid());
            Log.d("ListPoolAsyncTask", "ListRepositoryPullAsyncTask - github.isAnonymous() " + github.isAnonymous());
            Log.d("ListPoolAsyncTask", "ListRepositoryPullAsyncTask - github.isOffline() " + github.isOffline());

            GHRepository rep = github.getRepository(repositoryFullName);
            GHPullRequestQueryBuilder pullReqs =  rep.queryPullRequests();
            pullReqs.sort(GHPullRequestQueryBuilder.Sort.UPDATED);
            PagedIterator<GHPullRequest> iterator = pullReqs.list().iterator() ;
            int index = 0;
            result.pullRequestList = new ArrayList<PullRequest>();
            while (iterator.hasNext() && index < 20) {
                GHPullRequest pull = iterator.next();
                result.pullRequestList.add(new PullRequest( pull.getId(), String.valueOf(pull.getCreatedAt().getTime()) , pull.getTitle(), pull.getUser().getName() ));
                index++;
            }
            result.isThereMore = iterator.hasNext();
        } catch (HttpException e) {
            e.printStackTrace();
            Log.d("ListPoolAsyncTask", "ListRepositoryPullAsyncTask - HttpException ERROR " + e.getMessage());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            Log.d("ListPoolAsyncTask", "ListRepositoryPullAsyncTask - SocketTimeoutException ERROR " + e.getMessage());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.d("ListPoolAsyncTask", "ListRepositoryPullAsyncTask - NoSuchElementException ERROR " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ListPoolAsyncTask", "ListRepositoryPullAsyncTask - Exception ERROR " + e.getMessage() + " ");
        }

        return null;
    }

    @Override
    protected void onPostExecute (Void result)
    {
        Log.d("ListPoolAsyncTask" , "ListRepositoryPullAsyncTask onPostExecute " + result);
        sendResult();
    }

    private void sendResult() {
        Intent intent = new Intent(REPOSITORYPULLLIST_ASYNCTASK_RESULT);
        if(result != null)
        {
            Log.d("ListPoolAsyncTask" , "sendResult " + result);
            intent.putExtra(REPOSITORYPULLLIST_ASYNCTASK_RESULT, result);
            broadcaster.sendBroadcast(intent);
        }
    }

    public class RepositoryPullListAsyncTaskResult implements Serializable
    {
        List<PullRequest> pullRequestList;
        boolean isThereMore;

        public boolean isThereMore() {
            return isThereMore;
        }

        public void setThereMore(boolean thereMore) {
            isThereMore = thereMore;
        }

        public List<PullRequest> getPullRequestList() {
            return pullRequestList;
        }

        public void setPullRequestList(List<PullRequest> pullRequestList) {
            this.pullRequestList = pullRequestList;
        }
    }

}
