package org.programmers.crawling.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.crawling.domain.basetime.entity.BaseTimeEntity;
import org.programmers.crawling.domain.education.entity.Education;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    // TODO: 소셜 로그인의 경우 password를 저장하지 않기에, nullable로 설정함.
    private String password;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    // TODO: ENUM으로 지정
    @Column
    @Enumerated(EnumType.STRING)
    private Career career;

    // TODO: 기존 유저 - 학력 간 1:n 관계를 1:1로 수정함. (자소설닷컴 기준 최종 학력 하나만 저장하여, 이를 기반으로 변경한 것.)
    @OneToOne
    @JoinColumn(name = "education_id")
    private Education education;
}