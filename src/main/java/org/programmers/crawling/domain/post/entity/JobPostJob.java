package org.programmers.crawling.domain.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.crawling.domain.basetime.entity.BaseTimeEntity;
import org.programmers.crawling.domain.job.entity.Job;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JobPostJob extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosing;
}