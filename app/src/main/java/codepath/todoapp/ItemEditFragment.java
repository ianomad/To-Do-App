/*
 * Created by Iliiazbek Akhmedov on 9/6/2016
 * Copyright (c) 2016.
 */

package codepath.todoapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class ItemEditFragment extends DialogFragment {

    private EventHandler eventHandler = new EventHandler();

    private Button done;
    private Button cancel;
    private Spinner prioritySpinner;
    private ImageButton pickDateButton;
    private TextView pickDate;

    public ItemEditFragment() {
    }

    public static ItemEditFragment newInstance(String title) {
        ItemEditFragment frag = new ItemEditFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prioritySpinner = (Spinner) view.findViewById(R.id.priority_level);
        done = (Button) view.findViewById(R.id.done);
        cancel = (Button) view.findViewById(R.id.cancel);
        pickDateButton = (ImageButton) view.findViewById(R.id.pick_date);
        pickDate = (TextView) view.findViewById(R.id.due_date);


        done.setOnClickListener(eventHandler);
        cancel.setOnClickListener(eventHandler);
        pickDateButton.setOnClickListener(eventHandler);


//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//        // Show soft keyboard automatically and request focus to field
//        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    private void showDatePicker() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("DEBUG", "Hello, world!");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    private class EventHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.equals(cancel)) {
                dismiss();
                return;
            }

            if (v.equals(pickDateButton)) {
                showDatePicker();
                return;
            }
        }
    }
}