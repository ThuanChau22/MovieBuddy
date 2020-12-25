package moviebuddy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.LinkedList;
import java.time.LocalTime;

import moviebuddy.util.DBConnection;
import moviebuddy.db.TheatreDB;
import moviebuddy.db.TicketPriceDB;
import moviebuddy.db.EmployDB;
import moviebuddy.db.RoomDB;
import moviebuddy.db.ScheduleDB;
import moviebuddy.db.TicketDB;
import moviebuddy.model.Theatre;
import moviebuddy.model.Room;
import moviebuddy.model.TicketPrice;

public class TheatreDAO {

    public List<Theatre> listTheatres() throws Exception {
        String QUERY_THEATRES = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s ORDER BY %s;",
            TheatreDB.THEATRE_ID, TheatreDB.THEATRE_NAME, TheatreDB.ADDRESS,
            TheatreDB.CITY, TheatreDB.STATE, TheatreDB.COUNTRY,
            TheatreDB.ZIP_CODE, TheatreDB.TABLE, TheatreDB.THEATRE_NAME
        );

        List<Theatre> theatres = new LinkedList<>();
        Connection conn = null;
        PreparedStatement queryTheatres = null;
        try {
            conn = DBConnection.connect();
            queryTheatres = conn.prepareStatement(QUERY_THEATRES);
            ResultSet res = queryTheatres.executeQuery();
            while (res.next()) {
                Theatre theatre = new Theatre(res.getInt(TheatreDB.THEATRE_ID));
                theatre.setTheatreName(res.getString(TheatreDB.THEATRE_NAME));
                theatre.setAddress(res.getString(TheatreDB.ADDRESS));
                theatre.setCity(res.getString(TheatreDB.CITY));
                theatre.setState(res.getString(TheatreDB.STATE));
                theatre.setCountry(res.getString(TheatreDB.COUNTRY));
                theatre.setZip(res.getString(TheatreDB.ZIP_CODE));
                theatres.add(theatre);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryTheatres);
            DBConnection.close(conn);
        }
        return theatres;
    }

    public Theatre getTheatreById(String theatreId) throws Exception {
        String QUERY_THEATRE_BY_ID = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s=?;",
            TheatreDB.THEATRE_ID, TheatreDB.THEATRE_NAME, TheatreDB.ADDRESS,
            TheatreDB.CITY, TheatreDB.STATE, TheatreDB.COUNTRY,
            TheatreDB.ZIP_CODE, TheatreDB.TABLE, TheatreDB.THEATRE_ID
        );

        Theatre theatre = null;
        Connection conn = null;
        PreparedStatement queryTheatreById = null;
        try {
            conn = DBConnection.connect();
            queryTheatreById = conn.prepareStatement(QUERY_THEATRE_BY_ID);
            queryTheatreById.setString(1, theatreId);
            ResultSet res = queryTheatreById.executeQuery();
            while (res.next()) {
                theatre = new Theatre(res.getInt(TheatreDB.THEATRE_ID));
                theatre.setTheatreName(res.getString(TheatreDB.THEATRE_NAME));
                theatre.setAddress(res.getString(TheatreDB.ADDRESS));
                theatre.setCity(res.getString(TheatreDB.CITY));
                theatre.setState(res.getString(TheatreDB.STATE));
                theatre.setCountry(res.getString(TheatreDB.COUNTRY));
                theatre.setZip(res.getString(TheatreDB.ZIP_CODE));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryTheatreById);
            DBConnection.close(conn);
        }
        return theatre;
    }

    public Theatre getTheatreByName(String theatreName) throws Exception {
        String QUERY_THEATRE_BY_NAME = String.format(
            "SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s=?;",
            TheatreDB.THEATRE_ID, TheatreDB.THEATRE_NAME, TheatreDB.ADDRESS,
            TheatreDB.CITY, TheatreDB.STATE, TheatreDB.COUNTRY,
            TheatreDB.ZIP_CODE, TheatreDB.TABLE, TheatreDB.THEATRE_NAME
        );

        Theatre theatre = null;
        Connection conn = null;
        PreparedStatement queryTheatreByName = null;
        try {
            conn = DBConnection.connect();
            queryTheatreByName = conn.prepareStatement(QUERY_THEATRE_BY_NAME);
            queryTheatreByName.setString(1, theatreName);
            ResultSet res = queryTheatreByName.executeQuery();
            while (res.next()) {
                theatre = new Theatre(res.getInt(TheatreDB.THEATRE_ID));
                theatre.setTheatreName(res.getString(TheatreDB.THEATRE_NAME));
                theatre.setAddress(res.getString(TheatreDB.ADDRESS));
                theatre.setCity(res.getString(TheatreDB.CITY));
                theatre.setState(res.getString(TheatreDB.STATE));
                theatre.setCountry(res.getString(TheatreDB.COUNTRY));
                theatre.setZip(res.getString(TheatreDB.ZIP_CODE));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryTheatreByName);
            DBConnection.close(conn);
        }
        return theatre;
    }

    public String createTheatre(String theatreName, String address, String city, String state, String country,
            String zip) throws Exception {
        String INSERT_THEATRE = String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES(?,?,?,?,?,?);",
            TheatreDB.TABLE, TheatreDB.THEATRE_NAME, TheatreDB.ADDRESS,
            TheatreDB.CITY, TheatreDB.STATE, TheatreDB.COUNTRY,
            TheatreDB.ZIP_CODE
        );

        Connection conn = null;
        PreparedStatement insertTheatre = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            insertTheatre = conn.prepareStatement(INSERT_THEATRE);
            insertTheatre.setString(1, theatreName);
            insertTheatre.setString(2, address);
            insertTheatre.setString(3, city);
            insertTheatre.setString(4, state);
            insertTheatre.setString(5, country);
            insertTheatre.setString(6, zip);
            insertTheatre.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to create theatre";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(insertTheatre);
            DBConnection.close(conn);
        }
        return "";
    }

    public String updateTheatre(String theatreId, String theatreName, String address, String city, String state,
            String country, String zip) throws Exception {
        String UPDATE_THEATRE = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?;",
            TheatreDB.TABLE, TheatreDB.THEATRE_NAME, TheatreDB.ADDRESS,
            TheatreDB.CITY, TheatreDB.STATE, TheatreDB.COUNTRY,
            TheatreDB.ZIP_CODE, TheatreDB.THEATRE_ID
        );

        Connection conn = null;
        PreparedStatement updateTheatre = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            updateTheatre = conn.prepareStatement(UPDATE_THEATRE);
            updateTheatre.setString(1, theatreName);
            updateTheatre.setString(2, address);
            updateTheatre.setString(3, city);
            updateTheatre.setString(4, state);
            updateTheatre.setString(5, country);
            updateTheatre.setString(6, zip);
            updateTheatre.setString(7, theatreId);
            updateTheatre.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to update theatre";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(updateTheatre);
            DBConnection.close(conn);
        }
        return "";
    }

    public String deleteTheatre(String theatreId) throws Exception {
        String DELETE_TICKET = String.format(
            "DELETE FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s=?)",
            TicketDB.TABLE, TicketDB.SCHEDULE_ID, ScheduleDB.SCHEDULE_ID,
            ScheduleDB.TABLE, ScheduleDB.THEATRE_ID
        );
        String DELETE_SCHEDULE = String.format(
            "DELETE FROM %s WHERE %s=?;",
            ScheduleDB.TABLE, ScheduleDB.THEATRE_ID
        );
        String DELETE_ROOM = String.format(
            "DELETE FROM %s WHERE %s=?;",
            RoomDB.TABLE, RoomDB.THEATRE_ID
        );
        String DELETE_EMPLOY = String.format(
            "DELETE FROM %s WHERE %s=?;",
            EmployDB.TABLE, EmployDB.THEATRE_ID
        );
        String DELETE_TICKET_PRICE = String.format(
            "DELETE FROM %s WHERE %s=?;",
            TicketPriceDB.TABLE, TicketPriceDB.THEATRE_ID
        );
        String DELETE_THEATRE = String.format(
            "DELETE FROM %s WHERE %s=?;",
            TheatreDB.TABLE, TheatreDB.THEATRE_ID
        );

        Connection conn = null;
        PreparedStatement deleteTicket = null;
        PreparedStatement deleteSchedule = null;
        PreparedStatement deleteRoom = null;
        PreparedStatement deleteEmploy = null;
        PreparedStatement deleteTicketPrice = null;
        PreparedStatement deleteTheatre = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            deleteTicket = conn.prepareStatement(DELETE_TICKET);
            deleteTicket.setString(1, theatreId);
            deleteTicket.executeUpdate();

            deleteSchedule = conn.prepareStatement(DELETE_SCHEDULE);
            deleteSchedule.setString(1, theatreId);
            deleteSchedule.executeUpdate();

            deleteRoom = conn.prepareStatement(DELETE_ROOM);
            deleteRoom.setString(1, theatreId);
            deleteRoom.executeUpdate();

            deleteEmploy = conn.prepareStatement(DELETE_EMPLOY);
            deleteEmploy.setString(1, theatreId);
            deleteEmploy.executeUpdate();

            deleteTicketPrice = conn.prepareStatement(DELETE_TICKET_PRICE);
            deleteTicketPrice.setString(1, theatreId);
            deleteTicketPrice.executeUpdate();

            deleteTheatre = conn.prepareStatement(DELETE_THEATRE);
            deleteTheatre.setString(1, theatreId);
            deleteTheatre.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to delete theatre";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(deleteTicket);
            DBConnection.close(deleteSchedule);
            DBConnection.close(deleteRoom);
            DBConnection.close(deleteEmploy);
            DBConnection.close(deleteTicketPrice);
            DBConnection.close(deleteTheatre);
            DBConnection.close(conn);
        }
        return "";
    }

    public List<Room> listRooms(String theatreId) throws Exception {
        String QUERY_ROOMS = String.format(
            "SELECT %s, %s, %s, %s FROM %s WHERE %s=? ORDER BY %s;",
            RoomDB.THEATRE_ID, RoomDB.ROOM_NUMBER, RoomDB.SECTIONS,
            RoomDB.SEATS, RoomDB.TABLE, RoomDB.THEATRE_ID,
            RoomDB.ROOM_NUMBER
        );

        List<Room> rooms = new LinkedList<>();
        Connection conn = null;
        PreparedStatement queryRooms = null;
        try {
            conn = DBConnection.connect();
            queryRooms = conn.prepareStatement(QUERY_ROOMS);
            queryRooms.setString(1, theatreId);
            ResultSet res = queryRooms.executeQuery();
            while (res.next()) {
                Room room = new Room(res.getInt(RoomDB.THEATRE_ID), res.getInt(RoomDB.ROOM_NUMBER));
                room.setNumberOfRows(res.getInt(RoomDB.SECTIONS));
                room.setSeatsPerRow(res.getInt(RoomDB.SEATS));
                rooms.add(room);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryRooms);
            DBConnection.close(conn);
        }
        return rooms;
    }

    public Room getRoomById(String theatreId, String roomNumber) throws Exception {
        String QUERY_ROOM_BY_ID = String.format(
            "SELECT %s, %s, %s, %s FROM %s WHERE %s=? AND %s=?;",
            RoomDB.THEATRE_ID, RoomDB.ROOM_NUMBER, RoomDB.SECTIONS,
            RoomDB.SEATS, RoomDB.TABLE, RoomDB.THEATRE_ID,
            RoomDB.ROOM_NUMBER
        );

        Room room = null;
        Connection conn = null;
        PreparedStatement queryRoomById = null;
        try {
            conn = DBConnection.connect();
            queryRoomById = conn.prepareStatement(QUERY_ROOM_BY_ID);
            queryRoomById.setString(1, theatreId);
            queryRoomById.setString(2, roomNumber);
            ResultSet res = queryRoomById.executeQuery();
            while (res.next()) {
                room = new Room(res.getInt("theatre_id"), res.getInt("room_number"));
                room.setNumberOfRows(res.getInt("sections"));
                room.setSeatsPerRow(res.getInt("seats"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryRoomById);
            DBConnection.close(conn);
        }
        return room;
    }

    public String createRoom(String theatreId, String roomNumber, String sections, String seats) throws Exception {
        String INSERT_ROOM = String.format(
            "INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);",
            RoomDB.TABLE, RoomDB.THEATRE_ID, RoomDB.ROOM_NUMBER,
            RoomDB.SECTIONS, RoomDB.SEATS
        );

        Connection conn = null;
        PreparedStatement insertRoom = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            insertRoom = conn.prepareStatement(INSERT_ROOM);
            insertRoom.setString(1, theatreId);
            insertRoom.setString(2, roomNumber);
            insertRoom.setString(3, sections);
            insertRoom.setString(4, seats);
            insertRoom.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to create room";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(insertRoom);
            DBConnection.close(conn);
        }
        return "";
    }

    public String updateRoom(String theatreId, String roomId, String roomNumber, String sections, String seats)
            throws Exception {
        String UPDATE_ROOM = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=? AND %s=?;",
            RoomDB.TABLE, RoomDB.ROOM_NUMBER, RoomDB.SECTIONS,
            RoomDB.SEATS, RoomDB.THEATRE_ID, RoomDB.ROOM_NUMBER
        );

        Connection conn = null;
        PreparedStatement updateRoom = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            updateRoom = conn.prepareStatement(UPDATE_ROOM);
            updateRoom.setString(1, roomNumber);
            updateRoom.setString(2, sections);
            updateRoom.setString(3, seats);
            updateRoom.setString(4, theatreId);
            updateRoom.setString(5, roomId);
            updateRoom.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to update room";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(updateRoom);
            DBConnection.close(conn);
        }
        return "";
    }

    public String deleteRoom(String theatreId, String roomNumber) throws Exception {
        String DELETE_TICKET = String.format(
            "DELETE FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s=? AND %s=?)",
            TicketDB.TABLE, TicketDB.SCHEDULE_ID, ScheduleDB.SCHEDULE_ID,
            ScheduleDB.TABLE, ScheduleDB.THEATRE_ID, ScheduleDB.ROOM_NUMBER
        );
        String DELETE_SCHEDULE = String.format(
            "DELETE FROM %s WHERE %s=? AND %s=?;",
            ScheduleDB.TABLE, ScheduleDB.THEATRE_ID, ScheduleDB.ROOM_NUMBER
        );
        String DELETE_ROOM = String.format(
            "DELETE FROM %s WHERE %s=? AND %s=?;",
            RoomDB.TABLE, RoomDB.THEATRE_ID, RoomDB.ROOM_NUMBER
        );

        Connection conn = null;
        PreparedStatement deleteTicket = null;
        PreparedStatement deleteSchedule = null;
        PreparedStatement deleteRoom = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            deleteTicket = conn.prepareStatement(DELETE_TICKET);
            deleteTicket.setString(1, theatreId);
            deleteTicket.setString(2, roomNumber);
            deleteTicket.executeUpdate();

            deleteSchedule = conn.prepareStatement(DELETE_SCHEDULE);
            deleteSchedule.setString(1, theatreId);
            deleteSchedule.setString(2, roomNumber);
            deleteSchedule.executeUpdate();

            deleteRoom = conn.prepareStatement(DELETE_ROOM);
            deleteRoom.setString(1, theatreId);
            deleteRoom.setString(2, roomNumber);
            deleteRoom.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to delete room";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(deleteTicket);
            DBConnection.close(deleteSchedule);
            DBConnection.close(deleteRoom);
            DBConnection.close(conn);
        }
        return "";
    }

    public List<TicketPrice> listTicketPrices(String theatreId) throws Exception {
        String QUERY_TICKET_PRICES = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s=? ORDER BY %s;",
            TicketPriceDB.THEATRE_ID, TicketPriceDB.START_TIME, TicketPriceDB.PRICE,
            TicketPriceDB.TABLE, TicketPriceDB.THEATRE_ID, TicketPriceDB.START_TIME
        );

        List<TicketPrice> ticketPrices = new LinkedList<>();
        Connection conn = null;
        PreparedStatement queryTicketPrices = null;
        try {
            conn = DBConnection.connect();
            queryTicketPrices = conn.prepareStatement(QUERY_TICKET_PRICES);
            queryTicketPrices.setString(1, theatreId);
            ResultSet res = queryTicketPrices.executeQuery();
            while (res.next()) {
                TicketPrice ticketPrice = new TicketPrice(
                    res.getInt(TicketPriceDB.THEATRE_ID),
                    LocalTime.parse(res.getString(TicketPriceDB.START_TIME)));
                ticketPrice.setPrice(res.getDouble(TicketPriceDB.PRICE));
                ticketPrices.add(ticketPrice);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryTicketPrices);
            DBConnection.close(conn);
        }
        return ticketPrices;
    }

    public TicketPrice getTicketPrice(String theatreId, String startTime) throws Exception {
        String QUERY_TICKET_PRICE = String.format(
            "SELECT %s, %s, %s FROM %s WHERE %s=? AND %s=?;",
            TicketPriceDB.THEATRE_ID, TicketPriceDB.START_TIME, TicketPriceDB.PRICE,
            TicketPriceDB.TABLE, TicketPriceDB.THEATRE_ID, TicketPriceDB.START_TIME
        );

        TicketPrice ticketPrice = null;
        Connection conn = null;
        PreparedStatement queryTicketPrice = null;
        try {
            conn = DBConnection.connect();
            queryTicketPrice = conn.prepareStatement(QUERY_TICKET_PRICE);
            queryTicketPrice.setString(1, theatreId);
            queryTicketPrice.setString(2, startTime);
            ResultSet res = queryTicketPrice.executeQuery();
            while (res.next()) {
                ticketPrice = new TicketPrice(
                        res.getInt(TicketPriceDB.THEATRE_ID),
                        LocalTime.parse(res.getString(TicketPriceDB.START_TIME)));
                ticketPrice.setPrice(res.getDouble(TicketPriceDB.PRICE));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryTicketPrice);
            DBConnection.close(conn);
        }
        return ticketPrice;
    }

    public String addTicketPrice(String theatreId, String startTime, String price) throws Exception {
        String INSERT_TICKET_PRICE = String.format(
            "INSERT INTO %s (%s, %s, %s) VALUES (?,?,?);",
            TicketPriceDB.TABLE, TicketPriceDB.THEATRE_ID, TicketPriceDB.START_TIME,
            TicketPriceDB.PRICE
        );

        Connection conn = null;
        PreparedStatement insertTicketPrice = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            insertTicketPrice = conn.prepareStatement(INSERT_TICKET_PRICE);
            insertTicketPrice.setString(1, theatreId);
            insertTicketPrice.setString(2, startTime);
            insertTicketPrice.setString(3, price);
            insertTicketPrice.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to add ticket price";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(insertTicketPrice);
            DBConnection.close(conn);
        }
        return "";
    }

    public String deleteTicketPrice(String theatreId, String startTime) throws Exception {
        String DELETE_TICKET_PRICE = String.format(
            "DELETE FROM %s WHERE %s=? AND %s=?;",
            TicketPriceDB.TABLE, TicketPriceDB.THEATRE_ID, TicketPriceDB.START_TIME
        );

        Connection conn = null;
        PreparedStatement deleteTicketPrice = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            deleteTicketPrice = conn.prepareStatement(DELETE_TICKET_PRICE);
            deleteTicketPrice.setString(1, theatreId);
            deleteTicketPrice.setString(2, startTime);
            deleteTicketPrice.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to delete ticket price";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(deleteTicketPrice);
            DBConnection.close(conn);
        }
        return "";
    }
}
