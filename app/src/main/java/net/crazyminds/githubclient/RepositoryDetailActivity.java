package net.crazyminds.githubclient;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import net.crazyminds.githubclient.domain.Repository;

import static net.crazyminds.githubclient.MainActivity.REPOSITORY_DETAILL;

/**
 * Created by julio on 10/01/2017.
 */

public class RepositoryDetailActivity extends AppCompatActivity {
    public static final String CHOOSEN_REPOSITORY = "CHOOSEN_REPOSITORY";
    Repository repository;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_detail);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);


        Intent intent = getIntent();
        if(intent != null && intent.getParcelableExtra(REPOSITORY_DETAILL) != null){
            repository = (Repository) getIntent().getParcelableExtra(REPOSITORY_DETAILL);
            setTitle(repository.getFullName() );
        }
        else{
            Toast.makeText(this, "Fail! Try again", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return RepositoryDetailInfoFragment.newInstance(repository);
            }
            else{
                return RepositoryDetailPullFragment.newInstance(repository);
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Info";
                case 1:
                    return "Pulls";
            }
            return null;
        }
    }
}
