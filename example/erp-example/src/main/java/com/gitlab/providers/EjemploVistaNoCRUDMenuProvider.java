package com.gitlab.providers;

import org.springframework.stereotype.Component;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

@Component
public class EjemploVistaNoCRUDMenuProvider implements MenuProvider {
    @Override
    public String getLabel() {
        return "Vista No CRUD";
    }

    @Override
    public Component getView() {
        return Executions.createComponents("~./zul/views/Ejemplo.zul", null, null);
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public int getPosition() {
        return -1;
    }

    @Override
    public String getGroup() {
        return "Ejemplos";
    }
}

