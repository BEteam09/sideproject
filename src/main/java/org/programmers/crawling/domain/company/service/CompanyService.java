package org.programmers.crawling.domain.company.service;

import org.programmers.crawling.domain.company.entity.Company;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> findAndSaveCompany();

    Optional<Company> findByHomepageUrl(String companyUrl);

    List<Company> saveNewCompanies(List<Company> list);

    List<Company> findAllByNameIn(List<String> list);
}
