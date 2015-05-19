package reaper.app.fragment.event.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import reaper.R;
import reaper.api.model.event.Event;
import reaper.api.model.event.EventCategory;
import reaper.app.fragment.event.EventCategoryTypeCommunicator;

/**
 * Created by harsh on 14-05-2015.
 */
public class ChangeEventCategoryTypeDialogFragment extends DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch switchType;
    private ImageView general, eatOut, drinks, cafe, movies, outdoors, party, localEvents, shopping;
    private EventCategoryTypeCommunicator eventCategoryTypeCommunicator;
    private EventCategory oldEventCategory;
    private Event.Type oldEventType;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_change_event_category_type, container, false);
        switchType = (Switch) view.findViewById(R.id.sChangeEventCategoryTypeDialogFragment);
        general = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeGeneral);
        eatOut = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeEatOut);
        drinks = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeDrinks);
        cafe = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeCafe);
        movies = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeMovies);
        outdoors = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeOutdoors);
        party = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeMovies);
        localEvents = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeLocalEvents);
        shopping = (ImageView) view.findViewById(R.id.ivChangeEventCategoryTypeShopping);
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

        if(oldEventType == Event.Type.PUBLIC){
            switchType.setChecked(false);
        }else{
            switchType.setChecked(true);
        }

        switchType.setOnCheckedChangeListener(this);

    }

    public void setEventCategoryTypeCommunicator(EventCategoryTypeCommunicator eventCategoryTypeCommunicator){
        this.eventCategoryTypeCommunicator = eventCategoryTypeCommunicator;
    }

    public void setOldCategoryType(Event.Type oldEventType, EventCategory oldEventCategory){
        this.oldEventCategory = oldEventCategory;
        this.oldEventType = oldEventType;
    }

    @Override
    public void onClick(View v) {

        Event.Type eventType;

        if(switchType.isChecked()){
            eventType = Event.Type.INVITE_ONLY;
        }else{
            eventType = Event.Type.PUBLIC;
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeGeneral){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.GENERAL);
            dismiss();
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeCafe){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.CAFE);
            dismiss();
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeDrinks){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.DRINKS);
            dismiss();
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeEatOut){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.EAT_OUT);
            dismiss();
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeMovies){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.MOVIES);
            dismiss();
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeOutdoors){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.OUTDOORS);
            dismiss();
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeParty){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.PARTY);
            dismiss();
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeShopping){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.SHOPPING);
            dismiss();
        }

        if(v.getId() == R.id.ivChangeEventCategoryTypeLocalEvents){

            eventCategoryTypeCommunicator.onEventCategoryTypeChanged(eventType, EventCategory.LOCAL_EVENTS);
            dismiss();
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
