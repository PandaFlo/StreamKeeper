package StreamKeeper_Spring_Java.controllers;

import StreamKeeper_Spring_Java.dtos.*;
import StreamKeeper_Spring_Java.services.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to expose TMDB API endpoints.
 */
@RestController
@RequestMapping("/tmdb")
public class TmdbController {

    @Autowired
    private TmdbService tmdbService;

    /**
     * Endpoint to test the TMDB API connection.
     * @return a message indicating whether the connection is successful.
     */
    @GetMapping("/test-connection")
    public String testConnection() {
        boolean isConnected = tmdbService.testConnection();
        if (isConnected) {
            return "TMDB API connection successful!";
        } else {
            return "Failed to connect to TMDB API.";
        }
    }

    /**
     * Endpoint to perform a multi-search.
     * @param query the search query.
     * @param page the page number for pagination.
     * @return multi-search results.
     */
    @GetMapping("/search/multi")
    public MultiSearchResult multiSearch(@RequestParam String query,
                                         @RequestParam(defaultValue = "1") int page) {
        return tmdbService.multiSearch(query, page);
    }

    /**
     * Endpoint to get media details based on media type and ID.
     * @param mediaType the type of media (movie, tv, person).
     * @param id the ID of the media.
     * @return media details.
     */
    @GetMapping("/details/{mediaType}/{id}")
    public Media getMediaDetails(@PathVariable String mediaType,
                                 @PathVariable int id) {
        switch (mediaType.toLowerCase()) {
            case "movie":
                return tmdbService.getMovieDetails(id);
            case "tv":
                return tmdbService.getTvShowDetails(id);
            case "person":
                return tmdbService.getPersonDetails(id);
            default:
                throw new IllegalArgumentException("Invalid media type");
        }
    }

    /**
     * Endpoint to get watch providers for a media type and ID.
     * @param mediaType the type of media (movie, tv).
     * @param id the ID of the media.
     * @return watch providers.
     */
    @GetMapping("/{mediaType}/{id}/watch/providers")
    public WatchProviders getWatchProviders(@PathVariable String mediaType,
                                            @PathVariable int id) {
        switch (mediaType.toLowerCase()) {
            case "movie":
                return tmdbService.getMovieWatchProviders(id);
            case "tv":
                return tmdbService.getTvShowWatchProviders(id);
            default:
                throw new IllegalArgumentException("Invalid media type for watch providers");
        }
    }

    /**
     * Endpoint to get recommendations based on media type and ID.
     * @param mediaType the type of media (movie, tv).
     * @param id the ID of the media.
     * @param page the page number for pagination.
     * @return recommendations.
     */
    @GetMapping("/{mediaType}/{id}/recommendations")
    public Object getRecommendations(@PathVariable String mediaType,
                                     @PathVariable int id,
                                     @RequestParam(defaultValue = "1") int page) {
        switch (mediaType.toLowerCase()) {
            case "movie":
                return tmdbService.getMovieRecommendations(id, page);
            case "tv":
                return tmdbService.getTvShowRecommendations(id, page);
            default:
                throw new IllegalArgumentException("Invalid media type for recommendations");
        }
    }

    /**
     * Endpoint to search for keywords.
     * @param query the search query.
     * @return keyword search results.
     */
    @GetMapping("/search/keyword")
    public KeywordSearchResult searchKeywords(@RequestParam String query) {
        return tmdbService.searchKeywords(query);
    }

    /**
     * Endpoint to get popular movies.
     * @param page the page number for pagination.
     * @return popular movies.
     */
    @GetMapping("/movies/popular")
    public MovieSearchResult getPopularMovies(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.getPopularMovies(page);
    }

    /**
     * Endpoint to get top-rated movies.
     * @param page the page number for pagination.
     * @return top-rated movies.
     */
    @GetMapping("/movies/top-rated")
    public MovieSearchResult getTopRatedMovies(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.getTopRatedMovies(page);
    }

    /**
     * Endpoint to get upcoming movies.
     * @param page the page number for pagination.
     * @return upcoming movies.
     */
    @GetMapping("/movies/upcoming")
    public MovieSearchResult getUpcomingMovies(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.getUpcomingMovies(page);
    }

    /**
     * Endpoint to get now-playing movies.
     * @param page the page number for pagination.
     * @return now-playing movies.
     */
    @GetMapping("/movies/now-playing")
    public MovieSearchResult getNowPlayingMovies(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.getNowPlayingMovies(page);
    }

    /**
     * Endpoint to get popular TV shows.
     * @param page the page number for pagination.
     * @return popular TV shows.
     */
    @GetMapping("/tv/popular")
    public TvShowSearchResult getPopularTvShows(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.getPopularTvShows(page);
    }

    /**
     * Endpoint to get top-rated TV shows.
     * @param page the page number for pagination.
     * @return top-rated TV shows.
     */
    @GetMapping("/tv/top-rated")
    public TvShowSearchResult getTopRatedTvShows(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.getTopRatedTvShows(page);
    }

    /**
     * Endpoint to get TV shows airing today.
     * @param page the page number for pagination.
     * @return TV shows airing today.
     */
    @GetMapping("/tv/airing-today")
    public TvShowSearchResult getAiringTodayTvShows(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.getAiringTodayTvShows(page);
    }

    /**
     * Endpoint to get TV shows currently on the air.
     * @param page the page number for pagination.
     * @return TV shows on the air.
     */
    @GetMapping("/tv/on-the-air")
    public TvShowSearchResult getOnTheAirTvShows(@RequestParam(defaultValue = "1") int page) {
        return tmdbService.getOnTheAirTvShows(page);
    }

    // Additional endpoints can be added as needed.
}
