package org.gs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class MovieResourceTest {

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
    void getByIdOK() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.of(movie));

        Response response = movieResource.getById(1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Movie result = (Movie) response.getEntity();
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void getByIdKO() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Response response = movieResource.getById(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void getByTitleOK() {
        when(movieRepository.find("title", "Inception")).thenReturn(mock(PanacheQuery.class));
        when(movieRepository.find("title", "Inception").singleResultOptional()).thenReturn(Optional.of(movie));

        Response response = movieResource.getByTitle("Inception");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Movie result = (Movie) response.getEntity();
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void getByTitleKO() {
        when(movieRepository.find("title", "Inception")).thenReturn(mock(PanacheQuery.class));
        when(movieRepository.find("title", "Inception").singleResultOptional()).thenReturn(Optional.empty());

        Response response = movieResource.getByTitle("Inception");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
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
    void createOK() {
        when(movieRepository.isPersistent(movie)).thenReturn(true);

        Response response = movieResource.create(movie);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(URI.create("/movies/1"), response.getLocation());
    }

    @Test
    void createKO() {
        when(movieRepository.isPersistent(movie)).thenReturn(false);

        Response response = movieResource.create(movie);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void updateByIdOK() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.of(movie));

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("The Dark Knight");

        Response response = movieResource.updateById(1L, updatedMovie);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Movie result = (Movie) response.getEntity();
        assertEquals("The Dark Knight", result.getTitle());
    }

    @Test
    void updateByIdKO() {
        when(movieRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("The Dark Knight");

        Response response = movieResource.updateById(1L, updatedMovie);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void deleteByIdOK() {
        when(movieRepository.deleteById(1L)).thenReturn(true);

        Response response = movieResource.deleteById(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    void deleteByIdKO() {
        when(movieRepository.deleteById(1L)).thenReturn(false);

        Response response = movieResource.deleteById(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
