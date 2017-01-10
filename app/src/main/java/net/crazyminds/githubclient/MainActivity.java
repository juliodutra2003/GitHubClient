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
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , AbsListView.OnScrollListener  {

    private ListView listView;
    private RepositoryResumeAdapter repositoryResumeAdapter;
    private ProgressBar progressBar;

    private RepositoryResume repositoryResume;
    private boolean isThereMore;
    private List<RepositoryResume> listRepositoryResume;


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


        new ListRepositoriesAsyncTask().execute("0");

        //GHRepository rep = github.getRepository("teste");
            //rep.listCommits()
    }

    void PopulateListView()
    {
        Log.d("main" , "PopulateListView ");

        repositoryResumeAdapter = new RepositoryResumeAdapter(MainActivity.this, listRepositoryResume);
        Log.d("main" , "PopulateListView 3");
        listView.setAdapter(repositoryResumeAdapter);
        Log.d("main" , "PopulateListView 4");

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("main" , "onScrollStateChanged " + scrollState);
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
                    Log.d("main" , "PopulateListView - add repository to list " + rep.getName());
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
        }
    }

}
