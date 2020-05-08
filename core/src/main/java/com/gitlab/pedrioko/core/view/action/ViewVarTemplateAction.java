package com.gitlab.pedrioko.core.view.action;

import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.FormStates;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.domain.EmailTemplate;
import org.apache.velocity.VelocityContext;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

//@ToolAction
@Order(0)
public class ViewVarTemplateAction implements Action {

    private VelocityContext context;

    @Override
    public String getIcon() {
        return "fas fa-info-circle";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
      /*  HashMap<String, Object> vars = new HashMap<>();
        Turista turista = new Turista();
        vars.put("turista", turista);
        vars.put("usuario", (Usuario) turista);
        VelocityContext ctxEnc = new VelocityContext(vars);
        context = new VelocityContext(ctxEnc);
        Arrays.asList(context.internalGetKeys()).forEach(System.err::println);*/
    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(EmailTemplate.class);
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getClasses() {
        return "btn-info";
    }

    @Override
    public FormStates getFormState() {
        return FormStates.READ;
    }

    @Override
    public Integer position() {
        return 0;
    }

    @Override
    public String getColor() {
        return "#1418db";
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String getTooltipText() {
        return ReflectionZKUtil.getLabel("ver");
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
