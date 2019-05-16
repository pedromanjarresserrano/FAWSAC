package com.gitlab.pedrioko.core.api.impl;

import com.gitlab.pedrioko.core.api.StaticResouceLocation;
import com.gitlab.pedrioko.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FilesStaticResourceLocationsImpl implements StaticResouceLocation {
    public static final String STATIC_FILES_PATH = "/statics/files/";
    private static final String FILE_URI = "file:/";
    public static final String STATIC_FILES_LOCATION = FILE_URI + "F://Cache-Media/";

    @Override
    public String getPath() {
        return STATIC_FILES_PATH + "**";
    }

    @Override
    public String[] getLocations() {
        return new String[]{STATIC_FILES_LOCATION, FILE_URI + "E://Archivos De Programas/Cache/", FILE_URI + "I://cache-media/", FILE_URI + "I://test/"};
    }
}
