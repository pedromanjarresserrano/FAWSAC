package com.gitlab.pedrioko.spring.api.impl;

import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.spring.api.onLoginListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class onLoginListenerImpl implements onLoginListener {
    @Autowired
    private CrudService crudService;
    @Override
    public void onLoginEvent(Usuario usuario) {

    }

}
