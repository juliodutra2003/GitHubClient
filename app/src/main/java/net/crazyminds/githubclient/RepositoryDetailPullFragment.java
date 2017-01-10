package net.crazyminds.githubclient;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import net.crazyminds.githubclient.adapter.PullRequestAdapter;
import net.crazyminds.githubclient.connection.ListRepositoryPullAsyncTask;
import net.crazyminds.githubclient.domain.PullRequest;
import net.crazyminds.githubclient.domain.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static net.crazyminds.githubclient.RepositoryDetailActivity.CHOOSEN_REPOSITORY;
import static net.crazyminds.githubclient.connection.ListRepositoryPullAsyncTask.REPOSITORYPULLLIST_ASYNCTASK_RESULT;


/**
 * Created by julio on 10/01/2017.
 */

public class RepositoryDetailPullFragment extends android.support.v4.app.Fragment implements AbsListView.OnScrollListener  {

    private static final String PULL_LIST = "PULL_LIST";
    private static final String IS_THERE_MORE = "IS_THERE_MORE";

    BroadcastReceiver getPullBroadcastReceiver;
    Repository repository;
    private boolean isThereMore;
    private PullRequest pullRequest;
    private List<PullRequest> listPullRequest;

    int listViewYScroll;
    int listViewFirstVisible;

    private ListView listView;
    private PullRequestAdapter pullRequestAdapter;
    private ProgressBar progressBar;


    public RepositoryDetailPullFragment() {
        // Required empty public constructor
    }

    public static RepositoryDetailPullFragment newInstance(Repository repository) {
        RepositoryDetailPullFragment fragment = new RepositoryDetailPullFragment();
        Bundle args = new Bundle();
        args.putParcelable(CHOOSEN_REPOSITORY, repository);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            repository = getArguments().getParcelable(CHOOSEN_REPOSITORY);
        }

        listPullRequest = new ArrayList<PullRequest>();
        pullRequest = new PullRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_repository_detail_pull, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.pullfrag_progressbar);
        listView = (ListView) view.findViewById(R.id.pullfrag_listview);
        listView.setOnScrollListener(this);

        getPullBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                ListRepositoryPullAsyncTask.RepositoryPullListAsyncTaskResult result = (ListRepositoryPullAsyncTask.RepositoryPullListAsyncTaskResult) intent.getSerializableExtra(REPOSITORYPULLLIST_ASYNCTASK_RESULT);
                listPullRequest.addAll(result.getPullRequestList());
                isThereMore = result.isThereMore();
                PopulateListView();
                progressBar.setVisibility(View.GONE);
                listView.setSelectionFromTop(listViewFirstVisible, listViewYScroll);
            }
        };

        if(savedInstanceState != null){
            listPullRequest = (List<PullRequest>) savedInstanceState.getSerializable(PULL_LIST);
            if(listPullRequest != null)
                PopulateListView();

            isThereMore = savedInstanceState.getBoolean(IS_THERE_MORE, false);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            listViewFirstVisible = listView.getFirstVisiblePosition();
            View v = listView.getChildAt(0);
            listViewYScroll = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

            new ListRepositoryPullAsyncTask(getContext(), repository.getFullName()).execute("0");
        }

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        LocalBroadcastManager brInstance = LocalBroadcastManager.getInstance(getContext());
        brInstance.registerReceiver((getPullBroadcastReceiver), new IntentFilter(REPOSITORYPULLLIST_ASYNCTASK_RESULT));
    }

    @Override
    public void onStop(){
        LocalBroadcastManager brInstance = LocalBroadcastManager.getInstance(getContext());
        brInstance.unregisterReceiver(getPullBroadcastReceiver);
        super.onStop();
    }

    private void PopulateListView() {
        pullRequestAdapter = new PullRequestAdapter(getContext(), listPullRequest);
        listView.setAdapter(pullRequestAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        if(listPullRequest != null)
            savedInstanceState.putSerializable(PULL_LIST, (Serializable) listPullRequest);

        savedInstanceState.putBoolean(IS_THERE_MORE, isThereMore);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isThereMore){

            if(listView.getLastVisiblePosition() + 1 == listPullRequest.size()) {
                pullRequest.setId(listPullRequest.get(listPullRequest.size() - 1).getId());
                isThereMore = false;

                progressBar.setVisibility(View.VISIBLE);
                listViewFirstVisible = listView.getFirstVisiblePosition();
                View v = listView.getChildAt(0);
                listViewYScroll = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

                new ListRepositoryPullAsyncTask( getContext() , String.valueOf(pullRequest.getId()), repository.getFullName() ).execute("list");
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }
}
