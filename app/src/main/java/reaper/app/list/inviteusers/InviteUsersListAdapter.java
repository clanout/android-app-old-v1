package reaper.app.list.inviteusers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import reaper.api.model.user.User;
import reaper.local.reaper.R;

/**
 * Created by Aditya on 06-04-2015.
 */
public class InviteUsersListAdapter extends RecyclerView.Adapter<InviteUsersListAdapter.InviteUsersViewHolder>
{
    private LayoutInflater inflater;
    List<User> data = Collections.emptyList();

    public InviteUsersListAdapter(Context context, List<User> data)
    {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public InviteUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.custom_row_invite_users, parent, false);
        InviteUsersViewHolder holder = new InviteUsersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(InviteUsersViewHolder holder, int position)
    {
        User current = data.get(position);
        holder.userPic.setImageResource(current.getProfilePic());
        holder.username.setText(current.getFirstName() + " " + current.getLastname());
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class InviteUsersViewHolder extends RecyclerView.ViewHolder
    {
        ImageView userPic;
        TextView username;
        CheckBox checkBox;

        public InviteUsersViewHolder(View itemView)
        {
            super(itemView);
            userPic = (ImageView) itemView.findViewById(R.id.ivCustomRowInviteUsersPic);
            username = (TextView) itemView.findViewById(R.id.tvcustomrowInviteUsersName);
            checkBox = (CheckBox) itemView.findViewById(R.id.cbCustomRowinviteUsersheckBox);
        }
    }
}
