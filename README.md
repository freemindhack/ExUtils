### 常用工具类

- 搜集整理了一些常用工具类

#### 使用方法

- 在模块的gradle中添加
```'compile com.kermit:exutils:1.0.7'```

- 在需要使用的activity中，初始化；如果要全局使用，则创建如下类，并初始化

        public class App extends Application {
            @Override
            public void onCreate() {
                super.onCreate();
                ExUtils.initialize(this);
            }
        }


#### 内容

- utils类
- TaskManager
>是对handler进行的一个封装，思路来自以前看过的一个博客，将原始代码中提供Looper的线程替换成了HandlerThread。

        //构造器中指定task是在后台线程还是UI线程执行
        taskManager.next(new Task(Task.RunningStatus.WORK_THREAD) {
            //operation参数用来存储数据，并传递给下一个task
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
                    //operation来自上一个task
                    public TaskOperation onExecute(TaskOperation operation) {

                        mTextView.setText("ok!");

                        Toast.makeText(MainActivity.this, (String)operation.getTaskParams()[0], Toast.LENGTH_SHORT).show();

                        return null;
                    }
                }).execute();
