package moviebuddy.model;

import java.time.LocalDate;
import java.util.List;
import java.util.LinkedList;

public class ScheduledDate {
    private LocalDate showDate;
    private List<Movie> movies;

    public ScheduledDate(LocalDate showDate){
        this.showDate = showDate;
        movies = new LinkedList<>();
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie){
        movies.add(movie);
    }
}
