package com.jjangiji.hankkimoa.auth.service.oauth;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjangiji.hankkimoa.auth.service.dto.request.OauthLoginRequest;
import com.jjangiji.hankkimoa.auth.service.dto.response.OauthInfoApiResponse;
import com.jjangiji.hankkimoa.auth.service.dto.response.OauthTokenApiResponse;
import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.common.exception.OauthException;
import com.jjangiji.hankkimoa.common.exception.response.OauthExceptionResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import java.io.IOException;

@Component
public class OauthClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final OauthRequestProperties oauthRequestProperties;

    public OauthClient(
            RestClient restClient,
            ObjectMapper objectMapper,
            OauthRequestProperties oauthRequestProperties) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.oauthRequestProperties = oauthRequestProperties;
    }

    public OauthInfoApiResponse requestOauthInfo(OauthLoginRequest request) {
        OauthTokenApiResponse oauthTokenApiResponse = requestToken(request);
        String userInfoRequestUri = oauthRequestProperties.getUserInfoRequestUri();

        String headerName = "Authorization";
        String headerValue = "Bearer " + oauthTokenApiResponse.access_token();

        return restClient.get()
                .uri(userInfoRequestUri)
                .header(headerName, headerValue)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> { //todo 테스트코드 추가
                    throw new OauthException(getExceptionResponse(res));
                })
                .body(OauthInfoApiResponse.class);
    }

    private OauthTokenApiResponse requestToken(OauthLoginRequest request) {
        String tokenRequestUri = oauthRequestProperties.getTokenRequestUri();
        MultiValueMap<String, String> tokenRequestBody = oauthRequestProperties.createTokenRequestBody(request);

        return restClient.post()
                .uri(tokenRequestUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(tokenRequestBody)
                .retrieve()
                .body(OauthTokenApiResponse.class);
    }

    private OauthExceptionResponse getExceptionResponse(ClientHttpResponse response) {
        try {
            return objectMapper
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(response.getBody(), OauthExceptionResponse.class);
        } catch (IOException exception) {
            throw new HankkiMoaException(ExceptionCode.OAUTH_TOKEN_INTERNAL_EXCEPTION);
        }
    }
}
