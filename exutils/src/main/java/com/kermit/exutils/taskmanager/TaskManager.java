package com.kermit.exutils.taskmanager;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Kermit on 15-9-1.
 * e-mail : wk19951231@163.com
 */
public class TaskManager {

    private static final int MESSAGE_POST_EXECUTE = 0x01;
    private static final int MESSAGE_POST_PROGRESS = 0x02;


    public enum State{

        NEW,

        RUNNING,

        PAUSED,

        FINISHED,
    }

    public enum TaskManagerState{

        CONTINUE,

        PAUSE,
    }

    public interface IStateChangeListener{
        void onStateChanged(TaskManager taskManager, State oldSate, State newState);
    }

    private static HashMap<String, TaskManager> managerHashMap = new HashMap<>();

    private LinkedList<Task> mTaskList = new LinkedList<>();

    private TaskOperation mTaskOperation = new TaskOperation();

    private HandlerThread mHandlerThread = null;

    private Task curTask = null;

    private State mState = State.NEW;

    private String mName = null;

    private IStateChangeListener mListener = null;

    private Handler threadHandler = null;

    private Handler uiHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_POST_EXECUTE:
                    Task task = (Task) msg.obj;
                    executeTask(task);
                    runNextTask();
                    break;
                case MESSAGE_POST_PROGRESS:
                    postProgress(msg.obj);
                    break;
            }
        }
    };

    public TaskManager(){}

    public TaskManager(String name){
        mName = name;
    }

    public TaskManager next(Task task){
        if (null != task){
            synchronized (mTaskList){
                task.setTaskId(mTaskList.size() + 1);
                mTaskList.add(task);
            }
        }else{
            throw new NullPointerException("task is null.");
        }

        return this;
    }

    public void execute(){
        if (mTaskList.size() > 0){
            startThread();
            setState(State.RUNNING);

            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    doInBackground();
                }
            });
        }else{
            quitLooper();
        }
    }

    public void execute(TaskOperation operation){
        if (null != operation){
            this.mTaskOperation = operation;
        }
        execute();
    }

    public void postExecute(final Task task){
        if (null == task){
            throw new NullPointerException("task is null.");
        }
        final Task runTask = task;

        if (Task.RunningStatus.UI_THREAD == runTask.getRunStatus()){
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    executeTask(runTask);
                }
            });
        }
    }

    public void publishProgress(Object progress){
        uiHandler.obtainMessage(MESSAGE_POST_PROGRESS, progress).sendToTarget();
    }

    public void cancelCurrentTask(){
        if (null != curTask){
            curTask.cancel();
        }
    }

    public void removeTasks(){
        synchronized(mTaskList){
            if (mTaskList.size() > 0){
                mTaskList.clear();
                quitLooper();
            }
        }
    }

    public void setStateChangeListener(IStateChangeListener listener){
        this.mListener = listener;
    }

    public TaskOperation getTaskOperation(){
        return mTaskOperation;
    }

    public String getName(){
        return mName;
    }

    private void quitLooper() {
        if (null != mHandlerThread){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mHandlerThread.quitSafely();
            }else{
                mHandlerThread.quit();
            }
            mHandlerThread = null;
        }
        threadHandler = null;

        setState(State.FINISHED);
    }

    public State getState(){
        return mState;
    }

    private void setState(State state){
        final State oldState = mState;
        final State newState = state;

        if (mState == State.FINISHED){
            popTaskManager(this);
        }else{
            pushTaskManager(this);
        }

        if (oldState != newState){
            printTaskManagre(oldState, newState);
            performStateChange(oldState, newState);
        }
    }

    private void performStateChange(final State oldState, final State newState) {
        if (null != mListener){
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onStateChanged(TaskManager.this, oldState, newState);
                }
            });
        }
    }

    private void printTaskManagre(final State oldState, final State newState) {
        Log.d("TaskManager", "TaskManager state changed, task manager = " + this.toString());
    }

    private void pushTaskManager(TaskManager taskManager) {
        if (null != taskManager){
            String name = taskManager.getName();
            if (!TextUtils.isEmpty(name)) {
                managerHashMap.put(name, taskManager);
            }
        }
    }

    private void popTaskManager(TaskManager taskManager) {
        if (null != taskManager){
            String name = taskManager.getName();
            if (!TextUtils.isEmpty(name)){
                managerHashMap.remove(name);
            }
        }
    }

    public static HashMap<String, TaskManager> getTaskManagers(){
        return managerHashMap;
    }



    private void doInBackground() {
        curTask = null;

        if (mTaskList.isEmpty()){
            return;
        }
        Task task = mTaskList.get(0);
        curTask = task;

        synchronized (mTaskList){
            mTaskList.remove(0);
        }

        switch (task.getRunStatus()){
            case WORK_THREAD:
                executeTask(task);
                runNextTask();
                break;
            case UI_THREAD:
                uiHandler.obtainMessage(MESSAGE_POST_EXECUTE, task).sendToTarget();
                break;
        }
    }

    private void startThread() {
        if (null == mHandlerThread){
            String name = TextUtils.isEmpty(mName) ? this.toString() : mName;
            String threadName = "TaskManger_thread_" + name;
            mHandlerThread = new HandlerThread(threadName);
            mHandlerThread.start();
            threadHandler = new Handler(mHandlerThread.getLooper());
        }
    }

    private void postProgress(Object obj) {
        if (null != curTask){
            curTask.onProgressUpdate(obj);
        }
    }

    private void runNextTask() {
        if (isRunNext()){
            execute();
        }
    }

    private boolean isRunNext(){

        boolean isRunNext = true;
        boolean hasNext = false;

        if (null != mTaskOperation){
            isRunNext = (mTaskOperation.getTaskManagerState() == TaskManagerState.CONTINUE);
        }

        if (null != mTaskList){
            hasNext = mTaskList.size() > 0;
        }

        if (!hasNext){
            quitLooper();
        }

        return (isRunNext && hasNext);
    }

    private void executeTask(Task task) {

        if (null != task){
            task.setStatus(Task.Status.RUNNING);
            this.printExecuteTaskState(task);
        }

        try {
            mTaskOperation = task.onExecute(mTaskOperation);
        }catch (Exception e){
            e.printStackTrace();
        }

        task.setStatus(Task.Status.FINISHED);

        this.printExecuteTaskState(task);
    }

    protected void printExecuteTaskState(Task task)
    {
        Log.d("TaskManager", "    Executer the task: " + task.toString());
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Name = ").append(mName).append("  ");
        sb.append("State = ").append(mState).append("  ");
        sb.append(super.toString());

        return sb.toString();
    }

}
