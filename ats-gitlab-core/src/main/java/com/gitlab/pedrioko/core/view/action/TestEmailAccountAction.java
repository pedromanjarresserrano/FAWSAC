package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.forms.Form;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.MessageboxDlg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ToolAction
public class TestEmailAccountAction implements Action {

    @Autowired
    private MailService mailService;

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("Send Mail test");
    }

    @Override
    public String getIcon() {
        return "z-icon-floppy-o";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        ZKUtil.showInputDialogWindow("Email", "Input Email to test", data -> {
            if (data != null) {
                if (!((String) data).isEmpty()) {
                    mailService.send("Test", "Teste Email", (String) data);
                    ZKUtil.showMessage("Email sended", MessageType.SUCCESS);

                } else {
                    ZKUtil.showMessage("Email is empty", MessageType.INFO);
                }
            }
        });
    }

    @Override
    public List<?> getAplicateClass() {
        return Collections.emptyList();
    }

    @Override
    public String getClasses() {
        return "pull-left btn-primary";
    }

    @Override
    public FormStates getFormState() {
        return null;
    }

    @Override
    public Integer position() {
        return -1;
    }

    @Override
    public String getColor() {
        return null;
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return null;
    }
}
