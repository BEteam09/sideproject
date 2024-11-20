package org.programmers.crawling.domain.post.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.programmers.crawling.domain.company.entity.Company;
import org.programmers.crawling.domain.company.repository.CompanyRepository;
import org.programmers.crawling.domain.post.entity.JobPosting;
import org.programmers.crawling.domain.post.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;

    @Value("${batch-size}")
    private int batchSize;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<JobPosting> saveNewJobPostings(List<JobPosting> jobPostings,
        HashMap<JobPosting, Company> postCompanyMap, Map<String, Company> preloadCompanies) {
        // Step 1: 제목 리스트 가져오기
        final List<String> titleList = jobPostings.stream().map(JobPosting::getTitle).toList();

        // Step 2: 기존 제목 캐싱
        final List<String> existingTitleList = fetchExistingTitlesInBatches(titleList);

        // Step 3: 신규 공고 필터링 및 회사 매칭
        final List<JobPosting> newPostings = jobPostings.stream().filter(t -> {
            if (existingTitleList.contains(t.getTitle())) {
                return false;
            }
            final Company getCom = postCompanyMap.get(t);
            final Company company = preloadCompanies.getOrDefault(getCom.getName(), getCom);
            company.addPosting(t);
            return true;
        }).toList();

        return saveJobPostingInBatches(newPostings);
    }

    // 회사 데이터를 미리 조회하여 맵핑
    private Map<String, Company> preloadCompanies(List<Company> companies) {
        return companyRepository.findAllByNameIn(companies.stream().map(Company::getName).toList())
            .stream().collect(Collectors.toMap(Company::getName, Function.identity()));
    }

    private List<String> fetchExistingTitlesInBatches(List<String> titles) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < titles.size(); i += batchSize) {
            List<String> batch = titles.subList(i, Math.min(i + batchSize, titles.size()));
            result.addAll(jobPostingRepository.findAllByTitleIn(batch).stream()
                .filter(e -> Objects.nonNull(e.getCompany())).map(JobPosting::getTitle).toList());
        }
        return result;
    }

    private List<JobPosting> saveJobPostingInBatches(List<JobPosting> newPostings) {
        final List<JobPosting> list = new ArrayList<>();
        for (int i = 0; i < newPostings.size(); i += batchSize) {
            List<JobPosting> batch = newPostings.subList(i,
                Math.min(i + batchSize, newPostings.size()));
            list.addAll(jobPostingRepository.saveAll(batch));
            jobPostingRepository.flush(); // 플러시하여 DB 반영
        }
        return list;
    }
}
