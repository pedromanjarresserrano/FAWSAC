package com.gitlab.pedrioko.core.zk.viewmodel.crud.filters;

import com.gitlab.pedrioko.core.view.viewers.crud.controllers.CrudController;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

public class Pagination {

    private CrudController crudController;
    private int activepage;
    private long count;
    private int pagesize;

    @Init
    private void init() {
        crudController = (CrudController) Executions.getCurrent().getArg().get("crud-controller");
        count = crudController.getCount();
        pagesize = crudController.getLimit();
    }

    @Command
    public void paging() {
        int ofs = activepage * pagesize;
        crudController.setPage(ofs, pagesize);
        BindUtils.postGlobalCommand(null, null, "refresh", null);

    }

    @NotifyChange({"count", "pagesize"})
    @GlobalCommand
    public void refresh() {
        count = crudController.getCount();
        pagesize = crudController.getLimit();
    }

    public int getActivepage() {
        return activepage;
    }

    public void setActivepage(int activepage) {
        this.activepage = activepage;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
}
