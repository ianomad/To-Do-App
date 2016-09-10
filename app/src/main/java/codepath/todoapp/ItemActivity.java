/*
 * Created by Iliiazbek Akhmedov on 9/8/2016
 * Copyright (c) 2016.
 */

package codepath.todoapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import codepath.todoapp.model.ToDoItem;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

public class ItemActivity extends AppCompatActivity {

    private TextView taskName;
    private TextView notes;
    private TextView status;
    private TextView priority;
    private TextView dueDate;

    private ToDoItem task;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        initViews();
    }

    private void initViews() {
        taskName = (TextView) findViewById(R.id.task_name);
        notes = (TextView) findViewById(R.id.notes);
        status = (TextView) findViewById(R.id.status);
        priority = (TextView) findViewById(R.id.priority_level);
        dueDate = (TextView) findViewById(R.id.due_date);

        if (null != getActionBar()) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setData();
    }

    private void setData() {
        long taskId = getIntent().getLongExtra("taskId", 0);
        task = Realm.getDefaultInstance().where(ToDoItem.class).equalTo("taskId", taskId).findFirst();

        if (null == task) {
            finish();
            return;
        }

        setTitle(task.getTaskName());

        taskName.setText(task.getTaskName());
        notes.setText(task.getNotes());
        status.setText(task.getStatus().toString());
        priority.setText(task.getPriorityLevel().toString());
        dueDate.setText(sdf.format(task.getDueDate()));

        if (task.getPriorityLevel() == ToDoItem.Priority.HIGH) {
            priority.setTextColor(getResources().getColor(R.color.high));
        } else if (task.getPriorityLevel() == ToDoItem.Priority.MEDIUM) {
            priority.setTextColor(getResources().getColor(R.color.medium));
        } else {
            priority.setTextColor(getResources().getColor(R.color.low));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_edit:

                Intent intent = new Intent(this, NewItemActivity.class);
                intent.putExtra("taskId", task.getTaskId());
                startActivity(intent);

                return true;
            case R.id.action_delete:
                deleteTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void deleteTask() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to remove this task?")
                .setNegativeButton("No", null)
                .setPositiveButton("Delete",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        task.deleteFromRealm();
                                    }
                                });

                                finish();
                            }
                        }).show();

    }
}
