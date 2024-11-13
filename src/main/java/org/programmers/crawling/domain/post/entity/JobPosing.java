package org.programmers.crawling.domain.post.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.crawling.domain.basetime.entity.BaseTimeEntity;
import org.programmers.crawling.domain.company.entity.Company;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JobPosing extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false)
    private String title;

    private String description;

    // TODO: BIGINT + nullable로 되어있어서 Long 타입으로 지정했음.
    private Long salary;

    // TODO: ENUM으로 지정
    @Column
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    // TODO: 경력인데,, BIGINT로 되어있어서, Long 타입으로 지정했음. n년차를 저장하는 필드인지? 궁금
    private Long experienceLevel;

    @Column(nullable = false)
    private LocalDateTime postedDate;

    @Column(nullable = false)
    private LocalDateTime closingDate;
}
