package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.List;

import SoftveroveInzinierstvoTim5.RestAPI.dto.ReportDTO;

public interface ReportService {
    ReportDTO saveReport(ReportDTO report);
    boolean deleteReport(final Integer idreport);
    List<ReportDTO> getAllReports();
    ReportDTO getReportById(final Integer idreport);
}
