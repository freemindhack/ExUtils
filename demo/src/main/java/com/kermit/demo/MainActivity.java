package com.kermit.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kermit.exutils.taskmanager.Task;
import com.kermit.exutils.taskmanager.TaskManager;
import com.kermit.exutils.taskmanager.TaskOperation;
import com.kermit.exutils.utils.LogUtils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private TextView mTextView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView = new TextView(this);
        mTextView.setText("lalalalalal");
        setContentView(mTextView);

        //需要在app类里ExUtils.setDebug(true)，才会打印log
        LogUtils.setTag(TAG);
        LogUtils.i("start");
        LogUtils.i("Hey", "start");

        TaskManager taskManager = new TaskManager("show_textview");

        /**
         * 监听当任务执行的状态发生改变
         */
        taskManager.setStateChangeListener(new TaskManager.IStateChangeListener() {
            @Override
            public void onStateChanged(TaskManager taskManager, TaskManager.State oldSate, TaskManager.State newState) {
                LogUtils.i("oldState: " + oldSate + "  " + "newState: " + newState);
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
                cancel();

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
