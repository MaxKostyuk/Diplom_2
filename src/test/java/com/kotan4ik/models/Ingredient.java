package com.kotan4ik.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Ingredient {
    @JsonProperty("_id")
    private String id;
    private String name;
    private String type;
    private int proteins;
    private int fat;
    private int carbohydrates;
    private int calories;
    private int price;
    private String image;
    @JsonProperty("image_mobile")
    private String imageMobile;
    @JsonProperty("image_large")
    private String imageLarge;
    @JsonProperty("__v")
    private int version;
}
