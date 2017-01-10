package net.crazyminds.githubclient;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.crazyminds.githubclient.domain.Repository;

import static net.crazyminds.githubclient.RepositoryDetailActivity.CHOOSEN_REPOSITORY;


/**
 * Created by julio on 10/01/2017.
 */

public class RepositoryDetailInfoFragment extends android.support.v4.app.Fragment {

    Repository repository;

    //UI
    TextView Id;
    TextView Name;
    TextView FullName;
    TextView OwnerName;
    TextView Description;
    TextView Language;
    TextView HomePage;


    public RepositoryDetailInfoFragment() {
        // Required empty public constructor
    }

    public static RepositoryDetailInfoFragment newInstance(Repository repository) {
        RepositoryDetailInfoFragment fragment = new RepositoryDetailInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_repository_detail_info, container, false);

        Id = (TextView)view.findViewById(R.id.repositorydetail_id_value);
        Name = (TextView)view.findViewById(R.id.repositorydetail_name_value);
        FullName = (TextView)view.findViewById(R.id.repositorydetail_fullname_value);
        OwnerName = (TextView)view.findViewById(R.id.repositorydetail_ownername_value);
        Description = (TextView)view.findViewById(R.id.repositorydetail_description_value);
        Language = (TextView)view.findViewById(R.id.repositorydetail_language_value);
        HomePage = (TextView)view.findViewById(R.id.repositorydetail_homepage_value);

        Id.setText( getString( R.string.blank, repository.getId()));
        Name.setText( repository.getName());
        FullName.setText( repository.getFullName() );
        OwnerName.setText( repository.getOwnerName());
        Description.setText( repository.getDescription());
        Language.setText( repository.getLanguage() );
        HomePage.setText( repository.getHomePage());

        return view ;
    }

}
