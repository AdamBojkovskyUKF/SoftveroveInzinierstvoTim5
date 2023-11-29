package SoftveroveInzinierstvoTim5.RestAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import SoftveroveInzinierstvoTim5.RestAPI.dto.CompanyDTO;
import SoftveroveInzinierstvoTim5.RestAPI.model.Company;
import SoftveroveInzinierstvoTim5.RestAPI.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;

public class DefaultCompanyService implements CompanyService{

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public CompanyDTO saveCompany(CompanyDTO company) {
        Company companyModel = populateCompanyEntity(company);
        return populateCompanyData(companyRepository.save(companyModel));
    }

    @Override
    public boolean deleteCompany(Integer id_company) {
        companyRepository.deleteById(id_company);
        return true;
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> companies = new ArrayList<>();
        Iterable <Company> companyList = companyRepository.findAll();
        companyList.forEach(company -> {
            companies.add(populateCompanyData(company));
        });
        return companies;
    }

    @Override
    public CompanyDTO getCompanyId(Integer id_company) {
        return populateCompanyData(companyRepository.findById(id_company).orElseThrow(() -> new EntityNotFoundException("Company not found")));
    }

    private CompanyDTO populateCompanyData(final Company company) {
        CompanyDTO companyData = new CompanyDTO();
        companyData.setRepresentative_id_person(company.getRepresentative_id_person());
        companyData.setName(company.getName());
        companyData.setAddress(company.getAddress());
        return companyData;
    }

    private Company populateCompanyEntity(CompanyDTO companyData) {
        Company company = new Company();
        company.setRepresentative_id_person(companyData.getRepresentative_id_person());
        company.setName(companyData.getName());
        company.setAddress(companyData.getAddress());
        return company;
    }
    
}
