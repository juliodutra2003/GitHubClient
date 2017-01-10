package net.crazyminds.githubclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.crazyminds.githubclient.R;
import net.crazyminds.githubclient.domain.PullRequest;
import net.crazyminds.githubclient.domain.RepositoryResume;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by julio on 10/01/2017.
 */

public class PullRequestAdapter extends ArrayAdapter<PullRequest> {

private List<PullRequest> list;
private LayoutInflater inflater;


// INNER CLASS
public static class ViewHolder
{
    TextView id;
    TextView creationDate;
    TextView title;
    TextView ownerName;
}

    public PullRequestAdapter(Context ctx, List<PullRequest> listparam){
        super(ctx, 0 , listparam);
        list = listparam;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        PullRequest pull = list.get(position);
        inflater = LayoutInflater.from(getContext());

        if(convertView == null){

            convertView = inflater.inflate(R.layout.pull, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.id = (TextView) convertView.findViewById(R.id.pull_id);
            holder.creationDate = (TextView) convertView.findViewById(R.id.pull_date);
            holder.title = (TextView) convertView.findViewById(R.id.pull_title);
            holder.ownerName = (TextView) convertView.findViewById(R.id.pull_ownername);

        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id.setText(String.valueOf( pull.getId()));

        Date date = new Date(Long.parseLong(pull.getCreationDate()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        holder.creationDate.setText(today);

        holder.title.setText(pull.getTitle());
        holder.ownerName.setText(pull.getOwnerName());

        return convertView;
    }
}
