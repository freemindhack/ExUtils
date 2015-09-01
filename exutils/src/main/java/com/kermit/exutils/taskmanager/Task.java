package com.kermit.exutils.taskmanager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kermit on 15-9-1.
 * e-mail : wk19951231@163.com
 */
public abstract class Task {

    private int mTaskId = 0;

    private String mName = null;

    private AtomicBoolean mCancelled = new AtomicBoolean(false);

    private volatile Status mStatus = Status.RUNNING;

    private volatile RunningStatus mRunStatus = RunningStatus.UI_THREAD;

    public enum Status{

        PENDING,

        RUNNING,

        FINISHED,

    }

    public enum RunningStatus{

        WORK_THREAD,

        UI_THREAD,
    }


    public Task(Task task){
        this.mRunStatus = task.mRunStatus;
        this.mName      = task.mName;
        this.mStatus    = task.mStatus;
    }

    public Task(RunningStatus runStatus){
        this(runStatus, null);
    }

    public Task(RunningStatus runStatus, String name){
        mRunStatus = runStatus;
        mName = name;
    }

    public abstract TaskOperation onExecute(TaskOperation operation);

    public void onProgressUpdate(Object progress){}

    public void cancel(){
        mCancelled.set(true);
    }

    public boolean isCancel(){
        return mCancelled.get();
    }

    public int getTaskId() {
        return mTaskId;
    }

    public void setTaskId(int taskId) {
        mTaskId = taskId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public AtomicBoolean getCancelled() {
        return mCancelled;
    }

    public void setCancelled(AtomicBoolean cancelled) {
        mCancelled = cancelled;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public RunningStatus getRunStatus() {
        return mRunStatus;
    }

    public void setRunStatus(RunningStatus runStatus) {
        mRunStatus = runStatus;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("name = ").append(mName).append("  ");
        sb.append("TaskId = ").append(mTaskId).append("  ");
        sb.append(super.toString());

        return sb.toString();
    }

}
