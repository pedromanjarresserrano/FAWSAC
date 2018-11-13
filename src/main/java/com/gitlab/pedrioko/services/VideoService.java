package com.gitlab.pedrioko.services;

import com.gitlab.pedrioko.core.lang.FileEntity;

import java.util.List;

public interface VideoService {

    List<FileEntity> generatePreviewImage(String filePath);
}
