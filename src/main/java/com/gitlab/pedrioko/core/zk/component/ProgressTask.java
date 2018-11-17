package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.zk.component.model.enums.ProgressTaskType;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;

public class ProgressTask extends HtmlBasedComponent {

    private long time = 1000;
    private OnEvent onTimerEvent = null;
    private long maxValue = 0;
    private long currentValue = 0;
    private ProgressTaskType type;

    public ProgressTask() {
        super();
        addEventListener(Events.ON_AFTER_SIZE, e -> {
            if (onTimerEvent != null)
                onTimerEvent.doSomething();
        });
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "time", getTime());
        render(renderer, "maxValue", getMaxValue());
        render(renderer, "currentValue", getCurrentValue());
        render(renderer, "type", getType());
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
        smartUpdate("maxValue", getMaxValue());

    }

    public long getCurrentValue() {
        return currentValue;
    }



    public void setCurrentValue(long currentValue) {
        if (this.currentValue != currentValue) {
            this.currentValue = currentValue;
            smartUpdate("currentValue", getCurrentValue());
        }
    }

    public ProgressTaskType getType() {
        return type;
    }

    public void setType(ProgressTaskType type) {
        this.type = type;
        smartUpdate("type", getType());

    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        smartUpdate("time", getTime());
    }

    public OnEvent getOnTimerEvent() {
        return onTimerEvent;
    }

    public void setOnTimerEvent(OnEvent onTimerEvent) {
        this.onTimerEvent = onTimerEvent;
    }
}
