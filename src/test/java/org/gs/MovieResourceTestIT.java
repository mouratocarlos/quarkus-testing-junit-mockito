package org.gs;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieResourceTestIT {

    @InjectMock
    MovieRepository movieRepository;

    @Inject
    MovieResource movieResource;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setDescription("A mind-bending thriller");
        movie.setDirector("Christopher Nolan");
        movie.setCountry("USA");
    }

    @Test
    @Order(1)
    void getAll() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        when(movieRepository.listAll()).thenReturn(movies);

        Response response = movieResource.getAll();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<Movie> result = (List<Movie>) response.getEntity();
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    @Order(2)
    void getById() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.of(movie));

        Response response = movieResource.getById(1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Movie result = (Movie) response.getEntity();
        assertEquals("Inception", result.getTitle());
    }

    @Test
    @Order(3)
    void getByIdKO() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Response response = movieResource.getById(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(4)
    void getByTitle() {
        when(movieRepository.find("title", "Inception")).thenReturn(mock(PanacheQuery.class));
        when(movieRepository.find("title", "Inception").singleResultOptional()).thenReturn(Optional.of(movie));

        Response response = movieResource.getByTitle("Inception");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Movie result = (Movie) response.getEntity();
        assertEquals("Inception", result.getTitle());
    }

    @Test
    @Order(5)
    void getByTitleKO() {
        when(movieRepository.find("title", "Inception")).thenReturn(mock(PanacheQuery.class));
        when(movieRepository.find("title", "Inception").singleResultOptional()).thenReturn(Optional.empty());

        Response response = movieResource.getByTitle("Inception");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(6)
    void getByCountry() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        when(movieRepository.findByCountry("USA")).thenReturn(movies);

        Response response = movieResource.getByCountry("USA");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<Movie> result = (List<Movie>) response.getEntity();
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    @Order(7)
    void getByCountryKO() {
        when(movieRepository.findByCountry("France")).thenReturn(new ArrayList<>());

        Response response = movieResource.getByCountry("France");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<Movie> result = (List<Movie>) response.getEntity();
        assertTrue(result.isEmpty());
    }

    @Test
    @Order(8)
    void create() {
        when(movieRepository.isPersistent(movie)).thenReturn(true);

        Response response = movieResource.create(movie);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("/movies/1", response.getLocation().toString());
    }

    @Test
    @Order(9)
    void updateById() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.of(movie));

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("The Dark Knight");

        Response response = movieResource.updateById(1L, updatedMovie);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Movie result = (Movie) response.getEntity();
        assertEquals("The Dark Knight", result.getTitle());
    }

    @Test
    @Order(10)
    void updateByIdKO() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("The Dark Knight");

        Response response = movieResource.updateById(1L, updatedMovie);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(11)
    void deleteById() {
        when(movieRepository.deleteById(1L)).thenReturn(true);

        Response response = movieResource.deleteById(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(12)
    void deleteByIdKO() {
        when(movieRepository.deleteById(1L)).thenReturn(false);

        Response response = movieResource.deleteById(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
