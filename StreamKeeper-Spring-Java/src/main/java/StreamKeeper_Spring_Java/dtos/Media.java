package StreamKeeper_Spring_Java.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Base class for media types.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Media {

    protected int id;
    private String mediaType;

    // Abstract method to get the title/name
    public abstract String getTitle();

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    } 

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
