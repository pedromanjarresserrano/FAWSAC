package com.gitlab.pedrioko.core.view.api;

import com.gitlab.pedrioko.core.view.enums.FormStates;

public interface Valuable {

    void setValueForm(Object obj);

    FormStates getEstado();

    void setEstado(FormStates update);
}
