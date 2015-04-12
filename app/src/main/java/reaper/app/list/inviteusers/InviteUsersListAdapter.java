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

import reaper.R;
import reaper.api.model.user.User;

/**
 * Created by Aditya on 06-04-2015.
 */
public class InviteUsersListAdapter extends RecyclerView.Adapter<InviteUsersListAdapter.InviteUsersViewHolder>
{
    private LayoutInflater inflater;
    List<User> data = Collections.emptyList();
    private ClickListener clickListener;

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
        holder.username.setText(current.getFirstname() + " " + current.getLastname());
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class InviteUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView userPic;
        TextView username;
        CheckBox checkBox;

        public InviteUsersViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            userPic = (ImageView) itemView.findViewById(R.id.ivCustomRowInviteUsersPic);
            username = (TextView) itemView.findViewById(R.id.tvcustomrowInviteUsersName);
            checkBox = (CheckBox) itemView.findViewById(R.id.cbCustomRowinviteUsersheckBox);
        }

        @Override
        public void onClick(View view)
        {
            if(clickListener!=null){
                clickListener.itemClicked(view, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}
