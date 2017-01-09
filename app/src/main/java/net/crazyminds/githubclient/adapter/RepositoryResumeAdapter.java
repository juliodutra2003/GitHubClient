package net.crazyminds.githubclient.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import net.crazyminds.githubclient.domain.RepositoryResume;

import java.util.List;

/**
 * Created by julio on 09/01/2017.
 */

public class RepositoryResumeAdapter extends BaseAdapter {

    private List<RepositoryResume> list;
    private LayoutInflater inflater;

    public RepositoryResumeAdapter(Context c, List<RepositoryResume> l){
        list = l;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
