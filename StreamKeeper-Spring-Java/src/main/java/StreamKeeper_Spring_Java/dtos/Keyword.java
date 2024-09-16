package StreamKeeper_Spring_Java.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Keyword object returned by the TMDB API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Keyword {

    private int id;
    private String name;

    // Getters and Setters

    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }
}
