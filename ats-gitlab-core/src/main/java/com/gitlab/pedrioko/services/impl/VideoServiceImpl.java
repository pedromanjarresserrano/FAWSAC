package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.lang.AppParam;
import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.StorageService;
import com.gitlab.pedrioko.services.VideoService;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.DemuxerTrackMeta;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgcodecs.IMWRITE_JPEG_QUALITY;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

@Service
public class VideoServiceImpl implements VideoService {


    private static final String JGP = ".jpg";
    @Autowired
    private CrudService crudService;
    @Autowired
    private StorageService storageService;

    private OpenCVFrameConverter.ToIplImage converterToIplImage = new OpenCVFrameConverter.ToIplImage();

    @Override
    public List<FileEntity> generatePreviewImage(String filePath, int previewCount) {
        return this.generatePreviewImage(filePath, previewCount, 35);
    }

    @Override
    public List<FileEntity> generatePreviewImage(String filePath, int previewCount, int quality) {
        List<FileEntity> fileEntities = new ArrayList<>();
        try {
            AppParam name = storageService.getAppParam();
            ImageIO.setUseCache(false);
            File file = new File(filePath);
            try {
                avutil.av_log_set_level(avutil.AV_LOG_QUIET);
                FFmpegFrameGrabber g = new FFmpegFrameGrabber(filePath);
                String fileExtension = getFileExtension(file);
                g.setFormat(fileExtension);
                g.start();
                int frames = g.getLengthInFrames();
                int lengthInFrames = frames > previewCount ? (frames / previewCount) : 1;
                if (lengthInFrames <= 0)
                    lengthInFrames = 1;
                for (int i = 0; i < frames; i += lengthInFrames) {
                    String name1 = DigestUtils.md5DigestAsHex(("pv_" + file.getName() + "-" + i).getBytes());
                    File output = new File(name.getValue() + "\\" + name1 + JGP);
                    g.setFrameNumber(i);
                    boolean existFileEntity = storageService.existFileEntity(name1 + JGP);

                    if (!existFileEntity) {
                        if (!output.exists()) {
                            //       dst = ((AWTFrameGrab) awtFrameGrab.seekToSecondPrecise(sec)).getFrameWithOrientation();
                            //        storageService.writeImage(output, dst, "jpg");
                            cvSaveImage(output.getAbsolutePath(), converterToIplImage.convert(g.grabImage()), new int[]{IMWRITE_JPEG_QUALITY, quality, 0});
                        }
                        FileEntity fileEntity = new FileEntity();
                        fileEntity.setFilename(name1 + JGP);
                        fileEntity.setUrl(output.getAbsolutePath());
                        fileEntities.add(crudService.saveOrUpdate(fileEntity));
                    } else {
                        if (!output.exists() && output.length() <= 0) {
                            //       dst = ((AWTFrameGrab) awtFrameGrab.seekToSecondPrecise(sec)).getFrameWithOrientation();
                            //         fileEntities.add(storageService.saveFileImage(dst, name1));
                            cvSaveImage(output.getAbsolutePath(), converterToIplImage.convert(g.grabImage()), new int[]{IMWRITE_JPEG_QUALITY, quality, 0});
                            FileEntity fileEntity = !existFileEntity ? new FileEntity() : storageService.getFileEntities(name1).get(0);
                            fileEntity.setFilename(output.getName());
                            fileEntity.setUrl(output.getAbsolutePath());
                            fileEntities.add(fileEntity);
                        } else {
                            fileEntities.add(storageService.getFileEntity(name1 + JGP));
                        }
                    }
                }
                g.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileEntities;
    }

    private String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            extension = "";
        }

        return extension;

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

    @Override
    public Integer getResolution(String filePath) {
        try {
            FFmpegFrameGrabber g = new FFmpegFrameGrabber(filePath);
            String[] split = filePath.split("\\.");
            g.setFormat(split[split.length - 1]);
            g.start();
            g.setFrameNumber(1);
            int imageHeight = g.getImageHeight();
            g.close();
            return imageHeight;
        } catch (Exception e) {
            return 0;
        }
    }

}
