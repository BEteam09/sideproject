package org.programmers.crawling.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.crawling.domain.user.oauth.GoogleClient;
import org.programmers.crawling.domain.user.oauth.dto.GoogleResourceResponse;
import org.programmers.crawling.domain.user.service.OAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/users/oauth2")
@Slf4j
@RequiredArgsConstructor
public class OAuthController {

    private final GoogleClient googleClient;
    private final OAuthService oAuthService;

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
        String googleLoginUrl = googleClient.getGoogleLoginUrl();
        return new RedirectView(googleLoginUrl);
    }

    /**
     * 구글 로그인 콜백 처리
     */
    @GetMapping("/google")
    public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String authCode) {
        if (authCode != null) {
            String idToken = oAuthService.getIdTokenFromAuthCode(authCode);
            GoogleResourceResponse profile = oAuthService.getProfileFromIdToken(idToken);

            // TODO: ADD System Login Logic

            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.badRequest().body("Failed to retrieve auth code.");
    }
}