package net.crazyminds.githubclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import net.crazyminds.githubclient.adapter.RepositoryResumeAdapter;
import net.crazyminds.githubclient.connection.GetRepositoryAsyncTask;
import net.crazyminds.githubclient.connection.ListRepositoriesAsyncTask;
import net.crazyminds.githubclient.domain.Repository;
import net.crazyminds.githubclient.domain.RepositoryResume;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static net.crazyminds.githubclient.connection.GetRepositoryAsyncTask.GETREPOSITORY_ASYNCTASK_RESULT;
import static net.crazyminds.githubclient.connection.ListRepositoriesAsyncTask.REPOSITORYLIST_ASYNCTASK_RESULT;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , AbsListView.OnScrollListener  {

    private static final String REPOSITORY_RESUME_LIST = "REPOSITORY_RESUME_LIST";
    private static final String IS_THERE_MORE = "IS_THERE_MORE";
    public static final String REPOSITORY_DETAILL = "REPOSITORY_DETAILL";

    MainActivity context = this;

    BroadcastReceiver listRepBroadcastReceiver;
    BroadcastReceiver getRepBroadcastReceiver;

    private ListView listView;
    private RepositoryResumeAdapter repositoryResumeAdapter;
    private ProgressBar progressBar;

    private RepositoryResume repositoryResume;
    private boolean isThereMore;
    private List<RepositoryResume> listRepositoryResume;

    private Repository repository;

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

        listRepBroadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent)
            {
                ListRepositoriesAsyncTask.RepositoryListAsyncTaskResult result = (ListRepositoriesAsyncTask.RepositoryListAsyncTaskResult) intent.getSerializableExtra(REPOSITORYLIST_ASYNCTASK_RESULT);
                listRepositoryResume.addAll(result.getRepositoryResumeList());
                isThereMore = result.isThereMore();
                PopulateListView();
                progressBar.setVisibility(View.GONE);
                listView.setSelectionFromTop(listViewFirstVisible, listViewYScroll);
            }
        };

        getRepBroadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent)
            {
                GetRepositoryAsyncTask.GetRepositoryAsyncTaskResult result = (GetRepositoryAsyncTask.GetRepositoryAsyncTaskResult) intent.getSerializableExtra(GETREPOSITORY_ASYNCTASK_RESULT);
                repository = result.getRepository();
                progressBar.setVisibility(View.GONE);
                Intent intentDetail = new Intent( context , RepositoryDetailActivity.class);
                intentDetail.putExtra(REPOSITORY_DETAILL, repository);
                startActivity(intentDetail);
            }
        };

        if(savedInstanceState != null){
            listRepositoryResume = (List<RepositoryResume>) savedInstanceState.getSerializable(REPOSITORY_RESUME_LIST);
            if(listRepositoryResume != null)
                PopulateListView();

            isThereMore = savedInstanceState.getBoolean(IS_THERE_MORE, false);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            listViewFirstVisible = listView.getFirstVisiblePosition();
            View v = listView.getChildAt(0);
            listViewYScroll = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

            new ListRepositoriesAsyncTask(this).execute("0");
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        LocalBroadcastManager brInstance = LocalBroadcastManager.getInstance(this);
        brInstance.registerReceiver((listRepBroadcastReceiver), new IntentFilter(REPOSITORYLIST_ASYNCTASK_RESULT));
        brInstance.registerReceiver((getRepBroadcastReceiver), new IntentFilter(GETREPOSITORY_ASYNCTASK_RESULT));
    }

    @Override
    protected void onStop(){
        LocalBroadcastManager brInstance = LocalBroadcastManager.getInstance(this);
        brInstance.unregisterReceiver(listRepBroadcastReceiver);
        brInstance.unregisterReceiver(getRepBroadcastReceiver);
        super.onStop();
    }

    void PopulateListView(){
        repositoryResumeAdapter = new RepositoryResumeAdapter(MainActivity.this, listRepositoryResume);
        listView.setAdapter(repositoryResumeAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        if(listRepositoryResume != null)
            savedInstanceState.putSerializable(REPOSITORY_RESUME_LIST, (Serializable) listRepositoryResume);

        savedInstanceState.putBoolean(IS_THERE_MORE, isThereMore);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isThereMore){

            if(listView.getLastVisiblePosition() + 1 == listRepositoryResume.size()) {
                repositoryResume.setId(listRepositoryResume.get(listRepositoryResume.size() - 1).getId());
                isThereMore = false;

                progressBar.setVisibility(View.VISIBLE);
                listViewFirstVisible = listView.getFirstVisiblePosition();
                View v = listView.getChildAt(0);
                listViewYScroll = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

                new ListRepositoriesAsyncTask(this, String.valueOf(repositoryResume.getId()) ).execute("list");
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        progressBar.setVisibility(View.VISIBLE);
        new GetRepositoryAsyncTask(this, listRepositoryResume.get(position).getFullName() + "/" + listRepositoryResume.get(position).getName() ).execute("0");
    }
}
