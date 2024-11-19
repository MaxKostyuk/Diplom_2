package com.kotan4ik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetOrdersResponse {
    private boolean success;
    private List<OrderFull> orders;
    private int total;
    private int totalToday;
}
