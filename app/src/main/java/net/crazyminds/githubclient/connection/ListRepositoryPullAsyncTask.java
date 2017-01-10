package net.crazyminds.githubclient.connection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

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

/**
 * Created by julio on 10/01/2017.
 *
 * This class gets a list of pull requests from a repository, from GitHub, asynchronously and anonymously
 *
 * The result is paged and have a cap of 20 objects
 *
 * Returns to the caller by broadcast
 */

public class ListRepositoryPullAsyncTask extends AsyncTask<Object, Object, Void> {

    public static final String REPOSITORYPULLLIST_ASYNCTASK_RESULT = "net.crazyminds.REPOSITORYPULLLIST_ASYNCTASK_RESULT";

    RepositoryPullListAsyncTaskResult result = new RepositoryPullListAsyncTaskResult();
    LocalBroadcastManager broadcaster;
    String since = "0";
    String repositoryFullName = "";

    /**
     * GetRepositoryAsyncTask Constructor
     *
     * @param context
     *      Context activity of the caller.
     * @param repositoryfullname
     *      The target repository full name
     */
    public ListRepositoryPullAsyncTask(Context context, String repositoryfullname) {
        broadcaster = LocalBroadcastManager.getInstance(context);
        repositoryFullName = repositoryfullname;
    }

    /**
     * GetRepositoryAsyncTask Constructor
     *
     * @param context
     *      Context activity of the caller.
     * @param sinceparam
     *      Index of the first object to retrieve
     * @param repositoryfullname
     *      The target repository full name
     */
    public ListRepositoryPullAsyncTask(Context context, String sinceparam, String repositoryfullname)
    {
        broadcaster = LocalBroadcastManager.getInstance(context);
        since = sinceparam;
        repositoryFullName = repositoryfullname;
    }

    @Override
    protected Void doInBackground(Object... params) {
        GitHub github = null;

        try {
            github = GitHub.connectAnonymously();

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
        Intent intent = new Intent(REPOSITORYPULLLIST_ASYNCTASK_RESULT);
        if(result != null)
        {
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
