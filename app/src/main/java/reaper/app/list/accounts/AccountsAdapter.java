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

/**
 * Created by Aditya on 07-04-2015.
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>
{
    private LayoutInflater inflater;
    List<AccountsListItem> data = Collections.emptyList();
    private ClickListener clickListener;

    public AccountsAdapter(Context context, List<AccountsListItem> data)
    {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.custom_row_accounts, parent, false);
        AccountViewHolder accountViewHolder = new AccountViewHolder(view);
        return accountViewHolder;
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position)
    {
        AccountsListItem current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
    }

    public void setListener(AccountsAdapter.ClickListener clickListener){
        this.clickListener = clickListener;
    }
    
    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView icon;
        TextView title;

        public AccountViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            icon = (ImageView) itemView.findViewById(R.id.ivCustomRowAccountsIcon);
            title = (TextView) itemView.findViewById(R.id.tvCustomRowAccountTitle);
        }

        @Override
        public void onClick(View view)
        {
            if(clickListener != null){
                clickListener.itemClicked(view, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}
