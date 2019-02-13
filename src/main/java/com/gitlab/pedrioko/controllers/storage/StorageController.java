package com.gitlab.pedrioko.controllers.storage;

import com.gitlab.pedrioko.core.view.api.CrudGridItem;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController("/sc")
public class StorageController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private CrudService crudService;

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public Mono<?> handleRequest(@RequestParam String id, @RequestParam String clase) throws Exception {
        List<?> entities = ApplicationContextUtils.getEntities();
        List<?> collect = entities.stream().filter(e -> (((Class) e).getSimpleName().equalsIgnoreCase(clase))).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Object o = collect.get(0);
            CrudGridItem byId = (CrudGridItem) crudService.getById((Class) o, Long.parseLong(id));
            File file = new File(byId.getLocal());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return Mono.justOrEmpty(resource);
        } else
            return null;
      /*  File file = new File(filename);
        long len = 0;
        try {
            len = file.length();
        } catch (Exception e) {
            e.printStackTrace();
        }


        MediaType mediaType = MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(file));

        if (filename.toLowerCase().endsWith("mp4") || filename.toLowerCase().endsWith("mp3") ||
                filename.toLowerCase().endsWith("3gp") || filename.toLowerCase().endsWith("mpeg") ||
                filename.toLowerCase().endsWith("mpeg4"))
            mediaType = MediaType.parseMediaType("application/octet-stream");


        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(len)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);*/
    }
}
