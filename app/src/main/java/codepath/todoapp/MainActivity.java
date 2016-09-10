/*
 * Created by Iliiazbek Akhmedov on 9/6/2016
 * Copyright (c) 2016.
 */

package codepath.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import codepath.todoapp.model.ToDoItem;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private TextView emptyLabel;
    private ListView itemsListView;

    private List<ToDoItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                showEditDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initViews() {
        itemsListView = (ListView) findViewById(R.id.items_list_view);
        emptyLabel = (TextView) findViewById(R.id.empty_label);

        loadData();
    }

    private void loadData() {
        items = Realm.getDefaultInstance().where(ToDoItem.class).findAll();

        //not efficient, but have to wrap it for sorting
        items = new ArrayList<>(items);

        // Normally this would happen on the db query, but it is okay for a small data set
        Collections.sort(items, new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem a, ToDoItem b) {
                return a.getPriorityLevel().ordinal() - b.getPriorityLevel().ordinal();
            }
        });

        if (items.isEmpty()) {
            emptyLabel.setVisibility(View.VISIBLE);
            itemsListView.setVisibility(View.GONE);
        } else {
            emptyLabel.setVisibility(View.GONE);
            itemsListView.setVisibility(View.VISIBLE);

            ItemsAdapter adapter = new ItemsAdapter();
            itemsListView.setAdapter(adapter);
        }

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra("taskId", id);
                startActivity(intent);
            }
        });
    }

    private void showEditDialog() {
        startActivity(new Intent(this, NewItemActivity.class));
    }

    private class ItemsAdapter extends BaseAdapter {

        private class ViewHolder {
            TextView taskName;
            TextView notes;
            TextView status;
            TextView dueDate;
            TextView priority;

            ViewHolder() {

            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).getTaskId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View view = convertView;
            ViewHolder holder;

            if (null == view || null == view.getTag()) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                view = inflater.inflate(R.layout.list_item_todoitem, parent, false);

                holder = new ViewHolder();
                holder.taskName = (TextView) view.findViewById(R.id.task_name);
                holder.notes = (TextView) view.findViewById(R.id.notes);
                holder.dueDate = (TextView) view.findViewById(R.id.due_date);
                holder.status = (TextView) view.findViewById(R.id.status);
                holder.priority = (TextView) view.findViewById(R.id.priority_level);

            } else {
                holder = (ViewHolder) view.getTag();
            }

            ToDoItem toDoItem = items.get(position);

            String st = "Status: " + toDoItem.getStatus();
            String dd = "Due Date: " + sdf.format(toDoItem.getDueDate());
            String pr = "Priority: " + toDoItem.getPriorityLevel();

            holder.taskName.setText(toDoItem.getTaskName());
            holder.notes.setText(toDoItem.getNotes());
            holder.dueDate.setText(dd);
            holder.status.setText(st);
            holder.priority.setText(pr);

            if (toDoItem.getPriorityLevel() == ToDoItem.Priority.HIGH) {
                holder.priority.setTextColor(getResources().getColor(R.color.high));
            } else if (toDoItem.getPriorityLevel() == ToDoItem.Priority.MEDIUM) {
                holder.priority.setTextColor(getResources().getColor(R.color.medium));
            } else {
                holder.priority.setTextColor(getResources().getColor(R.color.low));
            }

            return view;
        }
    }
}
