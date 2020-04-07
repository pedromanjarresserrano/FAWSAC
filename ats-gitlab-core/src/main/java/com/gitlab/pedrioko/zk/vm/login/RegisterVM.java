package com.gitlab.pedrioko.zk.vm.login;

import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.view.util.TextValidator;
import com.gitlab.pedrioko.domain.EmailTemplate;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoNotificaion;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.MailService;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.PathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * The Class RegisterVM.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RegisterVM {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterVM.class);

    /**
     * The labeltitulo.
     */
    private String labeltitulo;

    /**
     * The labelemail.
     */
    private String labelemail;

    /**
     * The labelusername.
     */
    private String labelusername;

    /**
     * The labelpassword.
     */
    private String labelpassword;

    /**
     * The labelcpassword.
     */
    private String labelcpassword;

    /**
     * The valuecpassword.
     */
    private String valuecpassword;

    /**
     * The labelname.
     */
    private String labelname;

    /**
     * The labellastname.
     */
    private String labellastname;

    /**
     * The labelmessage.
     */
    private String labelmessage;

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
     * The newuser.
     */
    private Usuario newuser;

    /**
     * The visiblemessage.
     */
    private boolean visiblemessage;

    /**
     * The labellogin.
     */
    private String labellogin;

    /**
     * Avilitable message.
     */
    private void avilitableMessage() {
        visiblemessage = true;
    }

    /**
     * Disable message.
     */
    private void disableMessage() {
        visiblemessage = false;
        labelerror = "";
    }

    /**
     * Gets the action.
     *
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets the labelcpassword.
     *
     * @return the labelcpassword
     */
    public String getLabelcpassword() {
        return labelcpassword;
    }

    /**
     * Gets the labelemail.
     *
     * @return the labelemail
     */
    public String getLabelemail() {
        return labelemail;
    }

    /**
     * Gets the labelerror.
     *
     * @return the labelerror
     */
    public String getLabelerror() {
        return labelerror;
    }

    /**
     * Gets the labellastname.
     *
     * @return the labellastname
     */
    public String getLabellastname() {
        return labellastname;
    }

    /**
     * Gets the labellogin.
     *
     * @return the labellogin
     */
    public String getLabellogin() {
        return labellogin;
    }

    /**
     * Gets the labelmessage.
     *
     * @return the labelmessage
     */
    public String getLabelmessage() {
        return labelmessage;
    }

    /**
     * Gets the labelname.
     *
     * @return the labelname
     */
    public String getLabelname() {
        return labelname;
    }

    /**
     * Gets the labelpassword.
     *
     * @return the labelpassword
     */
    public String getLabelpassword() {
        return labelpassword;
    }

    /**
     * Gets the labeltitulo.
     *
     * @return the labeltitulo
     */
    public String getLabeltitulo() {
        return labeltitulo;
    }

    /**
     * Gets the labelusername.
     *
     * @return the labelusername
     */
    public String getLabelusername() {
        return labelusername;
    }

    /**
     * Gets the newuser.
     *
     * @return the newuser
     */
    public Usuario getNewuser() {
        return newuser;
    }

    /**
     * Gets the valuecpassword.
     *
     * @return the valuecpassword
     */
    public String getValuecpassword() {
        return valuecpassword;
    }

    /**
     * Sets the valuecpassword.
     *
     * @param valuecpassword the new valuecpassword
     */
    public void setValuecpassword(String valuecpassword) {
        this.valuecpassword = valuecpassword;
    }

    /**
     * Inits the.
     */
    @Init
    public void init() {
        visiblemessage = false;
        newuser = new Usuario();
        labeltitulo = Labels.getLabel("registro.titulo");
        labelemail = Labels.getLabel("userbasicform.email");
        labelmessage = Labels.getLabel("recuperarcuenta.message");
        labelerror = Labels.getLabel("recuperarcuenta.error");
        action = Labels.getLabel("registro.accion");
        labelusername = Labels.getLabel("userbasicform.nombredeusuario");
        labelpassword = Labels.getLabel("userbasicform.contrasena");
        labelcpassword = Labels.getLabel("userbasicform.confirmarcontrasena");
        labelname = Labels.getLabel("userbasicform.nombres");
        labellastname = Labels.getLabel("userbasicform.apellidos");
        labellogin = Labels.getLabel("loginform.titulo");

    }

    /**
     * Checks if is visiblemessage.
     *
     * @return the visiblemessage
     */
    public boolean isVisiblemessage() {
        return visiblemessage;
    }

    /**
     * Register.
     */
    @Command
    @NotifyChange({"visiblemessage", "labelerror"})
    public void register() {
        disableMessage();
        newuser.setIdusuario(-1);
        newuser.setPassword(DigestUtils.md5DigestAsHex(newuser.getPassword().getBytes()));
        if (TextValidator.validateIfBlank(newuser.getEmail(), newuser.getApellidos(), newuser.getNombres(), newuser.getPassword(), newuser.getUsername(), valuecpassword)) {
            avilitableMessage();
            labelerror = Labels.getLabel("userbasicform.error.campovacio");
            return;
        }
        if (!newuser.getPassword().equals(DigestUtils.md5DigestAsHex(valuecpassword.getBytes()))) {
            avilitableMessage();
            labelerror = Labels.getLabel("userbasicform.error.contrasena");
            return;
        }
        Usuario user = crudService.getEntityByQueryUnique("FROM " + Usuario.class.getName() + " usr WHERE usr.email = ?", newuser.getEmail());
        if (user != null) {
            labelerror = Labels.getLabel("registro.error.email");
            avilitableMessage();
            return;
        }
        user = crudService.getEntityByQueryUnique("FROM " + Usuario.class.getName() + " usr WHERE usr.username = ?", newuser.getUsername());
        if (user != null) {
            labelerror = Labels.getLabel("registro.error.usuario");
            avilitableMessage();
            return;
        }
        newuser.setTipo(TipoUsuario.ROLE_USER);
        Optional<UserProfile> findFirst = crudService.getAll(UserProfile.class).stream()
                .filter(UserProfile::isPordefectorgistro).findFirst();
        newuser.setUserprofiles(new ArrayList<>());
        newuser.setPicture(null);
        if (findFirst.isPresent())
            newuser.getUserprofiles().add(findFirst.get());
        newuser.setEnable(Boolean.TRUE);
        Usuario turista = crudService.saveOrUpdate(newuser);
        send(turista, turista.getEmail());
    }

    private void send(Usuario usuario, String mail) {
        try {
            if (usuario != null && mail != null) {
                PathBuilder<?> pathBuilder = crudService.getPathBuilder(EmailTemplate.class);
                EnumPath<TipoNotificaion> tipo = pathBuilder.getEnum("tipo", TipoNotificaion.class);
                EmailTemplate fetchFirst = (EmailTemplate) crudService.query().from(pathBuilder).where(tipo.eq(TipoNotificaion.REGISTER)).fetchFirst();
                if (fetchFirst != null) {
                    HashMap<String, Object> vars = new HashMap<>();
                    vars.put("usuario", usuario);
                    smtpmailsender.send(Labels.getLabel("registrocreado"), fetchFirst.getPlantilla(), Arrays.asList(mail), null, vars);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error on sendmail()", e);

        }
    }
}
