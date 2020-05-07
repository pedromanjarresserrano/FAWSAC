package com.gitlab.pedrioko.core.zk.viewmodel.crud;

import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.StorageService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Map;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CrudViewVM {

    private CrudView crudView;
    private Class<?> klass;
    @WireVariable
    private CrudService crudService;
    private StorageService storageService;
    private String uuid;
    private boolean open = false;

    @Init
    private void init() {

        Map<?, ?> arg = (Map<?, ?>) Executions.getCurrent().getArg();
        crudView = (CrudView) arg.get("CrudView");
        klass = (Class<?>) arg.get("klass-crud");
        uuid = (String) arg.get("CrudViewUUID");

        EventQueues.lookup("filter-crud-" + klass.getSimpleName(), EventQueues.SESSION, true).subscribe(event -> {
            if (event.getTarget() == crudView && event.getName().equalsIgnoreCase("filter-crud-" + klass.getSimpleName() + "-" + uuid)) {
                open = !open;
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }


    @NotifyChange("*")
    @GlobalCommand
    public void refresh() {
        System.out.println("Update");
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
