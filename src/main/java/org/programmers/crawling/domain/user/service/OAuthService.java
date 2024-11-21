package org.programmers.crawling.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.crawling.domain.user.oauth.GoogleClient;
import org.programmers.crawling.domain.user.oauth.dto.GoogleResourceResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    final GoogleClient googleClient;

    public String getIdTokenFromAuthCode(String authCode) {
        log.info("call OAuthService.getIdTokenFromAuthCode[authCode: {}]", authCode);
        return googleClient.getIdTokenFromAuthCode(authCode);
    }

    public GoogleResourceResponse getProfileFromIdToken(String idToken) {
        log.info("call OAuthService.getProfileFromIdToken[idToken: {}]", idToken);
        return googleClient.getProfileFromIdToken(idToken);
    }
}
