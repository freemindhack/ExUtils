package com.kermit.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.kermit.exutils.taskmanager.Task;
import com.kermit.exutils.taskmanager.TaskManager;
import com.kermit.exutils.taskmanager.TaskOperation;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView = new TextView(this);
        mTextView.setText("lalalalalal");
        setContentView(mTextView);

        TaskManager taskManager = new TaskManager("show_textview");
        taskManager.setStateChangeListener(new TaskManager.IStateChangeListener() {
            @Override
            public void onStateChanged(TaskManager taskManager, TaskManager.State oldSate, TaskManager.State newState) {
                Toast.makeText(MainActivity.this, " onStateChanged state = " + newState, Toast.LENGTH_SHORT).show();
            }
        });

        taskManager.next(new Task(Task.RunningStatus.WORK_THREAD) {
            @Override
            public TaskOperation onExecute(TaskOperation operation) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }
        })
                .next(new Task(Task.RunningStatus.UI_THREAD) {
                    @Override
                    public TaskOperation onExecute(TaskOperation operation) {

                        mTextView.setText("ok!");

                        return null;
                    }
                }).execute();
    }

}
