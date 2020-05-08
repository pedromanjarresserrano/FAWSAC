package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.lang.annotation.CellCustomizer;
import com.gitlab.pedrioko.core.view.api.ListCellCustomizer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

import java.util.Arrays;
import java.util.List;

@CellCustomizer
public class FileEntityListCellCustomizer implements ListCellCustomizer {
    @Override
    public List<Class<?>> getToClass() {
        return Arrays.asList(FileEntity.class);
    }

    @Override
    public Component getCompònent(Object value) {
        Label label = new Label();
        String filename = ((FileEntity) value).getFilename();
        label.setValue(filename);
        return label;
    }
}
