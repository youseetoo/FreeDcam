package uc2.uc2rest.response.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BtScanItem {
    @JsonProperty("name")
    public String name;
    @JsonProperty("mac")
    public String mac;
}
