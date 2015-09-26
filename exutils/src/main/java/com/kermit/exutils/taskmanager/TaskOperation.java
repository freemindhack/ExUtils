package com.kermit.exutils.taskmanager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Kermit on 15-9-1.
 * e-mail : wk19951231@163.com
 */

/**
 * 主要负责任务参数的设置，和任务管理器的状态
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

    /**
     * 设置参数，但会清除之前的参数
     * @param params
     */
    public void setTaskParams(Object[] params) {
        this.mNextTaskParams = params;
    }

    /**
     * 通过设置operation的方式设置参数，将两个operation的值混合
     * @param operation
     */
    public void setTaskParams(TaskOperation operation){
        if (this == operation){
            throw new IllegalArgumentException("The arg cannot set itself.");
        }

        if (null == operation){
            return;
        }

        Object[] params = operation.getTaskParams();
        if (null == params){
            return;
        }

        ArrayList<Object> paramList = new ArrayList<>();
        if (null != this.mNextTaskParams){
            paramList.addAll(Arrays.asList(this.mNextTaskParams));
        }

        paramList.addAll(Arrays.asList(params));

        this.mNextTaskParams = paramList.toArray();
    }



    public TaskManager.TaskManagerState getTaskManagerState() {
        return mTaskManagerState;
    }

    public void setTaskManagerState(TaskManager.TaskManagerState taskManagerState) {
        mTaskManagerState = taskManagerState;
    }


    /**
     * 添加参数，不清楚之前的参数
     * @param params
     */
    public void appendTaskParams(Object[] params){
        if (null != params){
            TaskOperation taskOperation = new TaskOperation(params);
            setTaskParams(taskOperation);
        }
    }
}
