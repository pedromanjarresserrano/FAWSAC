package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import java.util.concurrent.ThreadPoolExecutor;

@Menu
public class TaskMenuProvider implements MenuProvider {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("usuarios");
    }

    @Override
    public Component getView() {
        Window window = new Window();
        Label label = new Label();
        window.appendChild(label);
        Timer timer = new Timer();
        timer.setDelay(2000);
        timer.setRepeats(true);
        long l = getProcent();
        label.setValue("Task at " + l + "%");
        timer.addEventListener(Events.ON_TIMER, e -> label.setValue("Task at " + getProcent() + "%"));
        timer.start();
        return window;
    }

    private long getProcent() {
        ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
        return threadPoolExecutor.getCompletedTaskCount() * 100 / threadPoolExecutor.getTaskCount();
    }

    @Override
    public String getIcon() {
        return "fa fa-users";
    }

    @Override
    public int getPosition() {
        return 2;
    }

    @Override
    public String getGroup() {
        return "administracion";
    }
}
