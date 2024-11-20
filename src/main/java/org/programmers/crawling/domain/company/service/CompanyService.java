package org.programmers.crawling.domain.company.service;

import java.util.List;
import org.programmers.crawling.domain.company.entity.Company;

public interface CompanyService {

    List<Company> saveNewCompanies(List<Company> list);

    List<Company> findAllByNameIn(List<String> list);
}
