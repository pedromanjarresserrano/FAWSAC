package com.gitlab.pedrioko.core.view.forms;

import com.gitlab.pedrioko.domain.Usuario;
import org.zkoss.zul.Textbox;

import java.util.LinkedHashMap;


public class LoginForm extends CustomForm {

    private static final long serialVersionUID = 1L;

    public LoginForm() {
        super(Usuario.class, new LinkedHashMap<String, Class<?>>() {
            private static final long serialVersionUID = 1L;

            {
                put("username", Textbox.class);
                put("password", Textbox.class);
            }
        });
        Textbox password = (Textbox) getComponentOfField("password");
        password.setType("password");
        getFormRenglones().forEach((k, v) -> {
            v.setClass("col-md-12 col-lg-12 col-xs-12");
        });
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.core.view.forms.Form#setValueForm(java.lang.
     * Object)
     */
    @Override
    public void setValueForm(Object obj) {
        super.setValueForm((Usuario) obj);
    }

}
