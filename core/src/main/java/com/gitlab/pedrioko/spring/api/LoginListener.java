package com.gitlab.pedrioko.spring.api;

import com.gitlab.pedrioko.domain.Usuario;

public interface LoginListener {

    void onLoginEvent(Usuario usuario);
}
