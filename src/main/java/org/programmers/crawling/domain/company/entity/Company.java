package org.programmers.crawling.domain.company.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.crawling.domain.basetime.entity.BaseTimeEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Company extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String homepageUrl;

    // TODO: 요거는 종류가 되게 많을 것이라 생각해서, ENUM으로 지정하지 않고, String으로 지정했습니다.
    private String industry;

    private Integer companySize;
    private String logoUrl;
    private LocalDateTime foundedDate;
    private String city;
}
