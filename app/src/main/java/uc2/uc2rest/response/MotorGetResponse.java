package uc2.uc2rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import uc2.uc2rest.response.items.MotorGetItem;

public class MotorGetResponse {

    @JsonProperty("steppers")
    public MotorGetItem motorGetItem[];
}
