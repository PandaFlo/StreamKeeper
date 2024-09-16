package StreamKeeper_Spring_Java.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Represents the watch providers data returned by the TMDB API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchProviders {

    private Map<String, CountryProvider> results;

    // Getters and Setters

    public Map<String, CountryProvider> getResults() {
        return results;
    }

    public void setResults(Map<String, CountryProvider> results) {
        this.results = results;
    }

    /**
     * Inner class to represent country-specific providers.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CountryProvider {

        private String link;

        @JsonProperty("flatrate")
        private Provider[] flatrate;

        // Additional provider types (e.g., buy, rent) can be added as needed

        // Getters and Setters

        public String getLink() {
            return link;
        }

        @JsonProperty("link")
        public void setLink(String link) {
            this.link = link;
        }

        public Provider[] getFlatrate() {
            return flatrate;
        }

        public void setFlatrate(Provider[] flatrate) {
            this.flatrate = flatrate;
        }
    }

    /**
     * Inner class to represent individual provider.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Provider {

        @JsonProperty("provider_id")
        private int providerId;

        @JsonProperty("provider_name")
        private String providerName;

        @JsonProperty("logo_path")
        private String logoPath;

        // Getters and Setters

        public int getProviderId() {
            return providerId;
        }

        public void setProviderId(int providerId) {
            this.providerId = providerId;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

        public String getLogoPath() {
            return logoPath;
        }

        public void setLogoPath(String logoPath) {
            this.logoPath = logoPath;
        }
    }
}
