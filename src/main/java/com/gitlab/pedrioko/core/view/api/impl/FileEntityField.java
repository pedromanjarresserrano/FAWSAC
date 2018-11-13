package com.gitlab.pedrioko.core.view.api.impl;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.lang.annotation.FieldForm;
import com.gitlab.pedrioko.core.view.api.FieldComponent;
import com.gitlab.pedrioko.core.view.forms.EntityForm;
import com.gitlab.pedrioko.core.zk.component.FileUpload;
import com.gitlab.pedrioko.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;

import java.io.File;
import java.lang.reflect.Field;

@FieldForm
public class FileEntityField implements FieldComponent {
    @Autowired
    private StorageService storageservice;

    private Media photo;

    @Override
    public Class[] getToClass() {
        return new Class[]{FileEntity.class};
    }

    @Override
    public Component getComponent(Field e, EntityForm f) {
        FileUpload button = new FileUpload("UploadFile");
        button.setClass("btn btn-lg btn-success");
        button.getUpload();
        button.setValue(new FileEntity());
        button.addEventListener("onUpload", x -> {
            UploadEvent upEvent = (UploadEvent) x;
            if (upEvent != null) {
                photo = upEvent.getMedia();
                String name = photo.getName();
                button.setLabel(name);
                File file = storageservice.saveFile(name, photo.getStreamData());
                FileEntity value = button.getValue();
                value.setUrl(file.getAbsolutePath());
                value.setFilename(file.getName());
                button.setValue(value);
            }
        });

        return button;
    }

}
