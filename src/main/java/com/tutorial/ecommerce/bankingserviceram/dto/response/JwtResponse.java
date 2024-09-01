package com.tutorial.ecommerce.bankingserviceram.dto.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn;

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
        this.expiresIn = 3600;
    }
}
