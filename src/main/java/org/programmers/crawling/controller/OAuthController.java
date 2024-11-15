package org.programmers.crawling.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/users/oauth2")
@Slf4j
public class OAuthController {

    @Value("${google.client.username}")
    private String googleClientUsername;

    @Value("${google.client.password}")
    private String googleClientPassword;

    @Value("${GOOGLE_CLIENT_REDIRECT_URL}")
    private String googleClientRedirectURL;

    /**
     * 로그인 페이지 호출
     */
    @GetMapping()
    public String login() {
        return "login";
    }

    /**
     * 구글 로그인 요청
     */
    @GetMapping("/google/login")
    public RedirectView loginGoogle() {
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientUsername
            + "&redirect_uri=" + googleClientRedirectURL
            + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return new RedirectView(googleLoginUrl);
    }

    /**
     * 구글 로그인 콜백 처리
     */
    @GetMapping("/google")
    public ResponseEntity<String> handleGoogleCallback(@RequestParam(name = "code") String authCode) {
        if (authCode != null) {
//            log.info("Google authCode: {}", authCode);
            // TODO: 인가 코드를 사용하여 액세스 토큰 요청
            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.badRequest().body("Failed to retrieve auth code.");
    }
}