package net.crazyminds.githubclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import net.crazyminds.githubclient.adapter.RepositoryResumeAdapter;
import net.crazyminds.githubclient.domain.RepositoryResume;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterator;


import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , AbsListView.OnScrollListener  {

    private static final String REPOSITORY_RESUME_LIST = "REPOSITORY_RESUME_LIST";
    private static final String IS_THERE_MORE = "IS_THERE_MORE";

    private ListView listView;
    private RepositoryResumeAdapter repositoryResumeAdapter;
    private ProgressBar progressBar;

    private RepositoryResume repositoryResume;
    private boolean isThereMore;
    private List<RepositoryResume> listRepositoryResume;

    int listViewYScroll;
    int listViewFirstVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repositoryResume = new RepositoryResume();
        listRepositoryResume = new ArrayList<RepositoryResume>();

        listView = (ListView) findViewById(R.id.mainactvity_listview);
        progressBar = (ProgressBar) findViewById(R.id.mainactvity_progressbar);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);

        Log.d("main" , "onCreate");

        if(savedInstanceState != null)
        {
            listRepositoryResume = (List<RepositoryResume>) savedInstanceState.getSerializable(REPOSITORY_RESUME_LIST);
            if(listRepositoryResume != null)
                PopulateListView();

            isThereMore = savedInstanceState.getBoolean(IS_THERE_MORE, false);
        }
        else
        {
            new ListRepositoriesAsyncTask().execute("0");
        }


        //GHRepository rep = github.getRepository("teste");
            //rep.listCommits()
    }

    void PopulateListView()
    {
        Log.d("main" , "PopulateListView ");

        repositoryResumeAdapter = new RepositoryResumeAdapter(MainActivity.this, listRepositoryResume);
        listView.setAdapter(repositoryResumeAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);

        if(listRepositoryResume != null)
            savedInstanceState.putSerializable(REPOSITORY_RESUME_LIST, (Serializable) listRepositoryResume);

        savedInstanceState.putBoolean(IS_THERE_MORE, isThereMore);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("main" , "onScrollStateChanged isThereMore " + isThereMore);
        if(isThereMore){

            if(listView.getLastVisiblePosition() + 1 == listRepositoryResume.size()){
                repositoryResume.setId(listRepositoryResume.get(listRepositoryResume.size() - 1).getId());
                isThereMore = false;
                new ListRepositoriesAsyncTask(repositoryResume.getId()+"").execute("list");
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("main" , "onItemClick " + position);
    }

    private class ListRepositoriesAsyncTask extends AsyncTask<Object, Object, Void>
    {
        String since = "0";

        public ListRepositoriesAsyncTask(){
        }

        public ListRepositoriesAsyncTask(String sinceparam)
        {
            since = sinceparam;
        }

        @Override
        protected void onPreExecute ()
        {
            Log.d("main" , "ListRepositoriesAsyncTask - onPreExecute");
            progressBar.setVisibility(View.VISIBLE);
            listViewFirstVisible = listView.getFirstVisiblePosition();
            View v = listView.getChildAt(0);
            listViewYScroll = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
        }

        @Override
        protected Void doInBackground(Object... params) {
            GitHub github = null;

            Log.d("main" , "ListRepositoriesAsyncTask - doInBackground");
            try {
               github = GitHub.connectAnonymously();
               PagedIterator<GHRepository> iterator = github.listAllPublicRepositories(since).iterator();
                int index = 0;
                while (iterator.hasNext() && index < 20 )
                {
                    GHRepository rep = iterator.next();
                    listRepositoryResume.add(new RepositoryResume (rep.getId(), rep.getName(), rep.getOwnerName()  ));
                    index++;
                }
                isThereMore = iterator.hasNext();
            }
            catch (NoSuchElementException e) {
                Log.d("main" , "ListRepositoriesAsyncTask - doInBackground ERROR " + e.getMessage());
            }
            catch (SocketTimeoutException e) {
                Log.d("main" , "ListRepositoriesAsyncTask - doInBackground ERROR " + e.getMessage());
            }
            catch (Exception e) {
                Log.d("main" , "ListRepositoriesAsyncTask - doInBackground ERROR " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute (Void result)
        {
            Log.d("main" , "ListRepositoriesAsyncTask onPostExecute " + result);
            PopulateListView();
            progressBar.setVisibility(View.GONE);
            listView.setSelectionFromTop(listViewFirstVisible, listViewYScroll);
        }
    }

}
