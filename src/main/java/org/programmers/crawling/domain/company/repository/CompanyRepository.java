package org.programmers.crawling.domain.company.repository;

import java.util.List;
import org.programmers.crawling.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByNameIn(List<String> nameList);
}
