package org.programmers.crawling.domain.crawling.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.programmers.crawling.domain.company.entity.Company;
import org.programmers.crawling.domain.company.service.CompanyService;
import org.programmers.crawling.domain.post.entity.JobPosting;
import org.programmers.crawling.domain.post.entity.JobType;
import org.programmers.crawling.domain.post.service.JobPostingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CrawlingServiceImpl implements CrawlingService {

  public static final String URL =
      "https://www.wanted.co.kr/wdlist?country=kr&job_sort=job.recommend_order&years=-1&locations=all";
  public static final String POST_TITLE_TAG = "JobHeader_JobHeader__PositionName__kfauc";
  public static final String COMPANY_NAME_TAG = "JobHeader_JobHeader__Tools__Company__Link__zAvYv";
  public static final String CLOSING_DATE_TAG = "wds-lgio6k";
  public static final String DESC_TAG = "wds-wcfcu3";
  public static final String EXPERIENCE_LV_TAG = "JobHeader_JobHeader__Tools__Company__Info__yT4OD";
  public static final String SALARY_TAG = "wds-yh9s95";
  public static final String COMPANY_INFO_TAG = "CompanyInfo_CompanyInfo__Text__oLsJ6";

  private final CompanyService companyService;
  private final JobPostingService jobPostingService;

  private static Company getCompany(WebDriver driver, String companyName, String companyUrl) {
    final String city = driver.findElements(By.className(COMPANY_INFO_TAG)).get(1).getText();
    final String companySize =
        driver.findElements(By.className("CompanyTagList_CompanyTagList__tag__hb_2Y")).stream()
            .map(WebElement::getText)
            .filter(text -> text.contains("명"))
            .findFirst()
            .orElse(null); // "명"을 포함하는 텍스트만 필터링
    final String foundedDate = driver.findElement(By.tagName("time")).getAttribute("datetime");
    final String industry = driver.findElements(By.className(COMPANY_INFO_TAG)).get(0).getText();
    return Company.builder()
        .companySize(companySize)
        .foundedDate(Year.parse(foundedDate))
        .name(companyName)
        .city(city)
        .homepageUrl(companyUrl)
        .industry(industry)
        .build();
  }

  private static JobPosting getPosting(
      String salary,
      String postTitle,
      String desc,
      String closingDate,
      JobType jobType,
      String minEx,
      String maxEx) {
    return JobPosting.builder()
        .salary(Long.parseLong(salary.isEmpty() ? "0" : salary))
        .title(postTitle)
        .description(desc)
        .closingDate(
            closingDate.equals("상시채용")
                ? null
                : LocalDateTime.parse(
                    closingDate + " 00:00:00", DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")))
        .jobType(jobType)
        .minExperience(Long.parseLong(minEx.isEmpty() ? "0" : minEx))
        .maxExperience(Long.parseLong(maxEx.isEmpty() ? "0" : maxEx))
        .build();
  }

  private static String getSalary(String salaryStr) {
    String salary = "";
    for (char c : salaryStr.toCharArray()) {
      // salaryStr -> 3,123만원
      try {
        final long l = Long.parseLong(String.valueOf(c));
        salary += l;
      } catch (NumberFormatException e) {
        log.debug("Not a number : {}", c); // 숫자가 아닌 것은 패스
      }
    }
    return salary;
  }

  public void scrollToEnd(WebDriver driver, int times, int delayMillis) {
    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

    for (int i = 0; i < times; i++) {
      jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);"); // 페이지 끝까지 스크롤
      try {
        Thread.sleep(delayMillis); // 짧은 지연 시간
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        log.error("Scroll thread interrupted {}", e.getMessage());
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void crawlAndSaveJobPostings() {
    WebDriverManager.chromedriver().setup();
    final ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    options.addArguments("--disable-popup-blocking");
    options.addArguments("--window-size=1920,1080");
    options.addArguments(
        "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.20 Safari/537.36");
    // 헤드리스 모드에서는 기본 창 크기가 작게 설정되어 있어, 일부 요소가 화면에 표시되지 않을 수 있습니다. 이를 방지하기 위해 브라우저 창 크기를 명시적으로 설정합니다.

    final WebDriver driver = new ChromeDriver(options);
    final Map<String, Company> companyMap = new HashMap<>();
    final List<JobPosting> jobPostings = new ArrayList<>();
    final HashMap<JobPosting, Company> postCompanyMap = new HashMap<>();

    try {
      driver.get(URL);

      final WebDriverWait wait =
          new WebDriverWait(driver, Duration.ofSeconds(15)); // 찾는 요소가 나올때 까지 Wait 걸어줌.
      wait.until(
          ExpectedConditions.visibilityOfElementLocated(By.className("JobCard_JobCard__Tb7pI")));

      scrollToEnd(driver, 10, 500);

      final List<WebElement> elements = driver.findElements(By.className("JobCard_JobCard__Tb7pI"));
      int errCnt = 0;
      for (WebElement element : elements) {
        try {
          final String jobUrl = element.findElement(By.tagName("a")).getAttribute("href");

          // 새 탭에서 링크 열기
          ((JavascriptExecutor) driver).executeScript("window.open('" + jobUrl + "', '_blank');");

          // 새 탭으로 이동
          ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
          driver.switchTo().window(tabs.get(1)); // 두 번째 탭

          wait.until(
              ExpectedConditions.visibilityOfElementLocated(By.className(EXPERIENCE_LV_TAG)));
          final String closingDate = driver.findElement(By.className(CLOSING_DATE_TAG)).getText();
          final String desc = driver.findElements(By.className(DESC_TAG)).get(1).getText(); // 주요 업무
          final String exLv = driver.findElements(By.className(EXPERIENCE_LV_TAG)).get(1).getText();

          wait.until(
              ExpectedConditions.visibilityOfElementLocated(
                  By.className(POST_TITLE_TAG))); // 노서치가 자주 발생해서 wait 걸어줌.
          final String postTitle = driver.findElement(By.className(POST_TITLE_TAG)).getText();
          final JobType jobType = postTitle.contains("계약직") ? JobType.TEMPORARY : JobType.PERMANENT;
          // final String postedDate = ""; // wanted 엔 postedDate 가 안 보입니다....

          boolean dash = false;
          String minEx = "";
          String maxEx = "";
          for (char c : exLv.toCharArray()) {
            if (c >= 48 && c <= 57) {
              if (!dash) {
                minEx += c; // 최소 경력
              } else {
                maxEx += c; // 최대 경력
              }
            }
            if (c == '-') {
              dash = true;
            }
          }

          wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(COMPANY_NAME_TAG)));
          final String descLink =
              driver.findElement(By.className(COMPANY_NAME_TAG)).getAttribute("href");
          final String companyName = driver.findElement(By.className(COMPANY_NAME_TAG)).getText();
          log.info("COMPANY : {}", companyName);
          // 입사자 평균 연봉 가져오기 위한 링크 -> 연봉이 안 써있음...
          ((JavascriptExecutor) driver).executeScript("window.open('" + descLink + "', '_blank');");
          tabs = new ArrayList<>(driver.getWindowHandles());
          driver.switchTo().window(tabs.get(2)); // 세 번째 탭

          wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(SALARY_TAG)));
          final List<WebElement> salaryElements = driver.findElements(By.className(SALARY_TAG));
          log.debug(
              "Salary Elements size : {}", salaryElements.size()); // 에러가 자주 발생하여 로그로 남겨놓기.....

          final String salaryStr = salaryElements.get(1).getText();
          String salary = getSalary(salaryStr);

          String companyUrl = null;
          try {
            driver.findElement(By.className("CompanyInfo_CompanyInfo__ShowMore__FX21j")).click();
            companyUrl =
                driver
                    .findElement(By.className("CompanyInfo_CompanyInfo__Modal__Contents__JBQGq"))
                    .findElement(By.tagName("a"))
                    .getAttribute("href");
          } catch (Exception e) {
            log.warn("Not Found Company Page : {}", companyName);
          }

          final Company company = getCompany(driver, companyName, companyUrl);
          final String key = companyName + "_" + companyUrl;
          companyMap.putIfAbsent(key, company);

          final JobPosting posting =
              getPosting(salary, postTitle, desc, closingDate, jobType, minEx, maxEx);

          jobPostings.add(posting);
          postCompanyMap.put(posting, company);

          driver.close(); // 현재 탭 닫기
          driver.switchTo().window(tabs.get(1)); // 원래 탭으로 돌아가기
          driver.close();
          driver.switchTo().window(tabs.get(0)); // 원래 탭으로 돌아가기
        } catch (Exception e) {
          // TODO : 에러 처리 개선
          errCnt++;
          log.error("요소 수집 에러 {}", e.getMessage());
        }
      }

      final List<Company> saveCompanies =
          companyService.saveNewCompanies(companyMap.values().stream().toList());
      // DB에 존재하는 회사 목록
      final Map<String, Company> preloadCompanies =
          companyService
              .findAllByNameIn(
                  postCompanyMap.values().stream().toList().stream().map(Company::getName).toList())
              .stream()
              .collect(Collectors.toMap(Company::getName, Function.identity()));
      final List<JobPosting> savePostings =
          jobPostingService.saveNewJobPostings(jobPostings, postCompanyMap, preloadCompanies);

      log.info("크롤링 성공 횟수 : {}", elements.size() - errCnt);
      log.info("크롤링 실패 횟수 : {}", errCnt);
    } finally {
      driver.quit(); // WebDriver의 전체 인스턴스를 종료
      log.info("크롤링 완료");
    }
  }
}
