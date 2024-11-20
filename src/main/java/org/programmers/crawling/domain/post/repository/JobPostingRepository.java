package org.programmers.crawling.domain.post.repository;

import org.programmers.crawling.domain.post.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    Collection<JobPosting> findAllByTitleIn(List<String> batch);
}
