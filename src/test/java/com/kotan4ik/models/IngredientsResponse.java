package com.kotan4ik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class IngredientsResponse {
    private boolean success;
    private List<Ingredient> data;
}
