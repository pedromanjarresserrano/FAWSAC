package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.api.impl.FilesStaticResourceLocationsImpl;
import com.gitlab.pedrioko.core.api.impl.StorageStaticResourceLocationsImpl;
import com.gitlab.pedrioko.core.lang.AppParam;
import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.StorageService;
import com.gitlab.pedrioko.spring.ServerListener;
import com.querydsl.core.types.dsl.PathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service("storageservice")
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);
    @Autowired
    private ServerListener serverListener;
    @Autowired
    private CrudService crudService;

    private byte[] buffer = new byte[2048];

    @PostConstruct
    private void init() {
        AppParam fetchFirst = getAppParam();
        if (fetchFirst == null) {
            fetchFirst = new AppParam(0, StorageService.APP_VAR_NAME, "");
            crudService.saveOrUpdate(fetchFirst);
        }

        fetchFirst = getTempAppParam();
        if (fetchFirst == null) {
            fetchFirst = new AppParam(0, StorageService.APP_TEMP_VAR_NAME, "");
            crudService.saveOrUpdate(fetchFirst);
        }
    }

    private AppParam getAppParam() {
        PathBuilder<?> path = crudService.getPathBuilder(AppParam.class);
        return (AppParam) crudService.query().from(path)
                .where(path.getString("name").eq(StorageService.APP_VAR_NAME)).fetchFirst();

    }

    private AppParam getTempAppParam() {
        PathBuilder<?> path = crudService.getPathBuilder(AppParam.class);
        return (AppParam) crudService.query().from(path)
                .where(path.getString("name").eq(StorageService.APP_TEMP_VAR_NAME)).fetchFirst();

    }

    private Path getPathTempAppParam() {
        return Paths.get(getTempAppParam().getValue());
    }

    @Override
    public String getStorageLocation() {
        return getAppParam().getValue();
    }

    @Override
    public String getTempStorageLocation() {
        return getTempAppParam().getValue();
    }

    @Override
    public String getUrlStorageLocation() {
        return "" + StorageStaticResourceLocationsImpl.STORAGE_PATH;
    }

    @Override
    public File getFile(String filename) {
        String value = getAppParam().getValue();
        return new File(value + filename.replace(value, ""));
    }

    @Override
    public String getUUID() {
        return UUID.randomUUID().toString();
    }


    @Override
    public File getFile(FileEntity filename) {
        return getFile(filename.getUrl());
    }

    @Override
    public String getUrlFile(FileEntity filename) {
        String url = filename.getUrl();
        String replace = url.replace(getStorageLocation(), "");
        if (replace.equalsIgnoreCase(url))
            replace = url.replace(getTempStorageLocation(), "");
        replace = replace.replace("\\", "/");
        if (replace.indexOf("/") == 0) {
            replace = replace.substring(1);
        }
        return filename != null ? getUrlFile(replace) : "";
    }

    @Override
    public String getUrlFile(String filename) {
        return getUrlFile(filename, false);
    }

    @Override
    public String getUrlFile(String filename, Boolean statics) {
        String replace = filename.replace(getStorageLocation(), "").replace("\\", "/");
        if (replace.startsWith("\\"))
            replace = replace.substring(1);
        return ((statics ? FilesStaticResourceLocationsImpl.STATIC_FILES_PATH : getUrlStorageLocation()) + replace).replace("//", "/");
    }

    @Override
    public FileEntity saveFile(InputStream inputstream) {
        String uuid = getUUID();
        File file = saveFile(uuid, inputstream);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(uuid);
        fileEntity.setUrl(file.getAbsolutePath());
        return fileEntity;
    }

    @Override
    public FileEntity saveFileToFileEntity(String filename, InputStream inputstream) {
        File file = saveFile(filename, inputstream);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);
        fileEntity.setUrl(file.getAbsolutePath());
        fileEntity.setCreationDate(new Date());
        return fileEntity;
    }

    @Override
    public List<FileEntity> saveZipFileToFileEntity(String filename, InputStream inputstream) {
        List<FileEntity> fileList = new ArrayList<>();
        try {
            ZipInputStream stream = new ZipInputStream(inputstream);
            Path outDir = getPathTempAppParam();
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                Path filePath = outDir.resolve(getUUID() + entry.getName());
                File file = filePath.toFile();
                try (FileOutputStream fos = new FileOutputStream(file);
                     BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length)) {
                    int len;
                    while ((len = stream.read(buffer)) > 0) {
                        bos.write(buffer, 0, len);
                    }
                    fileList.add(saveFileToFileEntity(file.getName(), new FileInputStream(file)));
                    try {
                        Files.move(filePath, Paths.get(getAppParam().getValue()));
                    } catch (Exception w) {
                        LOGGER.error("ERROR", w);
                    }
                    file.delete();
                }
            }
        } catch (Exception e) {
            LOGGER.error("ERROR", e);

        }
        return fileList;
    }


    @Override
    public FileEntity saveFileImage(BufferedImage bufferedImage, String fileName) {
        return saveFileImage(bufferedImage, fileName, "jpg");
    }

    @Override
    public void writeImage(BufferedImage bufferedImage, String fileName, String extension) {
        File output = new File(getStorageLocation() + "\\" + fileName + "." + extension);
        writeImage(output, bufferedImage, extension);

    }

    @Override
    public void writeImage(File output, BufferedImage bufferedImage, String extension) {
        try {
            BufferedOutputStream imageOutputStream = new BufferedOutputStream(new FileOutputStream(output));
            ImageIO.write(bufferedImage, extension, imageOutputStream);
            imageOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("ERROR", e);
        }
    }

    @Override
    public FileEntity saveFileImage(BufferedImage bufferedImage, String fileName, String extension) {
        File output = new File(getStorageLocation() + "\\" + fileName + "." + extension);

        FileEntity fileEntity = !existFileEntity(fileName) ? new FileEntity() : getFileEntities(fileName).get(0);
        if (!output.exists()) {
            try {
                ImageIO.write(bufferedImage, extension, output);
            } catch (IOException e) {
                LOGGER.error("ERROR", e);
            }
        }

        fileEntity.setFilename(output.getName());
        fileEntity.setUrl(output.getAbsolutePath());
        return fileEntity;
    }

    @Override
    public boolean existFileEntity(String fileName) {
        List<FileEntity> likePrecise = getFileEntities(fileName);
        return !(likePrecise == null || likePrecise.isEmpty());
    }

    @Override
    public FileEntity getFileEntity(String fileName) {
        List<FileEntity> likePrecise = getFileEntities(fileName);
        return !(likePrecise == null || likePrecise.isEmpty()) ? likePrecise.get(0) : null;
    }

    @Override
    public List<FileEntity> getFileEntities(String fileName) {
        return crudService.getLikePrecise(FileEntity.class, fileName);
    }


    @Override
    public File saveFile(String filename, InputStream inputstream) {
        String value = getAppParam().getValue();
        OutputStream outputStream = null;
        File banner = new File(value + "/" + filename);
        try (FileOutputStream fos = new FileOutputStream(banner)) {
            InputStream inputStream = inputstream;
            outputStream = fos;
            byte[] buffer = new byte[50000];
            for (int count; (count = inputStream.read(buffer)) != -1; ) {
                outputStream.write(buffer, 0, count);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            LOGGER.error("ERROR ON action()", e);
        }
        return banner;
    }

    @Override
    public void saveFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                //      throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
            /*    throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);*/
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(getAppParam().getValue() + "/" + filename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            //   throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public File getNewFile(String s) {
        return new File(getStorageLocation() + "\\" + s);
    }
}
