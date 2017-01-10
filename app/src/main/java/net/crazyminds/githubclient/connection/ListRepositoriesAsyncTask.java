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

public class ListRepositoriesAsyncTask extends AsyncTask<Object, Object, Void> {

        public static final String REPOSITORYLIST_ASYNCTASK_RESULT = "net.crazyminds.REPOSITORYLIST_ASYNCTASK_RESULT";

        RepositoryListAsyncTaskResult result = new RepositoryListAsyncTaskResult();
        LocalBroadcastManager broadcaster;
        String since = "0";

        public ListRepositoriesAsyncTask(Context context) {
            broadcaster = LocalBroadcastManager.getInstance(context);
        }

        public ListRepositoriesAsyncTask(Context context, String sinceparam)
        {
            broadcaster = LocalBroadcastManager.getInstance(context);
            since = sinceparam;
        }

        @Override
        protected Void doInBackground(Object... params) {
            GitHub github = null;

            Log.d("ListRepAsyncTask", "ListRepositoriesAsyncTask - doInBackground");
            try {
                //github = GitHub.connectAnonymously();
                github = GitHub.connectUsingPassword("juliodutra2003", "lort2t,2185");

                Log.d("ListRepAsyncTask", "ListRepositoriesAsyncTask - github.isCredentialValid() " + github.isCredentialValid());
                Log.d("ListRepAsyncTask", "ListRepositoriesAsyncTask - github.isAnonymous() " + github.isAnonymous());
                Log.d("ListRepAsyncTask", "ListRepositoriesAsyncTask - github.isOffline() " + github.isOffline());

                PagedIterator<GHRepository> iterator = github.listAllPublicRepositories(since).iterator();
                int index = 0;
                result.repositoryResumeList = new ArrayList<RepositoryResume>();
                while (iterator.hasNext() && index < 20) {
                    GHRepository rep = iterator.next();
                    result.repositoryResumeList.add(new RepositoryResume(rep.getId(), rep.getName(), rep.getOwnerName(), rep.getFullName() ));
                    index++;
                }
                result.isThereMore = iterator.hasNext();
            } catch (HttpException e) {
                e.printStackTrace();
                Log.d("ListRepAsyncTask", "ListRepositoriesAsyncTask - HttpException ERROR " + e.getMessage());
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.d("ListRepAsyncTask", "ListRepositoriesAsyncTask - SocketTimeoutException ERROR " + e.getMessage());
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                Log.d("ListRepAsyncTask", "ListRepositoriesAsyncTask - NoSuchElementException ERROR " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("ListRepAsyncTask", "ListRepositoriesAsyncTask - Exception ERROR " + e.getMessage() + " ");
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void result)
        {
            Log.d("ListRepAsyncTask" , "ListRepositoriesAsyncTask onPostExecute " + result);
            sendResult();
        }

    private void sendResult() {
        Intent intent = new Intent(REPOSITORYLIST_ASYNCTASK_RESULT);
        if(result != null)
        {
            Log.d("ListRepAsyncTask" , "sendResult " + result);
            intent.putExtra(REPOSITORYLIST_ASYNCTASK_RESULT, result);
            broadcaster.sendBroadcast(intent);
        }
    }

    public class RepositoryListAsyncTaskResult  implements Serializable
    {
        List<RepositoryResume> repositoryResumeList;
        boolean isThereMore;

        public boolean isThereMore() {
            return isThereMore;
        }

        public void setThereMore(boolean thereMore) {
            isThereMore = thereMore;
        }

        public List<RepositoryResume> getRepositoryResumeList() {
            return repositoryResumeList;
        }

        public void setRepositoryResumeList(List<RepositoryResume> repositoryResumeList) {
            repositoryResumeList = repositoryResumeList;
        }
    }

}
