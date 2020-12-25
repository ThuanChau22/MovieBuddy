package moviebuddy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.time.LocalTime;

import moviebuddy.util.DBConnection;
import moviebuddy.db.RoomDB;
import moviebuddy.db.ScheduleDB;
import moviebuddy.db.TicketDB;
import moviebuddy.model.Schedule;
import moviebuddy.model.TicketPrice;
import moviebuddy.model.ScheduledDate;
import moviebuddy.model.Ticket;

public class ScheduleDAO {

    public List<Schedule> listScheduleByMovieId(String theatreId, String movieId) throws Exception {
        String QUERY_SCHEDULES = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s=? AND %s=? ORDER BY %s, %s;",
            ScheduleDB.SCHEDULE_ID, ScheduleDB.THEATRE_ID, ScheduleDB.ROOM_NUMBER,
            ScheduleDB.MOVIE_ID, ScheduleDB.SHOW_DATE, ScheduleDB.START_TIME,
            ScheduleDB.END_TIME, ScheduleDB.TABLE, ScheduleDB.THEATRE_ID,
            ScheduleDB.MOVIE_ID, ScheduleDB.SHOW_DATE, ScheduleDB.START_TIME
        );

        List<Schedule> schedules = new LinkedList<>();
        Connection conn = null;
        PreparedStatement querySchedules = null;
        try {
            conn = DBConnection.connect();
            querySchedules = conn.prepareStatement(QUERY_SCHEDULES);
            querySchedules.setString(1, theatreId);
            querySchedules.setString(2, movieId);
            ResultSet res = querySchedules.executeQuery();
            while (res.next()) {
                Schedule schedule = new Schedule(res.getInt(ScheduleDB.SCHEDULE_ID));
                schedule.setTheatreId(res.getInt(ScheduleDB.THEATRE_ID));
                schedule.setRoomNumber(res.getInt(ScheduleDB.ROOM_NUMBER));
                schedule.setMovieId(res.getInt(ScheduleDB.MOVIE_ID));
                schedule.setShowDate(LocalDate.parse(res.getString(ScheduleDB.SHOW_DATE)));
                schedule.setStartTime(LocalTime.parse(res.getString(ScheduleDB.START_TIME)));
                schedule.setEndTime(LocalTime.parse(res.getString(ScheduleDB.END_TIME)));
                schedules.add(schedule);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(querySchedules);
            DBConnection.close(conn);
        }
        return schedules;
    }

    public List<ScheduledDate> listScheduledDates() throws Exception{
        return null;
    }

    public Schedule getScheduleConflict(String theatreId, String showDate, String movieId,String roomNumber, String startTime, String endTime) throws Exception {
        String QUERY_CONFLICT_SCHEUDLES = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s=? AND (%s=? OR %s=?) AND ((? < ? AND ((%s=? AND ((%s < %s AND %s >= ? AND %s <= ?) OR (%s > %s AND %s <= ?))) OR (%s=DATE_ADD(?, INTERVAL -1 DAY) AND %s > %s AND %s >= ?))) OR (? > ? AND ((%s=? AND ((%s < %s AND %s >= ?) OR (%s > %s))) OR (%s=DATE_ADD(?, INTERVAL 1 DAY) AND %s <= ?)))) LIMIT 1;",
            ScheduleDB.SCHEDULE_ID, ScheduleDB.THEATRE_ID, ScheduleDB.MOVIE_ID,
            ScheduleDB.ROOM_NUMBER, ScheduleDB.SHOW_DATE, ScheduleDB.START_TIME,
            ScheduleDB.END_TIME, ScheduleDB.TABLE, ScheduleDB.THEATRE_ID,
            ScheduleDB.MOVIE_ID, ScheduleDB.ROOM_NUMBER, ScheduleDB.SHOW_DATE,
            ScheduleDB.START_TIME, ScheduleDB.END_TIME, ScheduleDB.END_TIME,
            ScheduleDB.START_TIME, ScheduleDB.START_TIME, ScheduleDB.END_TIME,
            ScheduleDB.START_TIME, ScheduleDB.SHOW_DATE, ScheduleDB.START_TIME,
            ScheduleDB.END_TIME, ScheduleDB.END_TIME, ScheduleDB.SHOW_DATE,
            ScheduleDB.START_TIME, ScheduleDB.END_TIME, ScheduleDB.END_TIME,
            ScheduleDB.START_TIME, ScheduleDB.END_TIME, ScheduleDB.SHOW_DATE,
            ScheduleDB.START_TIME
        );

