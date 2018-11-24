package com.gitlab.pedrioko.core.view.api;

import com.gitlab.pedrioko.core.lang.FileEntity;

import java.util.List;

public interface ChosenItem {

    List<FileEntity> getFilesEntities();

    String getVisualName();
}
