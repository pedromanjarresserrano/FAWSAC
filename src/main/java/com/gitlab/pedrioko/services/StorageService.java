package com.gitlab.pedrioko.services;

import com.gitlab.pedrioko.core.lang.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface StorageService {

    String APP_VAR_NAME = "STORAGE_DIR";

    String getStorageLocation();

    String getUrlStorageLocation();

    File getFile(String filename);

    File getFile(FileEntity filename);

    String getUrlFile(FileEntity filename);

    String getUrlFile(String filename);

    File saveFile(String name, InputStream inputstream);

    void saveFile(MultipartFile file);
}