        Schedule schedule = null;
        Connection conn = null;
        PreparedStatement queryConflictSchedule = null;
        try {
            conn = DBConnection.connect();
            queryConflictSchedule = conn.prepareStatement(QUERY_CONFLICT_SCHEUDLES);
            queryConflictSchedule.setString(1, theatreId);
            queryConflictSchedule.setString(2, movieId);
            queryConflictSchedule.setString(3, roomNumber);
            queryConflictSchedule.setString(4, startTime);
            queryConflictSchedule.setString(5, endTime);
            queryConflictSchedule.setString(6, showDate);
            queryConflictSchedule.setString(7, startTime);
            queryConflictSchedule.setString(8, endTime);
            queryConflictSchedule.setString(9, endTime);
            queryConflictSchedule.setString(10, showDate);
            queryConflictSchedule.setString(11, startTime);
            queryConflictSchedule.setString(12, startTime);
            queryConflictSchedule.setString(13, endTime);
            queryConflictSchedule.setString(14, showDate);
            queryConflictSchedule.setString(15, startTime);
            queryConflictSchedule.setString(16, showDate);
            queryConflictSchedule.setString(17, endTime);
            ResultSet res = queryConflictSchedule.executeQuery();
            while (res.next()) {
                schedule = new Schedule(res.getInt(ScheduleDB.SCHEDULE_ID));
                schedule.setTheatreId(res.getInt(ScheduleDB.THEATRE_ID));
                schedule.setMovieId(res.getInt(ScheduleDB.MOVIE_ID));
                schedule.setRoomNumber(res.getInt(ScheduleDB.ROOM_NUMBER));
                schedule.setShowDate(LocalDate.parse(res.getString(ScheduleDB.SHOW_DATE)));
                schedule.setStartTime(LocalTime.parse(res.getString(ScheduleDB.START_TIME)));
                schedule.setEndTime(LocalTime.parse(res.getString(ScheduleDB.END_TIME)));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryConflictSchedule);
            DBConnection.close(conn);
        }
        return schedule;
    }

    public Schedule getScheduleById(String scheduleId) throws Exception {
        String QUERY_SCHEDULE = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s=?;",
            ScheduleDB.SCHEDULE_ID, ScheduleDB.THEATRE_ID, ScheduleDB.MOVIE_ID,
            ScheduleDB.ROOM_NUMBER, ScheduleDB.SHOW_DATE, ScheduleDB.START_TIME,
            ScheduleDB.END_TIME, ScheduleDB.TABLE, ScheduleDB.SCHEDULE_ID
        );

        Schedule schedule = null;
        Connection conn = null;
        PreparedStatement querySchedule = null;
        try {
            conn = DBConnection.connect();
            querySchedule = conn.prepareStatement(QUERY_SCHEDULE);
            querySchedule.setString(1, scheduleId);
            ResultSet res = querySchedule.executeQuery();
            while (res.next()) {
                schedule = new Schedule(res.getInt(ScheduleDB.SCHEDULE_ID));
                schedule.setTheatreId(res.getInt(ScheduleDB.THEATRE_ID));
                schedule.setMovieId(res.getInt(ScheduleDB.MOVIE_ID));
                schedule.setRoomNumber(res.getInt(ScheduleDB.ROOM_NUMBER));
                schedule.setShowDate(LocalDate.parse(res.getString(ScheduleDB.SHOW_DATE)));
                schedule.setStartTime(LocalTime.parse(res.getString(ScheduleDB.START_TIME)));
                schedule.setEndTime(LocalTime.parse(res.getString(ScheduleDB.END_TIME)));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(querySchedule);
            DBConnection.close(conn);
        }
        return schedule;
    }

    public String addSchedule(String theatreId, String roomNumber, String movieId, String showDate, String startTime, String endTime)
            throws Exception {
        String INSERT_SCHEDULE = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?);",
            ScheduleDB.TABLE, ScheduleDB.THEATRE_ID, ScheduleDB.ROOM_NUMBER,
            ScheduleDB.MOVIE_ID, ScheduleDB.SHOW_DATE, ScheduleDB.START_TIME,
            ScheduleDB.END_TIME
        );
        String QUERY_SCHEDULE_ID = String.format(
            "SELECT LAST_INSERT_ID() as %s;",
            ScheduleDB.SCHEDULE_ID
        );
        String QUERY_ROOM_CAPACITY = String.format(
            "SELECT %s, %s FROM %s WHERE %s=? AND %s=?;",
            RoomDB.SECTIONS, RoomDB.SEATS, RoomDB.TABLE,
            RoomDB.THEATRE_ID, RoomDB.ROOM_NUMBER
        );
        String INSERT_TICKET = String.format(
            "INSERT INTO %s (%s, %s) VALUES ",
            TicketDB.TABLE, TicketDB.SCHEDULE_ID, TicketDB.SEAT_NUMBER
        );

        Connection conn = null;
        PreparedStatement insertSchedule = null;
        PreparedStatement queryScheduleId = null;
        PreparedStatement queryRoomCapacity = null;
        PreparedStatement insertTicket = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            insertSchedule = conn.prepareStatement(INSERT_SCHEDULE);
            insertSchedule.setString(1, theatreId);
            insertSchedule.setString(2, roomNumber);
            insertSchedule.setString(3, movieId);
            insertSchedule.setString(4, showDate);
            insertSchedule.setString(5, startTime);
            insertSchedule.setString(6, endTime);
            insertSchedule.executeUpdate();

            String scheduleId = "";
            queryScheduleId = conn.prepareStatement(QUERY_SCHEDULE_ID);
            ResultSet resScheduleId = queryScheduleId.executeQuery();
            while (resScheduleId.next()) {
                scheduleId = resScheduleId.getString(ScheduleDB.SCHEDULE_ID);
            }

            int sections = 0, seats = 0;
            queryRoomCapacity = conn.prepareStatement(QUERY_ROOM_CAPACITY);
            queryRoomCapacity.setString(1, theatreId);
            queryRoomCapacity.setString(2, roomNumber);
            ResultSet resRoomCap = queryRoomCapacity.executeQuery();
            while (resRoomCap.next()) {
                sections = resRoomCap.getInt(RoomDB.SECTIONS);
                seats = resRoomCap.getInt(RoomDB.SEATS);
            }

            for (int i = 0; i < sections; i++) {
                for (int j = 0; j < seats; j++) {
                    char row = (char) ('A' + i);
                    int col = j + 1;
                    String seatNumber = row + ((col < 10) ? "0" : "") + col;
                    INSERT_TICKET += String.format("(%s,'%s')%s",
                    scheduleId, seatNumber, j < (seats - 1) ? "," : "");
                }
                INSERT_TICKET += i < (sections - 1) ? "," : ";";
            }
            insertTicket = conn.prepareStatement(INSERT_TICKET);
            insertTicket.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to add schedule";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(insertSchedule);
            DBConnection.close(queryScheduleId);
            DBConnection.close(queryRoomCapacity);
            DBConnection.close(insertTicket);
            DBConnection.close(conn);
        }
        return "";
    }

    public String deleteSchedule(String scheduleId) throws Exception {
        String DELETE_TICKET = String.format(
            "DELETE FROM %s WHERE %s=?;",
            TicketDB.TABLE, TicketDB.SCHEDULE_ID
        );
        String DELETE_SCHEDULE = String.format(
            "DELETE FROM %s WHERE %s=?;",
            ScheduleDB.TABLE, ScheduleDB.SCHEDULE_ID
        );
        Connection conn = null;
        PreparedStatement deleteTicket = null;
        PreparedStatement deleteSchedule = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            deleteTicket = conn.prepareStatement(DELETE_TICKET);
            deleteTicket.setString(1, scheduleId);
            deleteTicket.executeUpdate();

            deleteSchedule = conn.prepareStatement(DELETE_SCHEDULE);
            deleteSchedule.setString(1, scheduleId);
            deleteSchedule.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to delete schedule";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(deleteTicket);
            DBConnection.close(deleteSchedule);
            DBConnection.close(conn);
        }
        return "";
    }

    public Ticket getTicketInfo(int scheduleID, String seatNumber, boolean occupid) throws Exception {
        String QUERY_TICKET = "";
        Connection conn = DBConnection.connect();
        PreparedStatement getTicket = conn.prepareStatement(QUERY_TICKET);
        ResultSet res = getTicket.executeQuery();
        Ticket ticket = null;
        while (res.next()) {
            ticket = new Ticket();
            ticket.setScheduleId(res.getInt("schedule_id"));
            ticket.setSeatNumber(res.getString("seat_number"));
            ticket.setOccupied(res.getBoolean("occupid"));
        }
        getTicket.close();
        conn.close();
        return ticket;
    }

    public double getTicketPrice(String showTime, String ScheduleID) throws Exception{
        String QUERY_TICKET_PRICE = "SELECT DISTINCT tp.start_time, tp.price" +
        "FROM ticket_price tp" +  
        "JOIN schedule ms ON tp.theatre_id=ms.theatre_id" +
        "JOIN ticket t ON ms.schedule_id=t.schedule_id" + 
        "WHERE t.schedule_id=?;";
        Connection conn = DBConnection.connect();
        PreparedStatement getTicketPrice = conn.prepareStatement(QUERY_TICKET_PRICE);
        getTicketPrice.setString(1, ScheduleID);
        ResultSet res = getTicketPrice.executeQuery();
        List<TicketPrice> ticketPrices = new LinkedList<>();
        while (res.next()) {
            TicketPrice ticketPrice = new TicketPrice(res.getInt("theatre_id"),LocalTime.parse(res.getString("start_time")));
            ticketPrice.setPrice(res.getDouble("price"));
            ticketPrices.add(ticketPrice);
        }
        getTicketPrice.close();
        conn.close();
        for(int i =0;i<ticketPrices.size();i++){
            if(LocalTime.parse(showTime).isAfter(ticketPrices.get(i).getStartTime()) || i==ticketPrices.size()-1)
            return ticketPrices.get(i).getPrice();
        }
        return 0;
    }
}