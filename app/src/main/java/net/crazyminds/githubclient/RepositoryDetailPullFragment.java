package net.crazyminds.githubclient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.crazyminds.githubclient.domain.Repository;

import static net.crazyminds.githubclient.RepositoryDetailActivity.CHOOSEN_REPOSITORY;


/**
 * A simple {@link Fragment} subclass.
 */
public class RepositoryDetailPullFragment extends android.support.v4.app.Fragment {

    Repository repository;

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
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            repository = getArguments().getParcelable(CHOOSEN_REPOSITORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repository_detail_pull, container, false);
    }

}
