package org.programmers.crawling.domain.user.entity;

import lombok.Getter;

@Getter
public enum Career {

    JUNIOR("신입"),
    EXPERIENCED("경력"),
    EXPERIENCED_JUNIOR("경력신입");

    private final String status;

    Career(String status) {
        this.status = status;
    }
}
