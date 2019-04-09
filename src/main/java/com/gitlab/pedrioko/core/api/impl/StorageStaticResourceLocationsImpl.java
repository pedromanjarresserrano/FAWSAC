package com.gitlab.pedrioko.core.api.impl;

import com.gitlab.pedrioko.core.api.StaticResouceLocation;
import com.gitlab.pedrioko.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageStaticResourceLocationsImpl implements StaticResouceLocation {
    public static final String STORAGE_PATH = "/storage/files/";

    @Autowired
    private StorageService storageService;

    @Override
    public String getPath() {
        return STORAGE_PATH + "**";
    }

    @Override
    public String[] getLocations() {
        String storageLocation = storageService.getStorageLocation();
        String replace = storageLocation.replace("\\", "/");
        if (!replace.endsWith("/"))
            replace += "/";
        return new String[]{"file:/" + replace};
    }
}
