package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.zk.component.FileUpload;
import com.gitlab.pedrioko.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
        image.setClass("w-50  img-thumbnail rounded");
        Div field = new Div();
        field.setSclass("d-flex  justify-content-center");
        field.appendChild(image);
        FileUpload button = new FileUpload("UploadFile");
        button.setClass("btn btn-lg btn-success");
        button.getUpload();
        button.setValue(new FileEntity());
        EventListener<Event> eventEventListener = event -> {
            if (button.getValue() != null) {
                String filename = button.getValue().getFilename();
                if (filename != null && !filename.isEmpty()) {
                    String urlFile = storageservice.getUrlFile(filename, false).replace("//", "/");
                    if (field.getParent() == null) button.getParent().getChildren().add(0, field);
                    image.setSrc(urlFile);
                }
            }
        };
        button.addEventListener(Events.ON_UPLOAD, eventEventListener);
        button.addEventListener(Events.ON_CHANGE, eventEventListener);
        return button;
    }

}
