package org.programmers.crawling.domain.post.entity;

import lombok.Getter;

@Getter
public enum JobType {

    PERMANENT("정규"),
    TEMPORARY("계약");

    private final String type;

    JobType(String type) {
        this.type = type;
    }
}
