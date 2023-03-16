package uc2.uc2rest.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import uc2.uc2rest.response.items.LedColorItem;

@JsonTypeName("led")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class LedArrRequest
{
    public int LEDArrMode =0;
    public LedColorItem led_array[];
}
