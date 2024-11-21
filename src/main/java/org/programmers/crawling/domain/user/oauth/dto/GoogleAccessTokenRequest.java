package org.programmers.crawling.domain.user.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoogleAccessTokenRequest {

    final private String code; // 클라이언트로부터 전달받은 인가 코드

    @JsonProperty("client_id")
    final private String clientId; // 구글 개발자센터에서 발급 받은 Client ID

    @JsonProperty("client_secret")
    final private String clientSecret; // 구글 개발자센터에서 발급 받은 Client Secret

    @JsonProperty("redirect_uri")
    final private String redirectUri; // 구글 개발자센터에서 등록한 redirect_uri

    @JsonProperty("grant_type")
    final private String grantType; // 로그인 방식

    public GoogleAccessTokenRequest(String code, String clientId, String clientSecret,
        String redirectUri) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.grantType = "authorization_code";
    }
}
