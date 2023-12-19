package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.List;

import SoftveroveInzinierstvoTim5.RestAPI.dto.CompanyDTO;

public interface CompanyService {
    CompanyDTO saveCompany(CompanyDTO company);
    boolean deleteCompany(final Integer id_company);
    List<CompanyDTO> getAllCompanies();
    CompanyDTO getCompanyId(final Integer id_company);
} 
