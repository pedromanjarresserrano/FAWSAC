package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.annotation.FieldFilter;
import com.gitlab.pedrioko.core.lang.annotation.FileSize;
import com.gitlab.pedrioko.core.view.api.FieldFilterComponent;
import com.gitlab.pedrioko.core.zk.component.rangebox.FileSizeRangeBox;
import com.gitlab.pedrioko.core.zk.component.rangebox.IntegerRangeBox;
import org.zkoss.zk.ui.Component;

import java.lang.reflect.Field;

@org.springframework.stereotype.Component
public class IntFieldFilter implements FieldFilterComponent {

    @Override
    public Class[] getToClass() {
        return new Class[]{int.class, Integer.class};
    }

    @Override
    public Component getComponent(Field field) {
        return field.isAnnotationPresent(FileSize.class) ? new FileSizeRangeBox() : new IntegerRangeBox();
    }

}
