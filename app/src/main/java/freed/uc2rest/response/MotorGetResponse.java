package freed.uc2rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import freed.uc2rest.response.items.MotorGetItem;

public class MotorGetResponse {

    @JsonProperty("steppers")
    public MotorGetItem motorGetItem[];
}
