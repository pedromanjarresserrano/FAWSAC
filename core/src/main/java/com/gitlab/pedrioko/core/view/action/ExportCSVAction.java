package com.gitlab.pedrioko.core.view.action;

import org.springframework.stereotype.Component;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.AplicateAllClass;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.util.Exporter;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.zkoss.zul.Filedownload;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@Order(0)
public class ExportCSVAction implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportCSVAction.class);

    @Override
    public String getIcon() {
        return "far fa-file";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        String filename = "export-" + UUID.randomUUID() + ".csv";

        try {
            byte[] buildCSV = Exporter.BuildCSV(((CrudView) event.getCrudViewParent()).getValue());
            if (buildCSV == null) {
                ZKUtil.showMessage(ReflectionZKUtil.getLabel("emptyexport"), MessageType.INFO);
                return;
            }
            Filedownload.save(new ByteArrayInputStream(buildCSV), "", filename);
        } catch (Exception e) {
            LOGGER.error("ERROR on actionPerform()", e);
        }

    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(AplicateAllClass.class);
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getClasses() {
        return "btn-default";
    }

    @Override
    public FormStates getFormState() {
        return FormStates.READ;
    }

    @Override
    public Integer position() {
        return 5;
    }

    @Override
    public String getColor() {
        return "#7F8C8D";
    }

    @Override
    public int getGroup() {
        return 1;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("export") + " CSV";
    }

}
