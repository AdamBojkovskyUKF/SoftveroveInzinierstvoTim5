package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import SoftveroveInzinierstvoTim5.RestAPI.dto.ReportDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Report;
import SoftveroveInzinierstvoTim5.RestAPI.repository.ReportRepository;
import jakarta.persistence.EntityNotFoundException;

public class DefaultReportService implements ReportService{

    @Autowired
    ReportRepository reportRepository;

    @Override
    public ReportDTO saveReport(ReportDTO report) {
        Report reportModel = populateReportEntity(report);
        return populateReportData(reportRepository.save(reportModel));
    }

    @Override
    public boolean deleteReport(Integer idreport) {
        reportRepository.deleteById(idreport);
        return true;   
    }    

    @Override
    public List<ReportDTO> getAllReports() {
        List<ReportDTO> reports = new ArrayList<>();
        Iterable <Report> reportList = reportRepository.findAll();
        reportList.forEach(report -> {
            reports.add(populateReportData(report));
        });
        return reports;
    }

    @Override
    public ReportDTO getReportById(Integer idreport) {
        return populateReportData(reportRepository.findById(idreport).orElseThrow(() -> new EntityNotFoundException("Report not found")));
    }

    private ReportDTO populateReportData(final Report report) {
        ReportDTO reportData = new ReportDTO();
        reportData.setCreatoraccount_id_account(report.getCreatoraccount_id_account());
        reportData.setContent(report.getContent());
        reportData.setTimestamp(report.getTimestamp());
        reportData.setType(report.getType());
        return reportData;
    }

    private Report populateReportEntity(ReportDTO reportData) {
        Report report = new Report();
        report.setCreatoraccount_id_account(reportData.getCreatoraccount_id_account());
        report.setContent(reportData.getContent());
        report.setTimestamp(reportData.getTimestamp());
        report.setType(reportData.getType());
        return report;
    }
    
}
