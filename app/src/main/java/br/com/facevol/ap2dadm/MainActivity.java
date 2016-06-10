package br.com.facevol.ap2dadm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    protected SQLiteDatabase db;
    protected String tb = "tasks";

    protected EditText etDescription;
    protected Button btnSave;
    protected RecyclerView rvTodoList;
    protected TaskAdapter adapter;

    protected List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = (EditText) findViewById(R.id.etDescription);
        btnSave = (Button) findViewById(R.id.btnSave);
        rvTodoList = (RecyclerView) findViewById(R.id.rvTodoList);

        // save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Favor, preencha o campo descrição!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (taskList.size() > 0) {
                        insertTask(new Task(taskList.size() + 1, description));
                    } else {
                        insertTask(new Task(1, description));
                    }
                }
            }
        });

        // list
        taskList = new ArrayList<>();
        try {
            databaseMethods();
            Cursor cursor = getCursorFromQuery("SELECT * FROM " + tb + " ORDER BY id DESC");
            while (cursor.moveToNext()) {
                taskList.add(new Task(cursor.getInt(0), cursor.getString(1)));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new TaskAdapter(db, taskList);
        LinearLayoutManager lnmVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTodoList.setLayoutManager(lnmVertical);
        rvTodoList.setAdapter(adapter);
    }

    private void insertTask(Task task) {
        addItem(task);
    }

    private void addItem(Task task) {
        try {
            db.execSQL(String.format("INSERT INTO " + tb + " (id, description) VALUES (%s, '%s');", task.getId(), task.getDescription()));
            adapter.addItem(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void databaseMethods() {
        db = openOrCreateDatabase("TodoList", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tb + " (id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR)");
    }

    private Cursor getCursorFromQuery(String query) {
        return db.rawQuery(query, null);
    }
}
