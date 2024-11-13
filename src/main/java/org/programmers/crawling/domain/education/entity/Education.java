package org.programmers.crawling.domain.education.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.crawling.domain.basetime.entity.BaseTimeEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Education extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String education;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String admissionDate;

    @Column(nullable = false)
    private String graduateDate;
}
