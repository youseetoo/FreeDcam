package freed.uc2rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import freed.uc2rest.response.items.MotorActItem;

@JsonTypeName("motor")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class MotorActRequest
{
    @JsonProperty("steppers")
    public MotorActItem motorActItem[];
}
