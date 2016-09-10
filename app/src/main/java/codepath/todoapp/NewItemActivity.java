/*
 * Created by Iliiazbek Akhmedov on 9/7/2016
 * Copyright (c) 2016.
 */

package codepath.todoapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import codepath.todoapp.model.ToDoItem;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NewItemActivity extends AppCompatActivity {

    private EditText taskName;
    private EditText notes;
    private Spinner status;
    private Spinner priority;
    private Button dueDate;

    private Calendar calendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private EventHandler eventHandler = new EventHandler();
    private DatePickerDialog datePicker;
    private ToDoItem task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        initViews();
    }

    private void initViews() {
        taskName = (EditText) findViewById(R.id.task_name);
        notes = (EditText) findViewById(R.id.notes);
        status = (Spinner) findViewById(R.id.status);
        priority = (Spinner) findViewById(R.id.priority_level);
        dueDate = (Button) findViewById(R.id.due_date);


        long taskId = getIntent().getLongExtra("taskId", 0);
        task = Realm.getDefaultInstance().where(ToDoItem.class).equalTo("taskId", taskId).findFirst();

        calendar = Calendar.getInstance();
        calendar.setTime(new Date()); //set now...
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        setTitle(R.string.new_task);

        if (null != task) {
            setTitle(task.getTaskName());

            taskName.setText(task.getTaskName());
            notes.setText(task.getNotes());
            status.setSelection(task.getStatus().ordinal());
            priority.setSelection(task.getPriorityLevel().ordinal());
            calendar.setTime(task.getDueDate());
        }

        dueDate.setText(sdf.format(calendar.getTime()));
        dueDate.setOnClickListener(eventHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                return true;
            case R.id.action_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showDatePicker() {
        datePicker = new DatePickerDialog(this, eventHandler,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }

    private void saveItem() {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder(this).build());

        if (taskName.getText().toString().trim().isEmpty()) {
            new AlertDialog.Builder(this).setMessage("You forgot to add task name?")
                    .setPositiveButton("OK", null).show();
            return;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(null == task) {
                    task = realm.createObject(ToDoItem.class);
                    task.setTaskId(new Date().getTime());
                }

                task.setTaskName(taskName.getText().toString().trim());
                task.setDueDate(calendar.getTime());
                task.setNotes(notes.getText().toString());
                task.setStatus(ToDoItem.Status.valueOf((String) status.getSelectedItem()));
                task.setPriorityLevel(ToDoItem.Priority.valueOf((String) priority.getSelectedItem()));
            }
        });

        finish();
    }

    private class EventHandler implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

        @Override
        public void onClick(View v) {
            if (v.equals(dueDate)) {
                showDatePicker();
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (view.equals(datePicker.getDatePicker())) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                dueDate.setText(sdf.format(calendar.getTime()));
            }
        }
    }
}
