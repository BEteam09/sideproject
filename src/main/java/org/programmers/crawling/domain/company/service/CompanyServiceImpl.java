package org.programmers.crawling.domain.company.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.crawling.domain.company.entity.Company;
import org.programmers.crawling.domain.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;

  @Value("${batch-size}")
  private int batchSize;

  @Override
  public List<Company> findAndSaveCompany() {

    return List.of();
  }

  @Override
  public Optional<Company> findByHomepageUrl(String companyUrl) {
    return companyRepository.findByHomepageUrl(companyUrl);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<Company> saveNewCompanies(List<Company> findCompanies) {
    // Step 1: 크롤링한 회사 이름 목록
    final List<String> nameList = findCompanies.stream().map(Company::getName).toList();

    // Step 2: 이미 저장되어 있는 회사 목록 (회사 이름으로 검색)
    final Set<String> existingNameSet =
        companyRepository.findAllByNameIn(nameList).stream()
            .map(Company::getName)
            .collect(Collectors.toSet());

    // Step 3: 저장되어 있지 않는 회사 목록 (회사 이름으로 비교)
    final List<Company> newCompanies =
        findCompanies.stream()
            .filter(company -> !existingNameSet.contains(company.getName()))
            .toList();

    return saveCompanyInBatches(newCompanies); // Batch size: 50
  }

  @Override
  public List<Company> findAllByNameIn(List<String> nameList) {
    return companyRepository.findAllByNameIn(nameList);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<Company> saveCompanyInBatches(List<Company> newCompanies) {
    List<Company> companies = new java.util.ArrayList<>();
    for (int i = 0; i < newCompanies.size(); i += batchSize) {
      List<Company> batch = newCompanies.subList(i, Math.min(i + batchSize, newCompanies.size()));
      companies.addAll(companyRepository.saveAll(batch));
      companyRepository.flush(); // Ensure changes are written to the database
    }
    return companies;
  }
}
