package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.view.action.SaveAction;
import com.gitlab.pedrioko.core.view.action.TestEmailAccountAction;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.domain.EmailAccount;
import com.gitlab.pedrioko.domain.EmailTemplate;
import com.gitlab.pedrioko.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import java.util.Arrays;
import java.util.HashMap;

@Menu
public class EmailAccountMenuProvider implements MenuProvider {
    @Autowired
    private MailService mailService;
    @Autowired
    private SaveAction saveAction;
    @Autowired
    private TestEmailAccountAction testEmailAccountAction;

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("EmailAccount");
    }

    @Override
    public Component getView() {
        HashMap<Object, Object> arg = new HashMap<>();
        arg.put("actions-form", Arrays.asList(saveAction,testEmailAccountAction));
        arg.put("value", mailService.getEmailAccount());
        CrudActionEvent actionEvent = new CrudActionEvent();
        actionEvent.setFormstate(FormStates.UPDATE);
        arg.put("event-crud", actionEvent);
        arg.put("estado-form", FormStates.UPDATE);

        Component components = Executions.createComponents("~./zul/forms/form.zul", null, arg);
        return components;
    }

    @Override
    public String getIcon() {
        return "fas fa-envelope-square";
    }

    @Override
    public int getPosition() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String getGroup() {
        return "administracion";
    }
}
