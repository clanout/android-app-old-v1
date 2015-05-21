package reaper.app.fragment.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import reaper.R;
import reaper.api.model.chat.ChatMessage;
import reaper.app.activity.MainActivity;
import reaper.app.list.chat.ChatAdapter;

/**
 * Created by harsh on 21-05-2015.
 */
public class ChatFragment extends Fragment implements View.OnClickListener {

    private EditText typeMessage;
    private Button send;
    private ListView listView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessageList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        typeMessage = (EditText) view.findViewById(R.id.etTypeMessageChat);
        send = (Button) view.findViewById(R.id.bSendChat);
        listView = (ListView) view.findViewById(R.id.lvChat);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadDummyHistory();
        send.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbAccounts).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbCreateEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbHome).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbEditEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbSearch).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbFinaliseEvent).setVisible(false);
            ((MainActivity) getActivity()).getMenu().findItem(R.id.abbDeleteEvent).setVisible(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setTitle("Chat");
    }

    public void displayMessage(ChatMessage message) {
        chatAdapter.add(message);
        chatAdapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        listView.setSelection(listView.getCount() - 1);
    }

    private void loadDummyHistory(){

        chatMessageList = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setSenderName("Aditya");
        chatMessageList.add(msg);

        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
        msg1.setSenderName("Aditya");
        chatMessageList.add(msg1);

        chatAdapter = new ChatAdapter(new ArrayList<ChatMessage>(), getActivity());
        listView.setAdapter(chatAdapter);

        for(int i=0; i<chatMessageList.size(); i++) {
            ChatMessage message = chatMessageList.get(i);
            displayMessage(message);
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.bSendChat){

            String message = typeMessage.getText().toString();
            if(TextUtils.isEmpty(message)){
                return;
            }

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(42);
            chatMessage.setMessage(message);
            chatMessage.setSenderName("Harsh");
            chatMessage.setMe(true);

            typeMessage.setText("");
            displayMessage(chatMessage);
        }
    }
}
