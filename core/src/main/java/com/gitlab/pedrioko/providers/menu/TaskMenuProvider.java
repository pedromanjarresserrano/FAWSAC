package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.providers.AdminGroupProvider;
import com.gitlab.pedrioko.services.impl.ThreadServiceImpl;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

@Menu
public class TaskMenuProvider implements MenuProvider {

    Page page = new Page("");

    private final ThreadServiceImpl taskExecutor;

    private static int totalCount = 0;

    public TaskMenuProvider(ThreadServiceImpl taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Taks process");
    }

    @Override
    public Page getView() {
        Window window = new Window();
        Label label = new Label();
        window.appendChild(label);
        Timer timer = new Timer();
        Progressmeter progressmeter = new Progressmeter();
        label.setValue("Task at " + getProcent() + "%");
        timer.addEventListener(Events.ON_TIMER, e -> {
            long percent = getProcent();
            label.setValue("Task at " + percent + "% - " + (taskExecutor.getCompletedTaskCount() - totalCount) + "/" + (taskExecutor.getTaskCount() - totalCount) + " -- " + taskExecutor.getCurrent());
            progressmeter.setValue((int) (percent < 0 ? 0 : percent));
        });
        progressmeter.setWidth("100%");
        window.appendChild(timer);
        window.appendChild(progressmeter);
        timer.start();
        timer.setDelay(1000);
        timer.setRepeats(true);
        page.setComponent(window);
        return this.page;
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
    public Class<? extends Provider> getGroup() {
        return AdminGroupProvider.class;
    }
}
