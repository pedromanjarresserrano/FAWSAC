package com.gitlab.pedrioko.core.view.api;

import java.util.List;

public interface CrudDisplayTable {

    List getValue();
    <T> T getSelectedValue();
    void clearSelection();
    void update();
    void setValue(List<?> all);

    void updateValue(Object value);
}
