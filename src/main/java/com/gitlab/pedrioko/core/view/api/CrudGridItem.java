package com.gitlab.pedrioko.core.view.api;

import com.gitlab.pedrioko.core.lang.FileEntity;

import java.util.List;

public interface CrudGridItem {

    Boolean carrouselPreview();

    List<FileEntity> getFilesEntities();

    String name();

    default  FileEntity getFile(){
        return null;
    }

    String local();

    String webServiceURL();

    String visualName();

}
