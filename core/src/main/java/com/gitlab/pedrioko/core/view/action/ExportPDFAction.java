package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
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

@ToolAction
@Order(0)
public class ExportPDFAction implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPDFAction.class);

    @Override
    public String getIcon() {
        return "fas fa-file-pdf";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        String filename = "export-" + UUID.randomUUID() + ".pdf";

        try {
            byte[] buildPDF = Exporter.BuildPDF(((CrudView)event.getCrudViewParent()).getValue());
            if (buildPDF == null) {
                ZKUtil.showMessage(ReflectionZKUtil.getLabel("emptyexport"), MessageType.INFO);
                return;
            }
            Filedownload.save(new ByteArrayInputStream(buildPDF), "", filename);
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
        return "btn-danger";
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
        return "#E74C3C";
    }

    @Override
    public int getGroup() {
        return 1;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("export") + " PDF";
    }

}
