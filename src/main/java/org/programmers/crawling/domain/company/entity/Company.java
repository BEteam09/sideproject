package org.programmers.crawling.domain.company.entity;

import jakarta.persistence.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.programmers.crawling.domain.basetime.entity.BaseTimeEntity;
import org.programmers.crawling.domain.post.entity.JobPosting;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseTimeEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "company_seq") // Bulk Insert 를 위해 Seq 생성
  @SequenceGenerator(name = "company_seq", sequenceName = "company_sequence", allocationSize = 50)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(unique = true)
  private String homepageUrl; // 홈페이지가 없을 수 있음

  private String industry; // 테이블 분류?

  private String companySize;
  private String logoUrl;
  private Year foundedDate;
  private String city;

  @OneToMany(
      mappedBy = "company",
      cascade = {CascadeType.MERGE, CascadeType.REMOVE})
  @ToString.Exclude
  private List<JobPosting> jobPostings;

  public void addPosting(JobPosting jobPosting) {
    if (jobPostings == null) {
      jobPostings = new ArrayList<>();
    }
    this.jobPostings.add(jobPosting);
    jobPosting.assignCompany(this);
  }
}
