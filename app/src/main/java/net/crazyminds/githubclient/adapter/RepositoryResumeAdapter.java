package net.crazyminds.githubclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.crazyminds.githubclient.R;
import net.crazyminds.githubclient.domain.RepositoryResume;

import java.util.List;

/**
 * Created by julio on 09/01/2017.
 */

public class RepositoryResumeAdapter extends ArrayAdapter<RepositoryResume> {

    private List<RepositoryResume> list;
    private LayoutInflater inflater;


    // INNER CLASS
    public static class ViewHolder
    {
        TextView id;
        TextView name;
        TextView ownerName;
    }

    public RepositoryResumeAdapter(Context ctx, List<RepositoryResume> listparam){
        super(ctx, 0 , listparam);
        list = listparam;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        RepositoryResume repo = list.get(position);
        inflater = LayoutInflater.from(getContext());

        if(convertView == null){

            convertView = inflater.inflate(R.layout.repositoryresume, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.id = (TextView) convertView.findViewById(R.id.repositoryresume_id);
            holder.name = (TextView) convertView.findViewById(R.id.repositoryresume_projectname);
            holder.ownerName = (TextView) convertView.findViewById(R.id.repositoryresume_ownername);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id.setText(repo.getId()+"");
        holder.name.setText(repo.getName());
        holder.ownerName.setText(repo.getOwnerName());

        return convertView;
    }
}
