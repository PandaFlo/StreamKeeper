package StreamKeeper_Spring_Java.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a Person object returned by the TMDB API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person extends Media {

    private String name;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("known_for")
    private List<Media> knownFor;

    // Getters and Setters

    @Override
    public String getTitle() {
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

    public List<Media> getKnownFor() {
        return knownFor;
    }

    public void setKnownFor(List<Media> knownFor) {
        this.knownFor = knownFor;
    }
}
