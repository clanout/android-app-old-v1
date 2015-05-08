package reaper.app.list.accounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import reaper.R;

/**
 * Created by Aditya on 07-05-2015.
 */
public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.MyFriendsViewHolder>
{
    private LayoutInflater inflater;

    public MyFriendsAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        inflater.inflate(R.layout.list_item_my_friends, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(MyFriendsViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    class MyFriendsViewHolder extends RecyclerView.ViewHolder
    {
        public MyFriendsViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
