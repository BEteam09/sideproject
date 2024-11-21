package org.programmers.crawling.domain.user.oauth;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.programmers.crawling.domain.user.oauth.dto.GoogleAccessTokenRequest;
import org.programmers.crawling.domain.user.oauth.dto.GoogleAccessTokenResponse;
import org.programmers.crawling.domain.user.oauth.dto.GoogleResourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleClient {

    @Value("${google.client.username}")
    private String googleClientUsername;

    @Value("${google.client.password}")
    private String googleClientPassword;

    @Value("${google.client.redirect-url}")
    private String googleClientRedirectURL;

    @Value("${google.oauth.api.url}")
    private String googleOauthApiUrl;

    @Value("${google.resource.api.url}")
    private String googleResourceApiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public GoogleClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getGoogleAccessTokenFromCode(String authCode) {
        String decodedAuthCode = decodeAuthCode(authCode);

        HttpEntity<GoogleAccessTokenRequest> httpEntity = createHttpEntityForRequestGoogleAccessToken(
            decodedAuthCode);

        GoogleAccessTokenResponse response = requestGoogleOauthAPIServer(httpEntity);

        System.out.println(response);

        return Optional.ofNullable(response)
            .orElseThrow(() -> new RuntimeException("Google OAuth 인가 코드가 올바르지 않음"))
            .getAccessToken();
    }

    private String decodeAuthCode(String authCode) {
        return URLDecoder.decode(authCode, StandardCharsets.UTF_8);
    }

    private HttpEntity<GoogleAccessTokenRequest> createHttpEntityForRequestGoogleAccessToken(
        String authCode) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return new HttpEntity<>(
            new GoogleAccessTokenRequest(authCode, googleClientUsername, googleClientPassword, googleClientRedirectURL),
            headers);
    }

    private GoogleAccessTokenResponse requestGoogleOauthAPIServer(
        HttpEntity<GoogleAccessTokenRequest> httpEntity) {

        return restTemplate.exchange(
            googleOauthApiUrl, HttpMethod.POST, httpEntity, GoogleAccessTokenResponse.class
        ).getBody();
    }

    public GoogleResourceResponse getProfileFromIdToken(String idToken) {
        HttpEntity<?> httpEntity = createHttpEntityForRequestGoogleResource(idToken);

        GoogleResourceResponse response = requestGoogleResourceServer(httpEntity);
        System.out.println(response);

        return Optional.ofNullable(response)
            .orElseThrow(() -> new RuntimeException("Google Resource 서버 통신 에러"));
    }

    private HttpEntity<?> createHttpEntityForRequestGoogleResource(String idToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + idToken);
        return new HttpEntity<>(headers);
    }

    private GoogleResourceResponse requestGoogleResourceServer(HttpEntity<?> httpEntity) {
        return restTemplate.exchange(
            googleResourceApiUrl, HttpMethod.GET, httpEntity, GoogleResourceResponse.class
        ).getBody();
    }
}