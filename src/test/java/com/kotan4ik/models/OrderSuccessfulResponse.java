package com.kotan4ik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderSuccessfulResponse {
    private String name;
    private Order order;
    private boolean success;
}
