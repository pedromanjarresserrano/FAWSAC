package com.gitlab.pedrioko.zk.vm.login;

import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.Usuario;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * The Class LoginVM.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public @Data
class LoginVM {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginVM.class);


    /**
     * The account.
     */
    String account;

    /**
     * The password.
     */
    String password;

    /**
     * The label.
     */
    String label;

    /**
     * The boton.
     */
    String boton;

    /**
     * The valueuser.
     */
    String valueuser;

    /**
     * The valuepass.
     */
    String valuepass;

    /**
     * The labelrecovery.
     */
    private String labelrecovery;

    /**
     * The labelnewuser.
     */
    private String labelnewuser;

    /**
     * The visiblemessage.
     */
    private boolean visiblemessage;

    /**
     * The labelerror.
     */
    private String labelerror;
    @WireVariable("fhsessionutil")
    private FHSessionUtil fhsessionutil;



    /**
     * Inits the.
     */
    @Init
    public void init() {
        fhsessionutil.setIpLocal(Executions.getCurrent().getRemoteAddr());
        Usuario currentUser = fhsessionutil.getCurrentUser();
        if (currentUser != null) Executions.sendRedirect("/index");

        account = Labels.getLabel("loginform.usuario");
        password = Labels.getLabel("loginform.contrasena");
        label = Labels.getLabel("loginform.titulo");
        boton = Labels.getLabel("loginform.boton");
        labelnewuser = Labels.getLabel("registro");
        labelrecovery = Labels.getLabel("recuperarcuenta.titulo");
        Boolean error = Boolean.valueOf(Executions.getCurrent().getParameter("error"));
        if (error != null && error) {
            visiblemessage = error;
            labelerror = Labels.getLabel("login.error");
        }
    }


}