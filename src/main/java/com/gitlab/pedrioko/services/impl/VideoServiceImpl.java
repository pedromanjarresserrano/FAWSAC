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
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {


    private static final String JGP = ".jpg";
    @Autowired
    private CrudService crudService;
    @Autowired
    private StorageService storageService;

    @Override
    public List<FileEntity> generatePreviewImage(String filePath, int previewCount) {
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
            Long v = Math.round(totalDuration / previewCount);
            FileChannelWrapper ch = null;
            File file = new File(filePath);
            try {
                ch = NIOUtils.readableChannel(file);
                AWTFrameGrab awtFrameGrab = AWTFrameGrab.createAWTFrameGrab(ch);
                for (int i = 0; i < previewCount; i++) {
                    String name1 = DigestUtils.md5DigestAsHex(("pv_" + file.getName() + "-" + i).getBytes());
                    File output = new File(name.getValue() + "\\" + name1 + JGP);

                    BufferedImage dst = null;
                    boolean existFileEntity = storageService.existFileEntity(name1 + JGP);
                    if (existFileEntity) {
                        if (!output.exists()) {
                            dst = ((AWTFrameGrab) awtFrameGrab.seekToSecondPrecise(sec)).getFrameWithOrientation();
                            storageService.writeImage(output, dst, "jpg");
                        }
                        fileEntities.add(storageService.getFileEntity(name1 + JGP));
                    } else {
                        dst = ((AWTFrameGrab) awtFrameGrab.seekToSecondPrecise(sec)).getFrameWithOrientation();
                        fileEntities.add(storageService.saveFileImage(dst, name1));
                    }
                    sec += v;
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

    @Override
    public List<FileEntity> generatePreviewImage(String filePath) {
        return generatePreviewImage(filePath, 10);
    }

    @Override
    public double getTime(String filePath) {
        try {
            SeekableByteChannel bc = NIOUtils.readableFileChannel(filePath);
            MP4Demuxer dm = MP4Demuxer.createMP4Demuxer(bc);
            DemuxerTrack vt = dm.getVideoTrack();
            DemuxerTrackMeta meta = vt.getMeta();
            return meta.getTotalDuration();
        } catch (Exception e) {
            return 0D;
        }
    }

}
