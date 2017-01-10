package net.crazyminds.githubclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.crazyminds.githubclient.domain.Repository;

import static net.crazyminds.githubclient.MainActivity.REPOSITORY_DETAILL;

public class RepositoryDetailActivity extends AppCompatActivity {

    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_detail);

        if(getIntent() != null && getIntent().getSerializableExtra(REPOSITORY_DETAILL) != null){
            repository = (Repository) getIntent().getSerializableExtra(REPOSITORY_DETAILL);
           // TextView tvDescription = (TextView) findViewById(R.id.tvDescription);

            //tvDescription.setText(Html.fromHtml(car.toString()));

        }
        else{
            Toast.makeText(this, "Fail! Try again", Toast.LENGTH_SHORT).show();
        }

        setTitle(repository.getFullName() );
    }
}
