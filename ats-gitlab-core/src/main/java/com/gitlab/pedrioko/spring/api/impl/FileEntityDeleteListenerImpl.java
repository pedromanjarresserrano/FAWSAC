package com.gitlab.pedrioko.spring.api.impl;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.spring.api.DeleteListener;
import org.apache.commons.io.FileUtils;
import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zkoss.zul.Messagebox;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@Component
public class FileEntityDeleteListenerImpl implements DeleteListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileEntityDeleteListenerImpl.class);

    public void eventPerform(Object value) {
        if(value instanceof FileEntity){
            String url = ((FileEntity) value).getUrl();
            String replace = url.replace("\\", "\\\\");
            File file = new File(replace);
            try {
                FileUtils.forceDelete(file);
                file.getCanonicalFile().delete();
                Files.delete(Paths.get(replace));
            } catch (NoSuchFileException e1) {
                LOGGER.error("No such file/directory exists");
            } catch (DirectoryNotEmptyException e1) {
                LOGGER.error("Directory is not empty.");
            } catch (IOException e1) {
                LOGGER.error("Invalid permissions.");
            } catch (Exception ee) {
                LOGGER.error("Exception");
                ee.printStackTrace();
            }
        }
    }
}
