package moviebuddy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.InputStream;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;

import moviebuddy.util.DBConnection;
import moviebuddy.db.MovieDB;
import moviebuddy.db.ScheduleDB;
import moviebuddy.db.TicketDB;
import moviebuddy.model.Movie;

public class MovieDAO {

    public List<Movie> listMovies() throws Exception {
        String QUERY_MOVIES = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s ORDER BY %s DESC;",
            MovieDB.MOVIE_ID, MovieDB.TITLE, MovieDB.RELEASE_DATE,
            MovieDB.DURATION, MovieDB.POSTER, MovieDB.TRAILER,
            MovieDB.DESCRIPTION, MovieDB.TABLE, MovieDB.RELEASE_DATE
        );

        Connection conn = null;
        PreparedStatement queryMovies = null;
        List<Movie> movies = new LinkedList<>();
        try {
            conn = DBConnection.connect();
            queryMovies = conn.prepareStatement(QUERY_MOVIES);
            ResultSet res = queryMovies.executeQuery();
            while (res.next()) {
                Movie movie = new Movie(res.getInt(MovieDB.MOVIE_ID));
                movie.setTitle(res.getString(MovieDB.TITLE));
                movie.setReleaseDate(LocalDate.parse(res.getString(MovieDB.RELEASE_DATE)));
                movie.setDuration(res.getInt(MovieDB.DURATION));
                movie.setPoster(res.getString(MovieDB.POSTER));
                movie.setTrailer(res.getString(MovieDB.TRAILER));
                movie.setDescription(res.getString(MovieDB.DESCRIPTION));
                movies.add(movie);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryMovies);
            DBConnection.close(conn);
        }
        return movies;
    }

    public Movie getMovieById(String movieId) throws Exception {
        String QUERY_MOVIE_BY_ID = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s=?;",
            MovieDB.MOVIE_ID, MovieDB.TITLE, MovieDB.RELEASE_DATE,
            MovieDB.DURATION, MovieDB.POSTER, MovieDB.TRAILER,
            MovieDB.DESCRIPTION, MovieDB.TABLE, MovieDB.MOVIE_ID
        );

        Movie movie = null;
        Connection conn = null;
        PreparedStatement queryMovieById = null;
        try {
            conn = DBConnection.connect();
            queryMovieById = conn.prepareStatement(QUERY_MOVIE_BY_ID);
            queryMovieById.setString(1, movieId);
            ResultSet res = queryMovieById.executeQuery();
            while (res.next()) {
                movie = new Movie(res.getInt(MovieDB.MOVIE_ID));
                movie.setTitle(res.getString(MovieDB.TITLE));
                movie.setReleaseDate(LocalDate.parse(res.getString(MovieDB.RELEASE_DATE)));
                movie.setDuration(res.getInt(MovieDB.DURATION));
                movie.setPoster(res.getString(MovieDB.POSTER));
                movie.setTrailer(res.getString(MovieDB.TRAILER));
                movie.setDescription(res.getString(MovieDB.DESCRIPTION));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryMovieById);
            DBConnection.close(conn);
        }
        return movie;
    }

    public String uploadMovie(String title, String releaseDate, String duration, String trailer, InputStream poster,
            long posterSize, String description) throws Exception {
        String INSERT_MOVIE = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?);",
            MovieDB.TABLE, MovieDB.TITLE, MovieDB.RELEASE_DATE,
            MovieDB.DURATION, MovieDB.TRAILER, MovieDB.DESCRIPTION
        );
        String QUERY_MOVIE_ID = String.format(
            "SELECT LAST_INSERT_ID() as %s;",
            MovieDB.MOVIE_ID
        );
        String UPDATE_MOVIE_POSTER = String.format(
            "UPDATE %s SET %s=? WHERE %s= LAST_INSERT_ID();",
            MovieDB.TABLE, MovieDB.POSTER, MovieDB.MOVIE_ID
        );

        Connection conn = null;
        PreparedStatement insertMovie = null;
        PreparedStatement getMovieId = null;
        PreparedStatement updatePoster = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            insertMovie = conn.prepareStatement(INSERT_MOVIE);
            insertMovie.setString(1, title);
            insertMovie.setString(2, releaseDate);
            insertMovie.setInt(3, Integer.parseInt(duration));
            insertMovie.setString(4, trailer);
            insertMovie.setString(5, description);
            insertMovie.executeUpdate();

            int posterId = -1;
            getMovieId = conn.prepareStatement(QUERY_MOVIE_ID);
            ResultSet res = getMovieId.executeQuery();
            while (res.next()) {
                posterId = res.getInt(MovieDB.MOVIE_ID);
            }
            if (posterId < 0) {
                return "Fail to get movie id";
            }
            String posterURL = BuddyBucket.uploadPoster(posterId, poster, posterSize);
            if (posterURL.isEmpty()) {
                conn.rollback();
                return "Fail to upload poster";
            }

            updatePoster = conn.prepareStatement(UPDATE_MOVIE_POSTER);
            updatePoster.setString(1, posterURL);
            updatePoster.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to upload movie information";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(insertMovie);
            DBConnection.close(getMovieId);
            DBConnection.close(updatePoster);
            DBConnection.close(conn);
        }
        return "";
    }

    public String updateMovie(String movieId, String title, String releaseDate, String duration, String trailer,
            InputStream poster, long posterSize, String description) throws Exception {
        String UPDATE_MOVIE = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s = ?;",
            MovieDB.TABLE, MovieDB.TITLE, MovieDB.RELEASE_DATE,
            MovieDB.DURATION, MovieDB.TRAILER, MovieDB.DESCRIPTION,
            MovieDB.MOVIE_ID
        );
        String UPDATE_POSTER = String.format(
            "UPDATE %s SET %s=? WHERE %s = ?;",
            MovieDB.TABLE, MovieDB.POSTER, MovieDB.MOVIE_ID
        );

        Connection conn = null;
        PreparedStatement updateMovie = null;
        PreparedStatement updatePoster = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            updateMovie = conn.prepareStatement(UPDATE_MOVIE);
            updateMovie.setString(1, title);
            updateMovie.setString(2, releaseDate);
            updateMovie.setInt(3, Integer.parseInt(duration));
            updateMovie.setString(4, trailer);
            updateMovie.setString(5, description);
            updateMovie.setString(6, movieId);
            updateMovie.executeUpdate();

            if (posterSize > 0) {
                String posterURL = BuddyBucket.uploadPoster(Integer.parseInt(movieId), poster, posterSize);
                updatePoster = conn.prepareStatement(UPDATE_POSTER);
                updatePoster.setString(1, posterURL);
                updatePoster.setString(2, movieId);
                updatePoster.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to update movie information";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(updateMovie);
            DBConnection.close(updatePoster);
            DBConnection.close(conn);
        }
        return "";
    }

    public String deleteMovie(String movieId) throws Exception {
        String DELETE_TICKET = String.format(
            "DELETE FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s=?);",
            TicketDB.TABLE, TicketDB.SCHEDULE_ID, ScheduleDB.SCHEDULE_ID,
            ScheduleDB.TABLE, ScheduleDB.MOVIE_ID
        );
        String DELETE_SCHEDULE = String.format(
            "DELETE FROM %s WHERE %s=?;",
            ScheduleDB.TABLE, ScheduleDB.MOVIE_ID
        );
        String DELETE_MOVIE = String.format(
            "DELETE FROM %s WHERE %s=?;",
            MovieDB.TABLE, MovieDB.MOVIE_ID
        );

        Connection conn = null;
        PreparedStatement deleteTicket = null;
        PreparedStatement deleteSchedule = null;
        PreparedStatement deleteMovie = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);
            deleteTicket = conn.prepareStatement(DELETE_TICKET);
            deleteTicket.setString(1, movieId);
            deleteTicket.executeUpdate();

            deleteSchedule = conn.prepareStatement(DELETE_SCHEDULE);
            deleteSchedule.setString(1, movieId);
            deleteSchedule.executeUpdate();

            deleteMovie = conn.prepareStatement(DELETE_MOVIE);
            deleteMovie.setString(1, movieId);
            deleteMovie.executeUpdate();

            BuddyBucket.deletePoster(Integer.parseInt(movieId));

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to delete movie information";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(deleteTicket);
            DBConnection.close(deleteSchedule);
            DBConnection.close(deleteMovie);
            DBConnection.close(conn);
        }
        return "";
    }
}