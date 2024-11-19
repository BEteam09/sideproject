package org.programmers.crawling.domain.company.repository;

import org.programmers.crawling.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByHomepageUrl(String companyUrl);

    List<Company> findAllByNameIn(List<String> nameList);
}
