package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.services.impl.ThreadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

@Menu
public class TaskMenuProvider implements MenuProvider {

    @Autowired
    private ThreadServiceImpl taskExecutor;

    private static int totalCount = 0;

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Taks process");
    }

    @Override
    public Component getView() {
        Window window = new Window();
        Label label = new Label();
        window.appendChild(label);
        Timer timer = new Timer();
        Progressmeter progressmeter = new Progressmeter();
        label.setValue("Task at " + getProcent() + "%");
        timer.addEventListener(Events.ON_TIMER, e -> {
            long procent = getProcent();
            label.setValue("Task at " + procent + "% - " + (taskExecutor.getCompletedTaskCount() - totalCount) + "/" + (taskExecutor.getTaskCount() - totalCount) + " -- " + taskExecutor.getCurrent());
            progressmeter.setValue((int) (procent < 0 ? 0 : procent));
        });
        progressmeter.setWidth("100%");
        window.appendChild(timer);
        window.appendChild(progressmeter);
        timer.start();
        timer.setDelay(1000);
        timer.setRepeats(true);
        return window;
    }

    private long getProcent() {
        long taskCount = taskExecutor.getTaskCount();
        long completedTaskCount = taskExecutor.getCompletedTaskCount();
        int Qusize = taskExecutor.getQueueSize();
        long realTotal = taskCount - totalCount;
        long l = ((completedTaskCount - totalCount) * 100) / (realTotal <= 0L ? 1 : realTotal);
        if (l == 100 && Qusize == 0) {
            totalCount += taskCount;
        }
        return taskCount > 0 ? l : 0;
    }

    @Override
    public String getIcon() {
        return "fa fa-spinner spin";
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
