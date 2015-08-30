### 各种开发工具

- 部分参考自https://github.com/Jude95/Utils
- 搜集整理了一些常用工具类

### 使用方法

- 在模块的gradle中添加
```'compile com.kermit:exutils:1.0.1'```

- 创建如下类，并初始化工具类
```
    public class App extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            ExUtils.initialize(this);
        }
    }
```