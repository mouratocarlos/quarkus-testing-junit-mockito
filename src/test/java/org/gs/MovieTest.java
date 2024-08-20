package org.gs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovieTest {

    @Test
    public void testGetSetTitle() {
        Movie movie = new Movie();
        String title = "Inception";
        movie.setTitle(title);
        assertEquals(title, movie.getTitle());
    }

    @Test
    public void testGetSetDescription() {
        Movie movie = new Movie();
        String description = "A mind-bending thriller";
        movie.setDescription(description);
        assertEquals(description, movie.getDescription());
    }

    @Test
    public void testGetSetDirector() {
        Movie movie = new Movie();
        String director = "Christopher Nolan";
        movie.setDirector(director);
        assertEquals(director, movie.getDirector());
    }

    @Test
    public void testGetSetCountry() {
        Movie movie = new Movie();
        String country = "USA";
        movie.setCountry(country);
        assertEquals(country, movie.getCountry());
    }

    @Test
    public void testGetSetId() {
        Movie movie = new Movie();
        Long id = 1L;
        movie.setId(id);
        assertEquals(id, movie.getId());
    }
}
