package net.crazyminds.githubclient;

import android.content.Intent;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            new Runnable() {
                @Override
                public void run() {
                    GitHub github = null;
                    try {
                        github = GitHub.connect();
                        PagedIterable<GHRepository> ghrep = github.listAllPublicRepositories();
                        PopulateListView(ghrep);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };
            //GHRepository rep = github.getRepository("teste");
            //rep.listCommits()
    }

    void PopulateListView( PagedIterable<GHRepository> ghrepository )
    {
        if (ghrepository != null && ghrepository.asList().size() > 0)
        {
            Log.d("main" , "PopulateListView - " + ghrepository.asList().size() + " repositories found");

            for (GHRepository r : ghrepository) {
                listRepositoryResume.add(new RepositoryResume (r.getId(), r.getName(), r.getOwnerName()  ));
            }

            if(repositoryResumeAdapter == null){
                repositoryResumeAdapter = new RepositoryResumeAdapter(MainActivity.this, listRepositoryResume);
                listView.setAdapter(repositoryResumeAdapter);
            }
            else{
                repositoryResumeAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            //TODO - has internet?
            Log.d("main" , "PopulateListView - ERROR - no repository got");
        }

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("main" , "onScrollStateChanged " + scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("main" , "onItemClick " + position);
    }
}
