package StreamKeeper_Spring_Java.services;

import StreamKeeper_Spring_Java.dtos.*;
import StreamKeeper_Spring_Java.exceptions.CustomException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Service class to interact with the TMDB API.
 */
@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Tests the connection to the TMDB API by validating the API key.
     * @return true if the API key is valid, false otherwise.
     */
    public boolean testConnection() {
        String url = apiUrl + "/authentication/token/new?api_key=" + apiKey;
        try {
            restTemplate.getForObject(url, String.class);
            // If no exception is thrown, the API key is valid.
            return true;
        } catch (HttpClientErrorException e) {
            // Handle 4xx errors
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // Invalid API key
                return false;
            }
            throw e;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Performs a multi-search across movies, TV shows, and people.
     * @param query the search query.
     * @param page the page number for pagination.
     * @return the search results.
     */
    public MultiSearchResult multiSearch(String query, int page) {
        String url = String.format("%s/search/multi?api_key=%s&query=%s&page=%d",
                apiUrl, apiKey, query, page);
        try {
            return restTemplate.getForObject(url, MultiSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error performing multi-search", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the details of a specific movie by ID.
     * @param movieId the ID of the movie.
     * @return the movie details.
     */
    @Cacheable("movieDetails")
    public Movie getMovieDetails(int movieId) {
        String url = String.format("%s/movie/%d?api_key=%s", apiUrl, movieId, apiKey);
        try {
            return restTemplate.getForObject(url, Movie.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching movie details", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the details of a specific TV show by ID.
     * @param tvId the ID of the TV show.
     * @return the TV show details.
     */
    @Cacheable("tvDetails")
    public TvShow getTvShowDetails(int tvId) {
        String url = String.format("%s/tv/%d?api_key=%s", apiUrl, tvId, apiKey);
        try {
            return restTemplate.getForObject(url, TvShow.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching TV show details", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the details of a specific person by ID.
     * @param personId the ID of the person.
     * @return the person details.
     */
    @Cacheable("personDetails")
    public Person getPersonDetails(int personId) {
        String url = String.format("%s/person/%d?api_key=%s", apiUrl, personId, apiKey);
        try {
            return restTemplate.getForObject(url, Person.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching person details", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the watch providers for a specific movie by ID.
     * @param movieId the ID of the movie.
     * @return the watch providers.
     */
    public WatchProviders getMovieWatchProviders(int movieId) {
        String url = String.format("%s/movie/%d/watch/providers?api_key=%s", apiUrl, movieId, apiKey);
        try {
            return restTemplate.getForObject(url, WatchProviders.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching watch providers for movie", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the watch providers for a specific TV show by ID.
     * @param tvId the ID of the TV show.
     * @return the watch providers.
     */
    public WatchProviders getTvShowWatchProviders(int tvId) {
        String url = String.format("%s/tv/%d/watch/providers?api_key=%s", apiUrl, tvId, apiKey);
        try {
            return restTemplate.getForObject(url, WatchProviders.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching watch providers for TV show", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets movie recommendations based on a movie ID.
     * @param movieId the ID of the movie.
     * @param page the page number for pagination.
     * @return the recommended movies.
     */
    public MovieSearchResult getMovieRecommendations(int movieId, int page) {
        String url = String.format("%s/movie/%d/recommendations?api_key=%s&page=%d",
                apiUrl, movieId, apiKey, page);
        try {
            return restTemplate.getForObject(url, MovieSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching movie recommendations", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets TV show recommendations based on a TV show ID.
     * @param tvId the ID of the TV show.
     * @param page the page number for pagination.
     * @return the recommended TV shows.
     */
    public TvShowSearchResult getTvShowRecommendations(int tvId, int page) {
        String url = String.format("%s/tv/%d/recommendations?api_key=%s&page=%d",
                apiUrl, tvId, apiKey, page);
        try {
            return restTemplate.getForObject(url, TvShowSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching TV show recommendations", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Searches for keywords based on a query string.
     * @param query the search query.
     * @return the keyword search results.
     */
    public KeywordSearchResult searchKeywords(String query) {
        String url = String.format("%s/search/keyword?api_key=%s&query=%s",
                apiUrl, apiKey, query);
        try {
            return restTemplate.getForObject(url, KeywordSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching keyword search results", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets popular movies.
     * @param page the page number for pagination.
     * @return the popular movies.
     */
    public MovieSearchResult getPopularMovies(int page) {
        String url = String.format("%s/movie/popular?api_key=%s&page=%d",
                apiUrl, apiKey, page);
        try {
            return restTemplate.getForObject(url, MovieSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching popular movies", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets top-rated movies.
     * @param page the page number for pagination.
     * @return the top-rated movies.
     */
    public MovieSearchResult getTopRatedMovies(int page) {
        String url = String.format("%s/movie/top_rated?api_key=%s&page=%d",
                apiUrl, apiKey, page);
        try {
            return restTemplate.getForObject(url, MovieSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching top-rated movies", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets upcoming movies.
     * @param page the page number for pagination.
     * @return the upcoming movies.
     */
    public MovieSearchResult getUpcomingMovies(int page) {
        String url = String.format("%s/movie/upcoming?api_key=%s&page=%d",
                apiUrl, apiKey, page);
        try {
            return restTemplate.getForObject(url, MovieSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching upcoming movies", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets movies now playing in theaters.
     * @param page the page number for pagination.
     * @return the movies now playing.
     */
    public MovieSearchResult getNowPlayingMovies(int page) {
        String url = String.format("%s/movie/now_playing?api_key=%s&page=%d",
                apiUrl, apiKey, page);
        try {
            return restTemplate.getForObject(url, MovieSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching now playing movies", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets popular TV shows.
     * @param page the page number for pagination.
     * @return the popular TV shows.
     */
    public TvShowSearchResult getPopularTvShows(int page) {
        String url = String.format("%s/tv/popular?api_key=%s&page=%d",
                apiUrl, apiKey, page);
        try {
            return restTemplate.getForObject(url, TvShowSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching popular TV shows", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets top-rated TV shows.
     * @param page the page number for pagination.
     * @return the top-rated TV shows.
     */
    public TvShowSearchResult getTopRatedTvShows(int page) {
        String url = String.format("%s/tv/top_rated?api_key=%s&page=%d",
                apiUrl, apiKey, page);
        try {
            return restTemplate.getForObject(url, TvShowSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching top-rated TV shows", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets TV shows airing today.
     * @param page the page number for pagination.
     * @return the TV shows airing today.
     */
    public TvShowSearchResult getAiringTodayTvShows(int page) {
        String url = String.format("%s/tv/airing_today?api_key=%s&page=%d",
                apiUrl, apiKey, page);
        try {
            return restTemplate.getForObject(url, TvShowSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching TV shows airing today", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets TV shows currently on the air.
     * @param page the page number for pagination.
     * @return the TV shows on the air.
     */
    public TvShowSearchResult getOnTheAirTvShows(int page) {
        String url = String.format("%s/tv/on_the_air?api_key=%s&page=%d",
                apiUrl, apiKey, page);
        try {
            return restTemplate.getForObject(url, TvShowSearchResult.class);
        } catch (Exception e) {
            throw new CustomException("Error fetching TV shows on the air", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Additional methods can be added as needed.
}
