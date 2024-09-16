package StreamKeeper_Spring_Java.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Person object returned by the TMDB API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person extends Media {

    private int id;
    private String name;

    @JsonProperty("profile_path")
    private String profilePath;

    // Additional fields like known_for can be added if needed

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

    public String getProfilePath() {
        return profilePath;
    }

    @JsonProperty("profile_path")
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
