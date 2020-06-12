package com.example.actions;

import com.example.domain.Item;
import com.gitlab.pedrioko.core.lang.annotation.ToolAction;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.action.event.CrudActionEvent;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.services.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@ToolAction
public class MostrarDatosItemAction implements Action {

    @Autowired
    private CrudService crudService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private MailService mailService;

    @Autowired
    private SecurityService securityService;

    @Override
    public String getLabel() {
        return "Mostrar datos";
    }

    @Override
    public String getIcon() {
        return "fas fa-align-justify";
    }

    @Override
    public void actionPerform(CrudActionEvent event) {
        Object value = event.getValue();
        if (value != null) {
            String message = value.toString();
            ZKUtil.showMessage(message, MessageType.SUCCESS);
        } else {
            ZKUtil.showMessage("Seleccione un registro de item", MessageType.INFO);
        }
        List<Item> all = crudService.getAll(Item.class);

        ZKUtil.showMessage(all.toString(), MessageType.SUCCESS);

    }

    @Override
    public List<?> getAplicateClass() {
        return Arrays.asList(Item.class);
    }

    @Override
    public Integer position() {
        return 0;
    }

    @Override
    public String getColor() {
        return "black";
    }

    @Override
    public int getGroup() {
        return 7;
    }
}
