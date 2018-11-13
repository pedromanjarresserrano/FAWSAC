package com.gitlab.pedrioko.zk.composer.util;

import com.gitlab.pedrioko.domain.Usuario;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;

import java.util.Date;

/**
 * The Class LabelUtil.
 */
@Service("labelutil")
@Data
public class LabelUtil {

    /**
     * The user.
     */
    protected Usuario user;
    /**
     * The subir.
     */
    private String subir;
    /**
     * The nombres.
     */
    private String nombres;
    /**
     * The apellidos.
     */
    private String apellidos;
    /**
     * The genero.
     */
    private String genero;
    /**
     * The fecha.
     */
    private String fecha;
    /**
     * The email.
     */
    private String email;
    /**
     * The telefono.
     */
    private String telefono;
    /**
     * The cedula.
     */
    private String cedula;
    /**
     * The username.
     */
    private String username;
    /**
     * The password.
     */
    private String password;
    /**
     * The cpassword.
     */
    private String cpassword;
    /**
     * The guardar.
     */
    private String guardar;
    /**
     * The cancelar.
     */
    private String cancelar;
    /**
     * The valuenombres.
     */
    private String valuenombres;

    /**
     * The valueapellidos.
     */
    private String valueapellidos;

    /**
     * The valuegenero.
     */
    private Integer valuegenero;

    /**
     * The valuerole.
     */
    private Integer valuerole;

    /**
     * The valuefecha.
     */
    private Date valuefecha;

    /**
     * The valueemail.
     */
    private String valueemail;

    /**
     * The valuetelefono.
     */
    private long valuetelefono;

    /**
     * The valuecedula.
     */
    private String valuecedula;

    /**
     * The valueusername.
     */
    private String valueusername;

    /**
     * The valuepassword.
     */
    private String valuepassword;

    /**
     * The valuecpassword.
     */
    private String valuecpassword;

    /**
     * The generof.
     */
    private String generof;

    /**
     * The generom.
     */
    private String generom;

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
     * Instantiates a new label util.
     */
    private LabelUtil() {
        loadLabels();
    }

    /**
     * Load labels.
     */
    public void loadLabels() {
        nombres = Labels.getLabel("userbasicform.nombres");
        apellidos = Labels.getLabel("userbasicform.apellidos");
        genero = Labels.getLabel("userbasicform.genero");
        generom = Labels.getLabel("userbasicform.genero.masculino");
        generof = Labels.getLabel("userbasicform.genero.femenino");
        fecha = Labels.getLabel("userbasicform.fecha");
        email = Labels.getLabel("userbasicform.email");
        telefono = Labels.getLabel("userbasicform.telefono");
        cedula = Labels.getLabel("userbasicform.cedula");
        username = Labels.getLabel("userbasicform.nombredeusuario");
        password = Labels.getLabel("userbasicform.contrasena");
        cpassword = Labels.getLabel("userbasicform.confirmarcontrasena");
        guardar = Labels.getLabel("userbasicform.guardar");
        cancelar = Labels.getLabel("userbasicform.cancelar");
    }

}
