package yuziouo.ReportSystem;

import cn.nukkit.scheduler.Task;

import java.util.Map;

public class RemoveEmptyTask extends Task {
    Main main;
    public RemoveEmptyTask(Main main){
        this.main = main;
    }
    @Override
    public void onRun(int i) {
       for (String key:main.list.keySet()){
          if (main.list.get(key).isEmpty()){
              main.list.remove(key);
          }
       }
    }
}
