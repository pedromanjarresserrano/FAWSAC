package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.lang.AppParam;
import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.StorageService;
import com.gitlab.pedrioko.services.VideoService;
import com.querydsl.core.types.dsl.PathBuilder;
import org.jcodec.api.awt.AWTFrameGrab;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.DemuxerTrackMeta;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {


    @Autowired
    private CrudService crudService;

    @Override
    public List<FileEntity> generatePreviewImage(String filePath) {
        List<FileEntity> fileEntities = new ArrayList<>();
        try {
            Long sec = 1L;
            PathBuilder<?> path = crudService.getPathBuilder(AppParam.class);
            AppParam name = (AppParam) crudService.query().from(path).where(path.getString("name").eq(StorageService.APP_VAR_NAME)).fetchFirst();
            ImageIO.setUseCache(false);
            //where filePath is the path of video file and previeFileName is the name of preview image file.
            SeekableByteChannel bc = NIOUtils.readableFileChannel(filePath);
            MP4Demuxer dm = MP4Demuxer.createMP4Demuxer(bc);
            DemuxerTrack vt = dm.getVideoTrack();
            DemuxerTrackMeta meta = vt.getMeta();
            double totalDuration = meta.getTotalDuration();
            Long v = Math.round(totalDuration / 10L);
            FileChannelWrapper ch = null;
            File file = new File(filePath);
            try {
                ch = NIOUtils.readableChannel(file);
                AWTFrameGrab awtFrameGrab = AWTFrameGrab.createAWTFrameGrab(ch);
                for (int i = 0; i < 10; i++) {
                    File output = new File(name.getValue() + "\\preview_" + file.getName() + "-" + i + ".jpg");
                    if (!output.exists()) {
                        BufferedImage dst = ((AWTFrameGrab) awtFrameGrab.seekToSecondPrecise(sec)).getFrameWithOrientation();
                        ImageIO.write(dst, "jpg", output);
                        sec += v;
                        FileEntity e = new FileEntity();
                        e.setFilename(output.getName());
                        e.setUrl(output.getAbsolutePath());
                        fileEntities.add(e);
                    } else {
                        FileEntity e = new FileEntity();
                        e.setFilename(output.getName());
                        e.setUrl(output.getAbsolutePath());
                        fileEntities.add(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                NIOUtils.closeQuietly(ch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileEntities;
    }

}
