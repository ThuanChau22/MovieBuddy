package moviebuddy.model;

import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;

public class Movie {
    private int id;
    private String title;
    private int duration;
    private LocalDate releaseDate;
    private String trailer;
    private String poster;
    private String description;
    private List<Schedule> schedules;

    public Movie(int id) {
        this.id = id;
        schedules = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getPoster() {
        return poster;
    }

    public String getDescription() {
        return description;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addSchedule(Schedule s){
        schedules.add(s);
    }
}
