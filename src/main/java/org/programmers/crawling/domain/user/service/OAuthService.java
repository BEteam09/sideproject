package org.programmers.crawling.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.programmers.crawling.domain.user.oauth.GoogleClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    final GoogleClient googleClient;

    public void getGoogleAccessTokenFromCode(String authCode) {
        System.out.println(googleClient.getGoogleAccessTokenFromCode(authCode));
    }
}
