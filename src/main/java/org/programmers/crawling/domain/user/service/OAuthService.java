package org.programmers.crawling.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.programmers.crawling.domain.user.oauth.GoogleClient;
import org.programmers.crawling.domain.user.oauth.dto.GoogleResourceResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    final GoogleClient googleClient;

    public String getGoogleAccessTokenFromCode(String authCode) {
        return googleClient.getGoogleAccessTokenFromCode(authCode);
    }

    public GoogleResourceResponse getProfileFromIdToken(String idToken) {
        return googleClient.getProfileFromIdToken(idToken);
    }
}
