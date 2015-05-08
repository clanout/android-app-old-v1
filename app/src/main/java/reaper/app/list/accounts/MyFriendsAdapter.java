package reaper.app.list.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import reaper.R;
import reaper.api.model.user.User;

/**
 * Created by Aditya on 07-05-2015.
 */
public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.MyFriendsViewHolder>
{
    private LayoutInflater inflater;
    private List<User> friendsList = Collections.emptyList();
    private Context context;
    private FriendListItemClickListener friendListItemClickListener;

    public MyFriendsAdapter(Context context, List<User> friendsList){
        inflater = LayoutInflater.from(context);
        this.friendsList = friendsList;
        this.context = context;
    }

    @Override
    public MyFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.list_item_my_friends, parent, false);
        MyFriendsViewHolder holder = new MyFriendsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyFriendsViewHolder holder, int position)
    {
        User current = friendsList.get(position);

        holder.username.setText(current.getFirstname() + " " + current.getLastname());
        holder.userPic.setImageResource(R.drawable.ic_local_bar_black_48dp);
        holder.blockStatus.setImageResource(R.drawable.ic_check_circle_black_24dp);

    }

    @Override
    public int getItemCount()
    {
        return friendsList.size();
    }

    public void setFriendListItemClickListener(FriendListItemClickListener friendListItemClickListener){
        this.friendListItemClickListener = friendListItemClickListener;
    }

    class MyFriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userPic, blockStatus;
        private TextView username;

        public MyFriendsViewHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);
            username = (TextView) itemView.findViewById(R.id.tvMyFriendsListItemUserName);
            userPic = (ImageView) itemView.findViewById(R.id.ivMyFriendsListItemUserPic);
            blockStatus = (ImageView) itemView.findViewById(R.id.ivMyFriendsListBlockStatus);
        }

        @Override
        public void onClick(View v) {

            if(friendListItemClickListener!=null){
                friendListItemClickListener.onFriendListItemClicked(v, getPosition());
            }
        }
    }

    public interface FriendListItemClickListener{
        public void onFriendListItemClicked(View view, int position);
    }
}
