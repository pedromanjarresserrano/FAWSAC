package com.gitlab.pedrioko.core.zk.viewmodel.crud.table;

import com.gitlab.pedrioko.core.view.api.CrudDisplayTable;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.util.PropertiesUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.CrudView;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CrudTable {


    private List<Field> fieldList;
    private List<String> headers;
    private CrudView crudView;
    private Class<?> klass;
    private CrudDisplayTable crudDisplayTable;
    private MenuProvider menuprovider;

    @Init
    private void init() {
        crudView = (CrudView) Executions.getCurrent().getArg().get("CrudView");
        klass = (Class<?>) Executions.getCurrent().getArg().get("klass-crud");
        menuprovider = (MenuProvider) Executions.getCurrent().getArg().get("menuprovider");
        headers = getBean(PropertiesUtil.class).getFieldTable(klass);
        loadfields();
    }

    private void loadfields() {
        fieldList = ReflectionJavaUtil.getFields(klass);
        if (headers != null && !headers.isEmpty()) {
            List<Field> filter = fieldList.stream().filter(e -> headers.contains(e.getName()))
                    .collect(Collectors.toList());
            fieldList = filter;
        }
        headers = fieldList.stream().map(Field::getName).collect(Collectors.toList());
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }
}