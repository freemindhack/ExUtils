package com.kermit.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kermit.exutils.taskmanager.Task;
import com.kermit.exutils.taskmanager.TaskManager;
import com.kermit.exutils.taskmanager.TaskOperation;
import com.kermit.exutils.utils.ExUtils;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView = new TextView(this);
        mTextView.setText("lalalalalal");
        setContentView(mTextView);
        ExUtils.Toast("sdfasdf");

        TaskManager taskManager = new TaskManager("show_textview");
        taskManager.setStateChangeListener(new TaskManager.IStateChangeListener() {
            @Override
            public void onStateChanged(TaskManager taskManager, TaskManager.State oldSate, TaskManager.State newState) {
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

                operation.setTaskParams(new String[]{"hello?"});

                return operation;
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
