package com.example.automatic_bitcoinsales.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@ResponseBody
@Controller
public class test {
    final String publicKey = "BkdAaly79AiaMz5HlugjqEkRUANrCMdnwh5FLQ9v";
    final String secretKey ="BkdAaly79AiaMz5HlugjqEkRUANrCMdnwh5FLQ9v";

    @GetMapping("/getPrice/{name}")
    public String getPrice(@PathVariable("name") String name) throws IOException, NoSuchAlgorithmException, InterruptedException {
        int cycle = 30;

        String to = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));


        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String jwtToken = JWT.create()
                .withClaim("access_key", publicKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.upbit.com/v1/candles/minutes/1?" +
                        "market=KRW-BTC" +
                        "&to="+to +
                        "&count=1"))
                .headers("Accept", "application/json", "Authorization", authenticationToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        return response.body();
    }
}
