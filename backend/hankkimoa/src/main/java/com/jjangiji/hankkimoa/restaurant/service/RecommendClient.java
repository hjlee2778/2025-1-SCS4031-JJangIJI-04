package com.jjangiji.hankkimoa.restaurant.service;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.RecommendServerException;
import com.jjangiji.hankkimoa.restaurant.service.dto.reqeust.RecommendServerRestaurantsRequest;
import com.jjangiji.hankkimoa.restaurant.service.dto.response.RecommendServerRestaurantsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class RecommendClient {

    private final RestClient restClient;

    @Value("${recommend-server.restaurants-post-uri}")
    private String recommendRestaurantsRequestUri;

    public RecommendServerRestaurantsResponse requestRecommendRestaurants(RecommendServerRestaurantsRequest request) {
        return restClient.post()
                .uri(recommendRestaurantsRequestUri)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new RecommendServerException(getExceptionResponse(res));
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new RecommendServerException(getExceptionResponse(res));
                })
                .body(RecommendServerRestaurantsResponse.class);
    }

    private String getExceptionResponse(ClientHttpResponse response) {
        try {
            return new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RecommendServerException(ExceptionCode.RECOMMEND_SERVER_INTERNAL_EXCEPTION.getMessage());
        }
    }
}
