package com.gitlab.pedrioko.core.api.impl;

import com.gitlab.pedrioko.core.api.StaticResouceLocation;
import com.gitlab.pedrioko.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FilesStaticResourceLocationsImpl implements StaticResouceLocation {
    public static final String STATIC_FILES_PATH = "/statics/files/";
    public static final String STATIC_FILES_LOCATION = "file:/F://Cache-Media/";

    @Override
    public String getPath() {
        return STATIC_FILES_PATH + "**";
    }

    @Override
    public String[] getLocations() {
        return new String[]{STATIC_FILES_LOCATION,"file:/E://Archivos De Programas/Cache/"};
    }
}
