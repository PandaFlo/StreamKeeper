package StreamKeeper_Spring_Java.services;

import StreamKeeper_Spring_Java.dtos.*;
import StreamKeeper_Spring_Java.exceptions.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Tests the connection to the TMDB API by making a sample request.
     *
     * @return True if the connection is successful; otherwise, false.
     */
    public boolean testConnection() {
        String url = String.format("%s/movie/550?api_key=%s", apiUrl, apiKey);
        try {
            restTemplate.getForObject(url, Map.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Performs a multi-search on TMDB and returns a MultiSearchResult.
     *
     * @param query The search query string.
     * @param page  The page number for pagination.
     * @return A MultiSearchResult containing a list of media items matching the query.
     */
    public MultiSearchResult multiSearch(String query, int page) {
        String url = String.format("%s/search/multi?api_key=%s&query=%s&page=%d",
                apiUrl, apiKey, URLEncoder.encode(query, StandardCharsets.UTF_8), page);
        try {
            // Make a GET request to the TMDB API and parse the response into a Map
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // Extract the list of raw results from the response
            List<Map<String, Object>> rawResults = (List<Map<String, Object>>) response.get("results");
            List<Media> processedResults = new ArrayList<>();

            // Process each raw result item and convert it into a Media object
            if (rawResults != null) {
                for (Map<String, Object> item : rawResults) {
                    Media mediaItem = processMediaItem(item);
                    if (mediaItem != null) {
                        processedResults.add(mediaItem);
                    }
                }
            }

            // Build the MultiSearchResult object with the processed results
            MultiSearchResult multiSearchResult = new MultiSearchResult();
            multiSearchResult.setPage(getIntValue(response.get("page")));
            multiSearchResult.setResults(processedResults);
            multiSearchResult.setTotalResults(getIntValue(response.get("total_results")));
            multiSearchResult.setTotalPages(getIntValue(response.get("total_pages")));

            return multiSearchResult;

        } catch (Exception e) {
            throw new CustomException("Error performing multi-search: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Processes a single media item from the search results by determining its type and converting it accordingly.
     *
     * @param item The raw media item as a Map.
     * @return A Media object representing the media item, or null if the type is unknown.
     */
    private Media processMediaItem(Map<String, Object> item) {
        try {
            // Determine the media type from the 'media_type' field, or infer it based on the presence of certain keys
            String mediaType = (String) item.get("media_type");
            if (mediaType == null) {
                // Infer media type based on the presence of specific keys
                if (item.containsKey("title")) {
                    mediaType = "movie";
                } else if (item.containsKey("name")) {
                    mediaType = "tv";
                } else {
                    mediaType = "unknown";
                }
            }

            switch (mediaType) {
                case "movie":
                    // Convert the item to a Movie object
                    Movie movie = objectMapper.convertValue(item, Movie.class);
                    movie.setMediaType("movie");
                    return movie;
                case "tv":
                    // Convert the item to a TvShow object
                    TvShow tvShow = objectMapper.convertValue(item, TvShow.class);
                    tvShow.setMediaType("tv");
                    return tvShow;
                case "person":
                    // Convert the item to a Person object
                    Person person = objectMapper.convertValue(item, Person.class);
                    person.setMediaType("person");
                    // Process the 'known_for' field recursively
                    List<Map<String, Object>> knownForRaw = (List<Map<String, Object>>) item.get("known_for");
                    List<Media> knownForProcessed = new ArrayList<>();
                    if (knownForRaw != null) {
                        for (Map<String, Object> knownForItem : knownForRaw) {
                            Media knownForMedia = processMediaItem(knownForItem);
                            if (knownForMedia != null) {
                                knownForProcessed.add(knownForMedia);
                            }
                        }
                    }
                    person.setKnownFor(knownForProcessed);
                    return person;
                default:
                    // Unknown media type
                    return null;
            }
        } catch (Exception e) {
            // Handle or log the error
            return null;
        }
    }

    /**
     * Safely converts an Object to an int.
     *
     * @param value The object to convert.
     * @return The integer value, or 0 if conversion fails.
     */
    private int getIntValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else {
            return 0;
        }
    }

    /**
     * Retrieves movie details by ID from the TMDB API.
     *
     * @param movieId The ID of the movie.
     * @return A Movie object containing detailed information about the movie.
     */
    @Cacheable("movieDetails")
    public Movie getMovieDetails(int movieId) {
        String url = String.format("%s/movie/%d?api_key=%s", apiUrl, movieId, apiKey);
        try {
            Movie movie = restTemplate.getForObject(url, Movie.class);
            if (movie != null) {
                movie.setMediaType("movie");
            }
            return movie;
        } catch (Exception e) {
            throw new CustomException("Error fetching movie details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves TV show details by ID from the TMDB API.
     *
     * @param tvShowId The ID of the TV show.
     * @return A TvShow object containing detailed information about the TV show.
     */
    @Cacheable("tvShowDetails")
    public TvShow getTvShowDetails(int tvShowId) {
        String url = String.format("%s/tv/%d?api_key=%s", apiUrl, tvShowId, apiKey);
        try {
            TvShow tvShow = restTemplate.getForObject(url, TvShow.class);
            if (tvShow != null) {
                tvShow.setMediaType("tv");
            }
            return tvShow;
        } catch (Exception e) {
            throw new CustomException("Error fetching TV show details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves person details by ID from the TMDB API.
     *
     * @param personId The ID of the person.
     * @return A Person object containing detailed information about the person.
     */
    @Cacheable("personDetails")
    public Person getPersonDetails(int personId) {
        String url = String.format("%s/person/%d?api_key=%s", apiUrl, personId, apiKey);
        try {
            Person person = restTemplate.getForObject(url, Person.class);
            if (person != null) {
                person.setMediaType("person");
            }
            return person;
        } catch (Exception e) {
            throw new CustomException("Error fetching person details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves watch provider information for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return A WatchProviders object containing watch provider details.
     */
    public WatchProviders getMovieWatchProviders(int movieId) {
        return getWatchProviders("movie", movieId);
    }

    /**
     * Retrieves watch provider information for a specific TV show.
     *
     * @param tvShowId The ID of the TV show.
     * @return A WatchProviders object containing watch provider details.
     */
    public WatchProviders getTvShowWatchProviders(int tvShowId) {
        return getWatchProviders("tv", tvShowId);
    }

    /**
     * Helper method to retrieve watch provider information for a specific media type and ID.
     *
     * @param mediaType The type of media ("movie" or "tv").
     * @param id        The ID of the media.
     * @return A WatchProviders object containing watch provider details.
     */
    private WatchProviders getWatchProviders(String mediaType, int id) {
        String url = String.format("%s/%s/%d/watch/providers?api_key=%s", apiUrl, mediaType, id, apiKey);
        try {
            WatchProviders watchProviders = restTemplate.getForObject(url, WatchProviders.class);
            return watchProviders;
        } catch (Exception e) {
            throw new CustomException("Error fetching watch providers: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves movie recommendations based on a specific movie ID.
     *
     * @param movieId The ID of the movie.
     * @param page    The page number for pagination.
     * @return A MovieSearchResult containing recommended movies.
     */
    public MovieSearchResult getMovieRecommendations(int movieId, int page) {
        String url = String.format("%s/movie/%d/recommendations?api_key=%s&page=%d",
                apiUrl, movieId, apiKey, page);
        try {
            MovieSearchResult movieSearchResult = restTemplate.getForObject(url, MovieSearchResult.class);
            // Set the media type for each movie in the results
            if (movieSearchResult != null && movieSearchResult.getResults() != null) {
                for (Movie movie : movieSearchResult.getResults()) {
                    movie.setMediaType("movie");
                }
            }
            return movieSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching movie recommendations: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves TV show recommendations based on a specific TV show ID.
     *
     * @param tvShowId The ID of the TV show.
     * @param page     The page number for pagination.
     * @return A TvShowSearchResult containing recommended TV shows.
     */
    public TvShowSearchResult getTvShowRecommendations(int tvShowId, int page) {
        String url = String.format("%s/tv/%d/recommendations?api_key=%s&page=%d",
                apiUrl, tvShowId, apiKey, page);
        try {
            TvShowSearchResult tvShowSearchResult = restTemplate.getForObject(url, TvShowSearchResult.class);
            // Set the media type for each TV show in the results
            if (tvShowSearchResult != null && tvShowSearchResult.getResults() != null) {
                for (TvShow tvShow : tvShowSearchResult.getResults()) {
                    tvShow.setMediaType("tv");
                }
            }
            return tvShowSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching TV show recommendations: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Searches for keywords in the TMDB database.
     *
     * @param query The search query string.
     * @return A KeywordSearchResult containing the search results.
     */
    public KeywordSearchResult searchKeywords(String query) {
        String url = String.format("%s/search/keyword?api_key=%s&query=%s",
                apiUrl, apiKey, URLEncoder.encode(query, StandardCharsets.UTF_8));
        try {
            KeywordSearchResult keywordSearchResult = restTemplate.getForObject(url, KeywordSearchResult.class);
            return keywordSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error searching keywords: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of popular movies.
     *
     * @param page The page number for pagination.
     * @return A MovieSearchResult containing popular movies.
     */
    public MovieSearchResult getPopularMovies(int page) {
        String url = String.format("%s/movie/popular?api_key=%s&page=%d", apiUrl, apiKey, page);
        try {
            MovieSearchResult movieSearchResult = restTemplate.getForObject(url, MovieSearchResult.class);
            return movieSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching popular movies: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of top-rated movies.
     *
     * @param page The page number for pagination.
     * @return A MovieSearchResult containing top-rated movies.
     */
    public MovieSearchResult getTopRatedMovies(int page) {
        String url = String.format("%s/movie/top_rated?api_key=%s&page=%d", apiUrl, apiKey, page);
        try {
            MovieSearchResult movieSearchResult = restTemplate.getForObject(url, MovieSearchResult.class);
            return movieSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching top-rated movies: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of upcoming movies.
     *
     * @param page The page number for pagination.
     * @return A MovieSearchResult containing upcoming movies.
     */
    public MovieSearchResult getUpcomingMovies(int page) {
        String url = String.format("%s/movie/upcoming?api_key=%s&page=%d", apiUrl, apiKey, page);
        try {
            MovieSearchResult movieSearchResult = restTemplate.getForObject(url, MovieSearchResult.class);
            return movieSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching upcoming movies: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of movies that are currently playing in theaters.
     *
     * @param page The page number for pagination.
     * @return A MovieSearchResult containing now-playing movies.
     */
    public MovieSearchResult getNowPlayingMovies(int page) {
        String url = String.format("%s/movie/now_playing?api_key=%s&page=%d", apiUrl, apiKey, page);
        try {
            MovieSearchResult movieSearchResult = restTemplate.getForObject(url, MovieSearchResult.class);
            return movieSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching now-playing movies: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of popular TV shows.
     *
     * @param page The page number for pagination.
     * @return A TvShowSearchResult containing popular TV shows.
     */
    public TvShowSearchResult getPopularTvShows(int page) {
        String url = String.format("%s/tv/popular?api_key=%s&page=%d", apiUrl, apiKey, page);
        try {
            TvShowSearchResult tvShowSearchResult = restTemplate.getForObject(url, TvShowSearchResult.class);
            return tvShowSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching popular TV shows: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of top-rated TV shows.
     *
     * @param page The page number for pagination.
     * @return A TvShowSearchResult containing top-rated TV shows.
     */
    public TvShowSearchResult getTopRatedTvShows(int page) {
        String url = String.format("%s/tv/top_rated?api_key=%s&page=%d", apiUrl, apiKey, page);
        try {
            TvShowSearchResult tvShowSearchResult = restTemplate.getForObject(url, TvShowSearchResult.class);
            return tvShowSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching top-rated TV shows: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of TV shows that are airing today.
     *
     * @param page The page number for pagination.
     * @return A TvShowSearchResult containing TV shows airing today.
     */
    public TvShowSearchResult getAiringTodayTvShows(int page) {
        String url = String.format("%s/tv/airing_today?api_key=%s&page=%d", apiUrl, apiKey, page);
        try {
            TvShowSearchResult tvShowSearchResult = restTemplate.getForObject(url, TvShowSearchResult.class);
            return tvShowSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching TV shows airing today: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of TV shows that are currently on the air.
     *
     * @param page The page number for pagination.
     * @return A TvShowSearchResult containing TV shows currently on the air.
     */
    public TvShowSearchResult getOnTheAirTvShows(int page) {
        String url = String.format("%s/tv/on_the_air?api_key=%s&page=%d", apiUrl, apiKey, page);
        try {
            TvShowSearchResult tvShowSearchResult = restTemplate.getForObject(url, TvShowSearchResult.class);
            return tvShowSearchResult;
        } catch (Exception e) {
            throw new CustomException("Error fetching TV shows on the air: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
