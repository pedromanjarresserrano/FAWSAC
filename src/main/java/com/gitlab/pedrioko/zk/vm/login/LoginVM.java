package com.gitlab.pedrioko.zk.vm.login;

import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
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
     * The message.
     */
    String message;

    /**
     * The window.
     */
    String window;

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
    @WireVariable
    private FHSessionUtil fhsessionutil;

    @WireVariable
    private CrudService crudService;
    @WireVariable
    protected AuthenticationManager authenticationManager;

    private String appName;

    /**
     * Do login.
     */
    @Command
    @NotifyChange({"visiblemessage", "labelerror"})
    public void doLogin() {
        labelerror = "";
        visiblemessage = false;
        try {
            SecurityContext sc = SecurityContextHolder.getContext();
            if (valueuser == null || valueuser.isEmpty() || valuepass == null || valuepass.isEmpty())
                throw new IllegalArgumentException();
            sc.setAuthentication(new UsernamePasswordAuthenticationToken(valueuser, valuepass));
            SecurityContextHolder.setContext(sc);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String name = authentication.getName();
            if (name != null) {
                PathBuilder<?> builder = crudService.getPathBuilder(Usuario.class);
                Usuario user = (Usuario) crudService.query().from(builder).where(builder.get("username", String.class).eq(valueuser)).fetchFirst();
                fhsessionutil.setCurrentUser(user);
            }

        } catch (Exception e) {
            LOGGER.error("Error on doLogin()", e);
            labelerror = Labels.getLabel("login.error");
            visiblemessage = true;
            return;
        }
        if (fhsessionutil.getCurrentUser() != null) {
            if (fhsessionutil.getCurrentUser().getEnable()) {
                Executions.sendRedirect("/index");
            } else {
                labelerror = Labels.getLabel("login.userenable");
                visiblemessage = true;
            }
        } else {
            labelerror = Labels.getLabel("login.error");
            visiblemessage = true;
        }

    }


    /**
     * Inits the.
     */
    @Init
    public void init() {
        Usuario currentUser = fhsessionutil.getCurrentUser();
        if (currentUser != null) Executions.sendRedirect("/index");

        account = Labels.getLabel("loginform.usuario");
        password = Labels.getLabel("loginform.contrasena");
        label = Labels.getLabel("loginform.titulo");
        boton = Labels.getLabel("loginform.boton");
        labelnewuser = Labels.getLabel("registro");
        labelrecovery = Labels.getLabel("recuperarcuenta.titulo");
        appName = ApplicationContextUtils.getBean(Environment.class).getProperty("spring.application.name");
        crudService = ApplicationContextUtils.getBean(CrudService.class);

    }


}