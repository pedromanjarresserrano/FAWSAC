package com.gitlab.pedrioko.zk.vm.login;

import com.gitlab.pedrioko.core.view.util.StringUtil;
import com.gitlab.pedrioko.core.view.util.TextValidator;
import com.gitlab.pedrioko.domain.EmailTemplate;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoNotificaion;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.MailService;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.Data;
import org.springframework.util.DigestUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class RecoveryVM.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public @Data
class RecoveryVM {

    /**
     * The labeltitulo.
     */
    private String labeltitulo;

    /**
     * The labelemail.
     */
    private String labelemail;

    /**
     * The labelmessage.
     */
    private String labelmessage;

    /**
     * The email.
     */
    private String email;

    /**
     * The labelerror.
     */
    private String labelerror;


    /**
     * The my service.
     */
    @WireVariable
    private CrudService crudService;

    /**
     * The smtpmailsender.
     */
    @WireVariable
    private MailService smtpmailsender;

    /**
     * The action.
     */
    private String action;

    /**
     * The labellogin.
     */
    private String labellogin;

    /**
     * The visiblemessage.
     */
    private boolean visiblemessage;

    private Usuario user;

    /**
     * Inits the.
     */
    @Init
    public void init() {
        visiblemessage = false;
        labeltitulo = Labels.getLabel("recuperarcuenta.titulo");
        labelemail = Labels.getLabel("userbasicform.email");
        labelmessage = Labels.getLabel("recuperarcuenta.message");
        labelerror = Labels.getLabel("recuperarcuenta.error");
        labellogin = Labels.getLabel("loginform.titulo");
        action = Labels.getLabel("enviar");
    }

    /**
     * Recover.
     */
    @Command
    @NotifyChange({"visiblemessage", "labelerror"})
    public void recover() {
        if (TextValidator.validateIfBlank(email)) {
            labelerror = Labels.getLabel("userbasicform.error.campovacio");
            visiblemessage = true;
            return;
        }

        user = crudService.getEntityByQueryUnique("FROM " + Usuario.class.getName() + " usr WHERE usr.email = ?",
                email.trim());

        if (user == null) {

            labelerror = Labels.getLabel("recuperarcuenta.error");
            visiblemessage = true;
            return;
        }

        String saltstring = StringUtil.getSaltString();
        user.setPassword(DigestUtils.md5DigestAsHex(saltstring.getBytes()));
        crudService.save(user);
        sendEmail(user.getEmail(), user.getUsername(), saltstring);

    }

    /**
     * Send email.
     *
     * @param to         the to
     * @param username   the username
     * @param saltstring the saltstring
     */
    protected void sendEmail(String to, String username, String saltstring) {

        String subject = "Alert System Recovery Password";
        String body = "Welcome " + username + " \n your new pass is: " + saltstring;
        PathBuilder<?> template = crudService.getPathBuilder(EmailTemplate.class);
        EnumPath<TipoNotificaion> pathBuilder = template.getEnum("tipo", TipoNotificaion.class);

        EmailTemplate fetchFirst = (EmailTemplate) crudService.query().from(template)
                .where(pathBuilder.eq(TipoNotificaion.RESET_PASS)).fetchFirst();
        if (fetchFirst != null) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("password", saltstring);
            vars.put("turista", user);
            vars.put("usuario", (Usuario) user);
            smtpmailsender.send("Reset Password", fetchFirst.getPlantilla(), Arrays.asList(to), null, vars);
        } else {
            smtpmailsender.send(subject, body, to);
        }
    }


}
