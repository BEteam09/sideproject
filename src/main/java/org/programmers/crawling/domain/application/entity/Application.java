package org.programmers.crawling.domain.application.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.programmers.crawling.domain.post.entity.JobPosing;
import org.programmers.crawling.domain.user.entity.User;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id")
    private JobPosing jobPosing;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime appliedAt;
}
