package com.gitlab.pedrioko.providers.menu;

import com.gitlab.pedrioko.core.lang.Page;
import com.gitlab.pedrioko.core.lang.annotation.Menu;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.action.SaveAction;
import com.gitlab.pedrioko.core.view.action.TestEmailAccountAction;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.providers.AdminGroupProvider;
import com.gitlab.pedrioko.services.MailService;

import java.util.Arrays;
import java.util.HashMap;

@Menu
public class EmailAccountMenuProvider implements MenuProvider {
    private final MailService mailService;
    private final SaveAction saveAction;
    private final TestEmailAccountAction testEmailAccountAction;

    Page page = new Page("~./zul/forms/form.zul");

    public EmailAccountMenuProvider(MailService mailService, SaveAction saveAction, TestEmailAccountAction testEmailAccountAction) {
        this.mailService = mailService;
        this.saveAction = saveAction;
        this.testEmailAccountAction = testEmailAccountAction;
    }

    @Override
    public String getLabel() {
        return ReflectionZKUtil.getLabel("EmailAccount");
    }

    @Override
    public Page getView() {
        HashMap<Object, Object> arg = new HashMap<>();
        arg.put("actions-form", Arrays.asList(saveAction, testEmailAccountAction));
        arg.put("value", mailService.getEmailAccount());
        CrudActionEvent actionEvent = new CrudActionEvent();
        actionEvent.setFormstate(FormStates.UPDATE);
        arg.put("event-crud", actionEvent);
        arg.put("estado-form", FormStates.UPDATE);

        page.setArg(arg);
        return page;
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
    public Class<? extends Provider> getGroup() {
        return AdminGroupProvider.class;
    }
}
