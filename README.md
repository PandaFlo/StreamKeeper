# StreamKeeper

StreamKeeper is a web application that allows users to search for movies, TV shows, and people using the TMDB (The Movie Database) API. It provides detailed information about media content, including watch providers, recommendations, and more.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
  - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [License](#license)
- [Acknowledgements](#acknowledgements)

## Features

- **Search Functionality**: Perform searches for movies, TV shows, and people.
- **Multi-Search**: Search across movies, TV shows, and people in a single request.
- **Media Details**: View detailed information about movies, TV shows, and people.
- **Watch Providers**: Find out where to watch a particular movie or TV show.
- **Recommendations**: Get media recommendations based on a selected movie or TV show.
- **Popular Content**: Browse popular and top-rated movies and TV shows.
- **Responsive UI**: User-friendly interface built with Angular for seamless user experience.

## Technologies Used

### Backend

- **Java 11**
- **Spring Boot**
- **Spring Web**
- **Spring Cache**
- **RestTemplate** (for RESTful API calls)
- **Maven** (for project management)

### Frontend

- **Angular 12**
- **TypeScript**
- **Angular Material** (for UI components)
- **HTTPClient** (for API communication)

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 11 or higher**
- **Node.js and npm**
- **Angular CLI** (for frontend development)
- **Maven** (for backend build management)
- **TMDB API Key**: You need to register for an API key from [TMDB](https://www.themoviedb.org/documentation/api).

### Backend Setup

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/streamkeeper.git
   cd streamkeeper/backend
   ```

2. **Configure API Key**

   - Open `src/main/resources/application.properties`.
   - Replace `YOUR_TMDB_API_KEY` with your actual TMDB API key:

     ```properties
     tmdb.api.key=YOUR_TMDB_API_KEY
     tmdb.api.url=https://api.themoviedb.org/3
     ```

3. **Build the Project**

   ```bash
   mvn clean install
   ```

4. **Run the Backend**

   ```bash
   mvn spring-boot:run
   ```

   The backend server will start on `http://localhost:8080`.

### Frontend Setup

1. **Navigate to Frontend Directory**

   ```bash
   cd ../frontend
   ```

2. **Install Dependencies**

   ```bash
   npm install
   ```

3. **Configure API Endpoint**

   - Open `src/environments/environment.ts`.
   - Ensure the `apiUrl` is set to the backend server URL:

     ```typescript
     export const environment = {
       production: false,
       apiUrl: 'http://localhost:8080'
     };
     ```

4. **Run the Frontend**

   ```bash
   ng serve
   ```

   The frontend application will run on `http://localhost:4200`.

### Running the Application

- **Access the Application**: Open your web browser and navigate to `http://localhost:4200`.

## API Documentation

The backend API provides several endpoints to interact with the TMDB API.

### Base URL

```
http://localhost:8080/tmdb
```

### Endpoints

- **Test Connection**

  - `GET /tmdb/test-connection`
  - Tests the connection to the TMDB API.

- **Multi-Search**

  - `GET /tmdb/search/multi?query={query}&page={page}`
  - Searches across movies, TV shows, and people.

- **Get Media Details**

  - `GET /tmdb/details/{mediaType}/{id}`
  - Retrieves details for a specific media type (`movie`, `tv`, or `person`) and ID.

- **Get Watch Providers**

  - `GET /tmdb/{mediaType}/{id}/watch/providers`
  - Gets watch provider information for a movie or TV show.

- **Get Recommendations**

  - `GET /tmdb/{mediaType}/{id}/recommendations?page={page}`
  - Fetches recommendations based on a movie or TV show ID.

- **Get Popular Movies**

  - `GET /tmdb/movies/popular?page={page}`
  - Retrieves a list of popular movies.

- **Get Top-Rated TV Shows**

  - `GET /tmdb/tv/top-rated?page={page}`
  - Retrieves a list of top-rated TV shows.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgements

- **TMDB API**: This product uses the TMDB API but is not endorsed or certified by TMDB.
- **Spring Boot**: Simplifying backend development.
- **Angular**: For a robust frontend framework.

---

**Disclaimer**: This project is for educational purposes. Always adhere to TMDB's terms of service when using their API.
