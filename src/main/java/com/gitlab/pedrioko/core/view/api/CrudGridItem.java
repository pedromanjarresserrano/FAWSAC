package com.gitlab.pedrioko.core.view.api;

import com.gitlab.pedrioko.core.lang.FileEntity;

import java.util.List;

public interface CrudGridItem {

    Boolean isCarrouselPreview();

    List<FileEntity> getFilesEntities();

    String getName();

    String getVisualName();

}
