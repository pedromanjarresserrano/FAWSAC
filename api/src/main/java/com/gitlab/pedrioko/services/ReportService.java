package com.gitlab.pedrioko.services;

import com.gitlab.pedrioko.core.lang.Report;

import java.io.File;
import java.util.Collection;


public interface ReportService {

    File processReport(Report report, Collection datasource) throws Exception;


}
