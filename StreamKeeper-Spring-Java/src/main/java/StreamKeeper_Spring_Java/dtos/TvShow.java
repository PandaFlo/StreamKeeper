package StreamKeeper_Spring_Java.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a TV Show object returned by the TMDB API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvShow extends Media {

    private int id;

    @JsonProperty("name")
    private String title;

    @JsonProperty("first_air_date")
    private String firstAirDate;

    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    // Additional fields can be added as needed

    // Getters and Setters

    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty("name")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    @JsonProperty("first_air_date")
    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getOverview() {
        return overview;
    }

    @JsonProperty("overview")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @JsonProperty("poster_path")
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
