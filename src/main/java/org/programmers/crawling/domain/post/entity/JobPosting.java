package org.programmers.crawling.domain.post.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.programmers.crawling.domain.basetime.entity.BaseTimeEntity;
import org.programmers.crawling.domain.company.entity.Company;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
public class JobPosting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_posting_seq")
    // Bulk Insert 를 위해 Seq 생성
    @SequenceGenerator(name = "job_posting_seq", sequenceName = "job_posting_sequence", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @ToString.Exclude // 순환 참조 방지
    private Company company;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String description;

    private Long salary; // 0이면 추후협의

    @Column
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    //    private Long experienceLevel;
    private Long minExperience; // 0이면 신입
    private Long maxExperience; // 0이면 상한 없음 (신입 이상, 3년 이상, ...)

    @Column(nullable = true)
    private LocalDateTime postedDate;

    @Column(nullable = true)
    private LocalDateTime closingDate; // null이면 상시채용

    public void assignCompany(Company company) {
        this.company = company;
    }

}
