package org.programmers.crawling.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.crawling.domain.crawling.service.CrawlingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 컨트롤러 입니다.
 */
@RestController
@RequestMapping("crawling")
@RequiredArgsConstructor
public class CrawlingController {

  private final CrawlingService crawlingService;

  @GetMapping()
  public String crawl() {
    crawlingService.crawlAndSaveJobPostings();
    return "crawling";
  }
}
