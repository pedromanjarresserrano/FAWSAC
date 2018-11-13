package com.gitlab.pedrioko.zk.vm.user;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.view.util.TextValidator;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.StorageService;
import com.gitlab.pedrioko.zk.composer.util.LabelUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.File;
import java.io.IOException;

/**
 * The Class ProfileViewModel.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public @Data
class ProfileViewModel {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileViewModel.class);

    /**
     * The my service.
     */
    @WireVariable
    private CrudService crudService;

    @WireVariable
    private FHSessionUtil fhsessionutil;
    /**
     * The titulo.
     */
    private String titulo;

    /**
     * The tabdatos.
     */
    private String tabdatos;

    /**
     * The tabcontrasena.
     */
    private String tabcontrasena;

    /**
     * The tabdatosextra.
     */
    private String tabdatosextra;

    /**
     * The subir.
     */
    private String subir;

    /**
     * The photo.
     */
    private Media photo;

    /**
     * The habilita.
     */
    private Boolean habilita;

    /**
     * The src.
     */
    private String src;

    /**
     * The errordatosbasicos.
     */
    private String errordatosbasicos;

    /**
     * The errorpass.
     */
    private String errorpass;

    /**
     * The successdatosbasicos.
     */
    private String successdatosbasicos;

    /**
     * The successpass.
     */
    private String successpass;

    /**
     * The labelerror.
     */
    private String labelerror = "userbasicform.error.campovacio";

    /**
     * The turista.
     */
    private boolean turista;

    /**
     * The labelutil.
     */
    @WireVariable
    private LabelUtil labelutil;

    /**
     * After compose.
     *
     * @param view the view
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }


    /**
     * Habilitar.
     */
    @Command
    @NotifyChange("habilita")
    public void habilitar() {
        habilita = false;
    }

    /**
     * Inits the.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Init
    public void init() throws IOException {
        labelutil.loadLabels();
        labelutil.setUser(fhsessionutil.getCurrentUser());
        habilita = true;
        turista = false;
        src = "~./zul/images/male.png";
        try {
            File test = new File(labelutil.getUser().getPicture().getUrl());
            AImage ai = new AImage(test);
            labelutil.setPhoto(ai);
        } catch (Exception e) {
            LOGGER.error("ERROR ON init()", e);
        }
        if (labelutil.getUser().getTipo().equals(TipoUsuario.ROLE_TURISTA)) {
            turista = true;
        }
        tabdatos = Labels.getLabel("userbasicform.datosbasicos");
        tabcontrasena = Labels.getLabel("userbasicform.cambiocontrasena");
        tabdatosextra = Labels.getLabel("datos.extras");
        titulo = Labels.getLabel("userbasic.perfil.titulo");
        subir = Labels.getLabel("userbasicform.subir");
        labelutil.setValueapellidos(labelutil.getUser().getApellidos());
        labelutil.setValuecedula(labelutil.getUser().getCedula());
        labelutil.setValueemail(labelutil.getUser().getEmail());
        labelutil.setValuenombres(labelutil.getUser().getNombres());
        labelutil.setValuetelefono(labelutil.getUser().getTelefono());
        labelutil.setValueusername(labelutil.getUser().getUsername());

    }


    @Command
    @NotifyChange({"photo", "labelutil"})
    public void onFileUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        UploadEvent upEvent;
        Object objUploadEvent = ctx.getTriggerEvent();
        if (objUploadEvent != null && objUploadEvent instanceof UploadEvent) {
            upEvent = (UploadEvent) objUploadEvent;
            photo = upEvent.getMedia();
            labelutil.setPhoto(photo);
        }
    }

    /**
     * Update password.
     */
    @Command
    @NotifyChange({"errorpass", "successpass"})
    public void updatePassword() {
        if (labelutil.getValuepassword() != null && labelutil.getValuecpassword() != null
                && !labelutil.getValuepassword().isEmpty() && !labelutil.getValuecpassword().isEmpty()) {
            if (labelutil.getValuecpassword().equals(labelutil.getValuepassword())) {
                labelutil.getUser().setPassword(DigestUtils.md5DigestAsHex(labelutil.getValuecpassword().getBytes()));
                crudService.save(labelutil.getUser());
                successpass = Labels.getLabel("userbasicform.success.cambiocontrasena");
                errorpass = "";
                return;
            } else {
                errorpass = Labels.getLabel("userbasicform.error.contrasena");
                successpass = "";
                return;
            }
        }
        errorpass = Labels.getLabel(labelerror);
        successpass = "";
    }

    /**
     * Updateperfil.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Command
    @NotifyChange({"errordatosbasicos", "successdatosbasicos"})
    public void updateperfil() {

        if (TextValidator.validateIfBlank(labelutil.getValueapellidos(), labelutil.getValuecedula(),
                labelutil.getValueemail())) {
            errordatosbasicos = Labels.getLabel(labelerror);
            successdatosbasicos = "";
            return;
        }
        if (TextValidator.validateIfBlank(labelutil.getValuenombres(), String.valueOf(labelutil.getValuetelefono()),
                labelutil.getValueusername())) {
            errordatosbasicos = Labels.getLabel(labelerror);
            successdatosbasicos = "";
            return;
        }
        if (!TextValidator.validateEmail(labelutil.getValueemail())) {
            errordatosbasicos = Labels.getLabel("userbasicform.error.email");
            successdatosbasicos = "";
            return;
        }
        Usuario user = fhsessionutil.getCurrentUser();
        user.setApellidos(labelutil.getValueapellidos());
        user.setEmail(labelutil.getValueemail());
        user.setNombres(labelutil.getValuenombres());
        user.setTelefono(labelutil.getValuetelefono());
        user.setUsername(labelutil.getValueusername());
        String name = user.getIdusuario() + ".jpg";
        File saveFile = null;
        if (labelutil.getPhoto() != null)
            saveFile = ApplicationContextUtils.getBean(StorageService.class).saveFile(name, labelutil.getPhoto().getStreamData());
        FileEntity picture = crudService.getById(FileEntity.class, user.getPicture().getId());

        if (picture == null) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(name);
            fileEntity.setUrl(saveFile != null ? saveFile.getAbsolutePath() : "");
            user.setPicture(crudService.saveOrUpdate(fileEntity));
        } else {
            try {
                picture.setFilename(name);
                picture.setUrl(saveFile != null ? saveFile.getAbsolutePath() : "");
                user.setPicture(crudService.saveOrUpdate(picture));
            } catch (Exception a) {
            }
        }

        fhsessionutil.setCurrentUser(crudService.saveOrUpdate(user));
        successdatosbasicos = Labels.getLabel("userbasicform.success.datosbasicos");
        errordatosbasicos = "";
        EventQueues.lookup("loadImage", EventQueues.SESSION, true).publish(new Event("loadImage", null, null));
    }

}