package reaper.app.fragment.event.dialogfragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.ToggleButton;

import reaper.R;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventCategory;
import reaper.app.fragment.event.CreateEventFragment;

/**
 * Created by harsh on 11-05-2015.
 */
public class CreateEventDialogFragment extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch switchType;
    private ImageView general, eatOut, drinks, cafe, movies, outdoors, party, localEvents, shopping;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_create_event, container, false);
        switchType = (Switch) view.findViewById(R.id.sCreateEventDialogFragment);
        general = (ImageView) view.findViewById(R.id.ivGeneralType);
        eatOut = (ImageView) view.findViewById(R.id.ivEatOutType);
        drinks = (ImageView) view.findViewById(R.id.ivDrinksType);
        cafe = (ImageView) view.findViewById(R.id.ivCafeType);
        movies = (ImageView) view.findViewById(R.id.ivMoviesType);
        outdoors = (ImageView) view.findViewById(R.id.ivOutdoorsType);
        party = (ImageView) view.findViewById(R.id.ivPartyType);
        localEvents = (ImageView) view.findViewById(R.id.ivLocalEventsType);
        shopping = (ImageView) view.findViewById(R.id.ivShoppingType);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        general.setOnClickListener(this);
        eatOut.setOnClickListener(this);
        drinks.setOnClickListener(this);
        cafe.setOnClickListener(this);
        movies.setOnClickListener(this);
        outdoors.setOnClickListener(this);
        party.setOnClickListener(this);
        localEvents.setOnClickListener(this);
        shopping.setOnClickListener(this);

        switchType.setOnCheckedChangeListener(this);

        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onClick(View v) {

        Event.Type eventType;

        if(switchType.isChecked()){
             eventType = Event.Type.INVITE_ONLY;
        }else{
            eventType = Event.Type.PUBLIC;
        }

        if(v.getId() == R.id.ivGeneralType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.GENERAL);
        }

        if(v.getId() == R.id.ivCafeType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.CAFE);
        }

        if(v.getId() == R.id.ivDrinksType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.DRINKS);
        }

        if(v.getId() == R.id.ivEatOutType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.EAT_OUT);
        }

        if(v.getId() == R.id.ivMoviesType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.MOVIES);
        }

        if(v.getId() == R.id.ivOutdoorsType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.OUTDOORS);
        }

        if(v.getId() == R.id.ivPartyType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.PARTY);
        }

        if(v.getId() == R.id.ivShoppingType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.SHOPPING);
        }

        if(v.getId() == R.id.ivLocalEventsType){

            dismiss();
            setFragmentInfo(eventType, EventCategory.LOCAL_EVENTS);
        }


    }

    private void setFragmentInfo(Event.Type eventType, EventCategory eventCategory){

        CreateEventFragment createEventFragment = new CreateEventFragment();
        createEventFragment.setInfo(eventType, eventCategory);

        if(fragmentManager == null){
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flMainActivity, createEventFragment, "Create Event");
        fragmentTransaction.commit();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
