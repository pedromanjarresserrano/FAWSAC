package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.lang.Report;
import com.gitlab.pedrioko.services.ReportService;
import com.gitlab.pedrioko.services.StorageService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DAOGenericImpl.class);


    @Autowired
    private StorageService storageService;

    public File processReport(Report report, Collection datasource) throws Exception {
        JRBeanCollectionDataSource beanBurritoWrap = new JRBeanCollectionDataSource(datasource);
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        HashMap<String, Object> hashMap = new HashMap<>();
        File newFile = storageService.getNewFile(report.getNombre() + UUID.randomUUID().toString() + ".pdf");
        try (OutputStream outputStream = new FileOutputStream(newFile)) {
            jasperReport = JasperCompileManager.compileReport(report.getJasperFile().getUrl());
            jasperPrint = JasperFillManager.fillReport(jasperReport, hashMap, beanBurritoWrap);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (JRException e) {
            LOGGER.error("Error", e);
        }
        return newFile;
    }


}
