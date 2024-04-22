package in.reqres.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ColorModel {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("year")
    private String year;

    @JsonProperty("color")
    private String color;

    @JsonProperty("pantone_value")
    private String pantoneValue;
}
