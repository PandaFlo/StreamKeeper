package StreamKeeper_Spring_Java.dtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base class for media types returned by the TMDB API.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "media_type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Movie.class, name = "movie"),
    @JsonSubTypes.Type(value = TvShow.class, name = "tv"),
    @JsonSubTypes.Type(value = Person.class, name = "person")
})
public abstract class Media {

    private String mediaType;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
