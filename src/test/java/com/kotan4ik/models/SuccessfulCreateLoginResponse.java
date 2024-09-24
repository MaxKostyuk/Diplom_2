package com.kotan4ik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SuccessfulCreateLoginResponse {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
}
