package net.crazyminds.githubclient;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.crazyminds.githubclient.domain.Repository;

import java.util.Set;

import static net.crazyminds.githubclient.MainActivity.REPOSITORY_DETAILL;

public class RepositoryDetailActivity extends AppCompatActivity {

    Repository repository;

    //UI
    TextView Id;
    TextView Name;
    TextView FullName;
    TextView OwnerName;
    TextView Description;
    TextView Language;
    TextView HomePage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_detail);

        Id = (TextView)findViewById(R.id.repositorydetail_id_value);
        Name = (TextView)findViewById(R.id.repositorydetail_name_value);
        FullName = (TextView)findViewById(R.id.repositorydetail_fullname_value);
        OwnerName = (TextView)findViewById(R.id.repositorydetail_ownername_value);
        Description = (TextView)findViewById(R.id.repositorydetail_description_value);
        Language = (TextView)findViewById(R.id.repositorydetail_language_value);
        HomePage = (TextView)findViewById(R.id.repositorydetail_homepage_value);

        Intent intent = getIntent();

        if(intent != null && intent.getParcelableExtra(REPOSITORY_DETAILL) != null){
            repository = (Repository) getIntent().getParcelableExtra(REPOSITORY_DETAILL);

            Id.setText( getString( R.string.blank, repository.getId()));
            Name.setText( repository.getName());
            FullName.setText( repository.getFullName() );
            OwnerName.setText( repository.getOwnerName());
            Description.setText( repository.getDescription());
            Language.setText( repository.getLanguage() );
            HomePage.setText( repository.getHomePage());

            setTitle(repository.getFullName() );
        }
        else{
            Toast.makeText(this, "Fail! Try again", Toast.LENGTH_SHORT).show();
        }

    }
}
