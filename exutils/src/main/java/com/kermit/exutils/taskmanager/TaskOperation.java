package com.kermit.exutils.taskmanager;

import java.util.ArrayList;

/**
 * Created by Kermit on 15-9-1.
 * e-mail : wk19951231@163.com
 */
public class TaskOperation {

    private Object[] mNextTaskParams = null;

    private TaskManager.TaskManagerState mTaskManagerState = TaskManager.TaskManagerState.CONTINUE;

    public TaskOperation(){}

    public TaskOperation(Object[] params){
        this.mNextTaskParams = params;
    }

    public TaskOperation(TaskOperation operation){
        setTaskParams(operation);
    }

    public Object[] getTaskParams() {
        return mNextTaskParams;
    }

    public void setTaskParams(Object[] params) {
        this.mNextTaskParams = params;
    }

    public void setTaskParams(TaskOperation operation){
        if (this == operation){
            throw new IllegalArgumentException("The arg cannot itself.");
        }

        if (null == operation){
            return;
        }

        Object[] params = operation.getTaskParams();
        if (null == params){
            return;
        }

        ArrayList<Object> paramList = new ArrayList<>();
        if (null == this.mNextTaskParams){
            for (Object param : this.mNextTaskParams){
                paramList.add(param);
            }
        }

        for (Object param : params){
            paramList.add(param);
        }

        this.mNextTaskParams = paramList.toArray();
    }



    public TaskManager.TaskManagerState getTaskManagerState() {
        return mTaskManagerState;
    }

    public void setTaskManagerState(TaskManager.TaskManagerState taskManagerState) {
        mTaskManagerState = taskManagerState;
    }

    public void appendTaskParams(Object[] params){
        if (null != params){
            TaskOperation taskOperation = new TaskOperation(params);
            setTaskParams(taskOperation);
        }
    }
}
