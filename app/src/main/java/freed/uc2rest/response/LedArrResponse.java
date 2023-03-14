package freed.uc2rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import freed.uc2rest.enums.LedModes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LedArrResponse
{
    @JsonProperty("ledArrNum")
    public int ledArrNum;

    @JsonProperty("led_ison")
    public boolean is_on;

    @JsonProperty("ledArrPin")
    public int pin;

    @JsonProperty("LEDArrMode")
    public LedModes[] ledModes;
}
