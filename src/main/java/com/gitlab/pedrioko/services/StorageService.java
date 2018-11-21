package com.gitlab.pedrioko.services;

import com.gitlab.pedrioko.core.lang.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface StorageService {

    String APP_VAR_NAME = "STORAGE_DIR";
    String APP_TEMP_VAR_NAME ="TEMP_STORAGE_DIR" ;

    String getStorageLocation();

    String getTempStorageLocation();

    String getUrlStorageLocation();

    File getFile(String filename);

    String getUUID();

    File getFile(FileEntity filename);

    String getUrlFile(FileEntity filename);

    String getUrlFile(String filename);

    FileEntity saveFile(InputStream inputstream);

    FileEntity saveFileToFileEntity(String filename, InputStream inputstream);

    List<FileEntity> saveZipFileToFileEntity(String filename, InputStream inputstream);

    FileEntity saveFileImage(BufferedImage bufferedImage, String fileName);

    void writeImage(BufferedImage bufferedImage, String fileName, String extension);

    void writeImage(File output, BufferedImage bufferedImage, String extension);

    FileEntity saveFileImage(BufferedImage bufferedImage, String fileName, String extension);

    boolean existFileEntity(String fileName);

    List<FileEntity> getFileEntities(String fileName);

    File saveFile(String name, InputStream inputstream);

    void saveFile(MultipartFile file);
}
