package org.gs;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class MovieRepositoryTest {

    @Inject
    MovieRepository movieRepository;

    @Test
    @TestTransaction
    public void testFindByCountry() {
        Movie movie1 = new Movie();
        movie1.setTitle("Inception");
        movie1.setDescription("A mind-bending thriller");
        movie1.setDirector("Christopher Nolan");
        movie1.setCountry("USA");

        Movie movie2 = new Movie();
        movie2.setTitle("Parasite");
        movie2.setDescription("A dark comedy thriller");
        movie2.setDirector("Bong Joon-ho");
        movie2.setCountry("South Korea");

        Movie movie3 = new Movie();
        movie3.setTitle("The Dark Knight");
        movie3.setDescription("A superhero film");
        movie3.setDirector("Christopher Nolan");
        movie3.setCountry("USA");

        movieRepository.persist(movie1);
        movieRepository.persist(movie2);
        movieRepository.persist(movie3);

        List<Movie> moviesInUSA = movieRepository.findByCountry("USA");
        assertEquals(2, moviesInUSA.size());
        assertEquals("The Dark Knight", moviesInUSA.get(0).getTitle());
        assertEquals("Inception", moviesInUSA.get(1).getTitle());

        List<Movie> moviesInSouthKorea = movieRepository.findByCountry("South Korea");
        assertEquals(1, moviesInSouthKorea.size());
        assertEquals("Parasite", moviesInSouthKorea.get(0).getTitle());

        List<Movie> moviesInFrance = movieRepository.findByCountry("France");
        assertTrue(moviesInFrance.isEmpty());
    }
}
