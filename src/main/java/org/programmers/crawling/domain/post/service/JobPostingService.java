package org.programmers.crawling.domain.post.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.programmers.crawling.domain.company.entity.Company;
import org.programmers.crawling.domain.post.entity.JobPosting;

public interface JobPostingService {

    List<JobPosting> saveNewJobPostings(List<JobPosting> jobPostings,
        HashMap<JobPosting, Company> postCompanyMap, Map<String, Company> preloadCompanies);
}
