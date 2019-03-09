package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import com.gitlab.pedrioko.core.zk.component.FileUpload;
import com.gitlab.pedrioko.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

import java.lang.reflect.Field;

@FieldForm
public class FileEntityField implements FieldComponent {
    @Autowired
    private StorageService storageservice;


    @Override
    public Class[] getToClass() {
        return new Class[]{FileEntity.class};
    }

    @Override
    public Component getComponent(Field e) {
        Image image = new Image();

        Div field = new Div();
        FileUpload button = new FileUpload("UploadFile");
        button.setClass("btn btn-lg btn-success");
        button.getUpload();
        button.setValue(new FileEntity());
        button.addEventListener(Events.ON_UPLOAD, event -> {
          /*  if (button.getValue() != null) {
                String urlFile = (ApplicationContextUtils.getBean(StorageService.class).getUrlFile(((FileEntity) button.getValue()).getFilename(), false)).replace("//", "/");
                if (image.getParent() == null) button.getParent().getChildren().add(0, image);

                image.setVisible(true);

                image.setClass("img-responsive");
                image.setStyle("margin: auto;");
                image.setSrc(urlFile);
                image.setHeight("200px");


            }*/
        });


        button.addEventListener(Events.ON_CHANGE, event -> {
       /*     if (button.getValue() != null) {
                String urlFile = (ApplicationContextUtils.getBean(StorageService.class).getUrlFile(((FileEntity) button.getValue()).getFilename(), false)).replace("//", "/");
                if (image.getParent() == null) button.getParent().getChildren().add(0, image);
                image.setVisible(true);

                image.setClass("img-responsive");
                image.setStyle("margin: auto;");
                image.setSrc(urlFile);
                image.setHeight("200px");


            }*/
        });
        image.setVisible(false);


        return button;
    }

}
