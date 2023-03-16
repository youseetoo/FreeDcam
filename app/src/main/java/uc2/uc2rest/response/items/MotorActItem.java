package uc2.uc2rest.response.items;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MotorActItem
{
    public int stepperid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long speed = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long maxspeed;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long acceleration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long position;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int isforever;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int isaccel;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int isabs;
}
