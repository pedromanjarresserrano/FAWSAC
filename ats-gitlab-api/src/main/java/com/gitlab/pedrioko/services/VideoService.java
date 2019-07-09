package com.gitlab.pedrioko.services;

import com.gitlab.pedrioko.core.lang.FileEntity;

import java.util.List;

public interface VideoService {

    List<FileEntity> generatePreviewImage(String filePath, int previewCount);

    List<FileEntity> generatePreviewImage(String filePath, int previewCount, int quality);

    List<FileEntity> generatePreviewImage(String filePath);

    double getTime(String filePath);

    Integer getResolution(String filePath);
}
