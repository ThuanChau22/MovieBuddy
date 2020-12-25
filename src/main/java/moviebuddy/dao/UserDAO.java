package moviebuddy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;

import moviebuddy.util.Passwords;
import moviebuddy.util.DBConnection;
import moviebuddy.util.S;
import moviebuddy.db.UserDB;
import moviebuddy.db.RegisteredDB;
import moviebuddy.db.MembershipDB;
import moviebuddy.db.ProviderDB;
import moviebuddy.db.RoleDB;
import moviebuddy.db.EmployDB;
import moviebuddy.model.User;
import moviebuddy.model.Role;

public class UserDAO {

    public User signInCustomer(String email, String password) throws Exception {
        User user = getRegisteredUser(email);
        if (user != null
                && Passwords.isExpectedPassword(password.toCharArray(), user.getSalt(), user.getHashPassword())) {
            return user;
        }
        return null;
    }

    public User signInProvider(String staffId, String password) throws Exception {
        User user = getProviderByStaffId(staffId);
        if (user != null
                && Passwords.isExpectedPassword(password.toCharArray(), user.getSalt(), user.getHashPassword())) {
            return user;
        }
        return null;
    }

    public User getRegisteredUser(String email) throws Exception {
        String QUERY_REGISTERED_USER = String.format(
            "SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s=?;",
            RegisteredDB.ACCOUNT_ID, RegisteredDB.NAME, RegisteredDB.EMAIL,
            RegisteredDB.HASH_PASSWORD, RegisteredDB.SALT, RegisteredDB.ZIP_CODE,
            RegisteredDB.TABLE, RegisteredDB.EMAIL
        );

        User user = null;
        Connection conn =null;
        PreparedStatement getRegisteredUser = null;
        try {
            conn = DBConnection.connect();
            getRegisteredUser = conn.prepareStatement(QUERY_REGISTERED_USER);
            getRegisteredUser.setString(1, email);
            ResultSet res = getRegisteredUser.executeQuery();
            while (res.next()) {
                user = new User(res.getInt(RegisteredDB.ACCOUNT_ID));
                user.setUserName(res.getString(RegisteredDB.NAME));
                user.setEmail(res.getString(RegisteredDB.EMAIL));
                user.setHashPassword(res.getBytes(RegisteredDB.HASH_PASSWORD));
                user.setSalt(res.getBytes(RegisteredDB.SALT));
                user.setZip(res.getString(RegisteredDB.ZIP_CODE));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(getRegisteredUser);
            DBConnection.close(conn);
        }
        return user;
    }

    public User getProviderByStaffId(String staffId) throws Exception {
        String QUERY_PROVIDER_BY_ID = String.format(
            "SELECT ru.%s, ru.%s, ru.%s, ru.%s, ru.%s, ru.%s, m.%s, m.%s, m.%s, p.%s, r.%s, e.%s FROM %s ru JOIN %s m ON m.%s=ru.%s JOIN %s p ON p.%s=m.%s JOIN %s r ON r.%s=p.%s LEFT OUTER JOIN %s e ON e.%s=p.%s WHERE p.%s=?;",
            RegisteredDB.ACCOUNT_ID, RegisteredDB.NAME, RegisteredDB.EMAIL,
            RegisteredDB.HASH_PASSWORD, RegisteredDB.SALT, RegisteredDB.ZIP_CODE,
            MembershipDB.POINTS, MembershipDB.AUTO_RENEW, MembershipDB.END_DATE,
            ProviderDB.STAFF_ID, RoleDB.TITLE, EmployDB.THEATRE_ID,
            RegisteredDB.TABLE, MembershipDB.TABLE, MembershipDB.ACCOUNT_ID,
            RegisteredDB.ACCOUNT_ID, ProviderDB.TABLE, ProviderDB.ACCOUNT_ID,
            MembershipDB.ACCOUNT_ID, RoleDB.TABLE, RoleDB.ROLE_ID,
            ProviderDB.ROLE_ID, EmployDB.TABLE, EmployDB.STAFF_ID,
            ProviderDB.STAFF_ID, ProviderDB.STAFF_ID
        );

        User user = null;
        Connection conn = null;
        PreparedStatement getRegisteredUser = null;
        try {
            conn = DBConnection.connect();
            getRegisteredUser = conn.prepareStatement(QUERY_PROVIDER_BY_ID);
            getRegisteredUser.setString(1, staffId);
            ResultSet res = getRegisteredUser.executeQuery();
            while (res.next()) {
                user = new User(res.getInt(RegisteredDB.ACCOUNT_ID));
                user.setUserName(res.getString(RegisteredDB.NAME));
                user.setEmail(res.getString(RegisteredDB.EMAIL));
                user.setHashPassword(res.getBytes(RegisteredDB.HASH_PASSWORD));
                user.setSalt(res.getBytes(RegisteredDB.SALT));
                user.setZip(res.getString(RegisteredDB.ZIP_CODE));
                user.setBuddyPoints(res.getInt(MembershipDB.POINTS));
                user.setAutoRenew(res.getBoolean(MembershipDB.AUTO_RENEW));
                user.setEndDate(LocalDate.parse(res.getString(MembershipDB.END_DATE)));
                user.setStaffId(res.getInt(ProviderDB.STAFF_ID));
                user.setRole(res.getString(RoleDB.TITLE));
                user.setTheatre_id(res.getInt(EmployDB.THEATRE_ID));
            }
        } catch (Exception e) {
            throw e;
        } finally{
            DBConnection.close(getRegisteredUser);
            DBConnection.close(conn);
        }
        return user;
    }

    public List<User> listAdminUsers() throws Exception {
        String QUERY_ADMINS = String.format(
            "SELECT p.%s, r.%s, ru.%s, ru.%s, ru.%s FROM %s p JOIN %s r ON r.%s=p.%s JOIN %s ru ON ru.%s=p.%s WHERE r.%s='admin' ORDER BY p.%s;",
            ProviderDB.STAFF_ID, RoleDB.TITLE, RegisteredDB.ACCOUNT_ID,
            RegisteredDB.NAME, RegisteredDB.EMAIL, ProviderDB.TABLE,
            RoleDB.TABLE, RoleDB.ROLE_ID, ProviderDB.ROLE_ID,
            RegisteredDB.TABLE, RegisteredDB.ACCOUNT_ID, ProviderDB.ACCOUNT_ID,
            RoleDB.TITLE, ProviderDB.STAFF_ID
        );

        List<User> admins = new LinkedList<>();
        Connection conn = null;
        PreparedStatement queryAdmins = null;
        try {
            conn = DBConnection.connect();
            queryAdmins = conn.prepareStatement(QUERY_ADMINS);
            ResultSet res = queryAdmins.executeQuery();
            while (res.next()) {
                User admin = new User(res.getInt(RegisteredDB.ACCOUNT_ID));
                admin.setUserName(res.getString(RegisteredDB.NAME));
                admin.setEmail(res.getString(RegisteredDB.EMAIL));
                admin.setStaffId(res.getInt(ProviderDB.STAFF_ID));
                admin.setRole(res.getString(RoleDB.TITLE));
                admins.add(admin);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryAdmins);
            DBConnection.close(conn);
        }
        return admins;
    }

    public List<User> listProviderByTheatreId(String theatreId) throws Exception {
        String QUERY_PROVIDER_BY_THEATRE_ID = String.format(
            "SELECT p.%s, r.%s, ru.%s, ru.%s, ru.%s FROM %s p JOIN %s r ON r.%s=p.%s JOIN %s ru ON ru.%s=p.%s JOIN %s e ON e.%s=p.%s WHERE e.%s=? ORDER BY r.%s DESC, ru.%s;",
            ProviderDB.STAFF_ID, RoleDB.TITLE, RegisteredDB.ACCOUNT_ID,
            RegisteredDB.NAME, RegisteredDB.EMAIL, ProviderDB.TABLE,
            RoleDB.TABLE, RoleDB.ROLE_ID, ProviderDB.ROLE_ID,
            RegisteredDB.TABLE, RegisteredDB.ACCOUNT_ID, ProviderDB.ACCOUNT_ID,
            EmployDB.TABLE, EmployDB.STAFF_ID, ProviderDB.STAFF_ID,
            EmployDB.THEATRE_ID, RoleDB.TITLE, RegisteredDB.NAME
        );

        List<User> staffs = new LinkedList<>();
        Connection conn = null;
        PreparedStatement queryProviderByTheatreId = null;
        try {
            conn = DBConnection.connect();
            queryProviderByTheatreId = conn.prepareStatement(QUERY_PROVIDER_BY_THEATRE_ID);
            queryProviderByTheatreId.setString(1, theatreId);
            ResultSet res = queryProviderByTheatreId.executeQuery();
            while (res.next()) {
                User staff = new User(res.getInt(RegisteredDB.ACCOUNT_ID));
                staff.setUserName(res.getString(RegisteredDB.NAME));
                staff.setEmail(res.getString(RegisteredDB.EMAIL));
                staff.setStaffId(res.getInt(ProviderDB.STAFF_ID));
                staff.setRole(res.getString(RoleDB.TITLE));
                staffs.add(staff);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryProviderByTheatreId);
            DBConnection.close(conn);
        }
        return staffs;
    }

    public List<Role> listRoles() throws Exception {
        String QUERY_ROLES = String.format(
                "SELECT %s, %s FROM %s;",
                RoleDB.ROLE_ID, RoleDB.TITLE, RoleDB.TABLE
        );

        List<Role> roles = new LinkedList<>();
        Connection conn = null;
        PreparedStatement queryRoles = null;
        try {
            conn = DBConnection.connect();
            queryRoles = conn.prepareStatement(QUERY_ROLES);
            ResultSet res = queryRoles.executeQuery();
            while (res.next()) {
                Role role = new Role(res.getInt(RoleDB.ROLE_ID));
                role.setTitle(res.getString(RoleDB.TITLE));
                roles.add(role);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryRoles);
            DBConnection.close(conn);
        }
        return roles;
    }

    public int getEmployTheatreId(String staffId) throws Exception {
        String QUERY_EMPLOY_THEATRE_ID = String.format(
            "SELECT theatre_id FROM employ WHERE staff_id=?;",
            EmployDB.THEATRE_ID, EmployDB.TABLE, EmployDB.STAFF_ID
        );

        int theatreId = 0;
        Connection conn = null;
        PreparedStatement queryEmployTheatreId = null;
        try {
            conn = DBConnection.connect();
            queryEmployTheatreId = conn.prepareStatement(QUERY_EMPLOY_THEATRE_ID);
            queryEmployTheatreId.setString(1, staffId);
            ResultSet res = queryEmployTheatreId.executeQuery();
            while (res.next()) {
                theatreId = res.getInt(EmployDB.THEATRE_ID);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DBConnection.close(queryEmployTheatreId);
            DBConnection.close(conn);
        }
        return theatreId;
    }

    public String createRegisteredUser(String userName, String email, String password) throws Exception {
        String INSERT_USER = String.format(
            "INSERT INTO %s(%s) VALUES(?);",
            UserDB.TABLE, UserDB.USER_TYPE
        );
        String INSERT_REGISTERED_USER = String.format(
            "INSERT INTO %s(%s, %s, %s, %s, %s) VALUES (LAST_INSERT_ID(), ?, ?, ?, ?);",
            RegisteredDB.TABLE, RegisteredDB.ACCOUNT_ID, RegisteredDB.NAME,
            RegisteredDB.EMAIL, RegisteredDB.HASH_PASSWORD, RegisteredDB.SALT
        );

        Connection conn = null;
        PreparedStatement insertUser = null;
        PreparedStatement insertRegisteredUser = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            insertUser = conn.prepareStatement(INSERT_USER);
            insertUser.setString(1, UserDB.USER_TYPE_REGISTERED);
            insertUser.executeUpdate();

            insertRegisteredUser = conn.prepareStatement(INSERT_REGISTERED_USER);
            insertRegisteredUser.setString(1, userName);
            insertRegisteredUser.setString(2, email);
            byte[] salt = Passwords.getSalt();
            byte[] hashpw = Passwords.hash(password.toCharArray(), salt);
            insertRegisteredUser.setBytes(3, hashpw);
            insertRegisteredUser.setBytes(4, salt);
            insertRegisteredUser.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to create account";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(insertUser);
            DBConnection.close(insertRegisteredUser);
            DBConnection.close(conn);
        }
        return "";
    }

    public String createProvider(String role, String theatreLocation, String userName, String email, String password)
            throws Exception {
        String INSERT_USER = String.format(
            "INSERT INTO %s(%s) VALUES(?);",
            UserDB.TABLE, UserDB.USER_TYPE
        );
        String INSERT_REGISTERED_USER = String.format(
            "INSERT INTO %s(%s, %s, %s, %s, %s) VALUES (LAST_INSERT_ID(), ?, ?, ?, ?);",
            RegisteredDB.TABLE, RegisteredDB.ACCOUNT_ID, RegisteredDB.NAME,
            RegisteredDB.EMAIL, RegisteredDB.HASH_PASSWORD, RegisteredDB.SALT
        );
        String INSERT_MEMBERSHIP_USER = String.format(
            "INSERT INTO %s(%s, %s) VALUES (LAST_INSERT_ID(), ?);",
            MembershipDB.TABLE, MembershipDB.ACCOUNT_ID, MembershipDB.AUTO_RENEW
        );
        String INSERT_PROVIDER = String.format(
            "INSERT INTO %s(%s, %s) VALUES (LAST_INSERT_ID(), (SELECT %s FROM %s WHERE %s=?));",
            ProviderDB.TABLE, ProviderDB.ACCOUNT_ID, ProviderDB.ROLE_ID,
            RoleDB.ROLE_ID, RoleDB.TABLE, RoleDB.TITLE
        );
        String INSERT_EMPLOY = String.format(
            "INSERT INTO %s(%s, %s) VALUES (LAST_INSERT_ID(), ?);",
            EmployDB.TABLE, EmployDB.STAFF_ID, EmployDB.THEATRE_ID
        );

        int autoRenew = 1;
        Connection conn = null;
        PreparedStatement insertUser = null;
        PreparedStatement insertRegisteredUser = null;
        PreparedStatement insertMembership = null;
        PreparedStatement insertProvider = null;
        PreparedStatement insertEmploy = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            insertUser = conn.prepareStatement(INSERT_USER);
            insertUser.setString(1, UserDB.USER_TYPE_REGISTERED);
            insertUser.executeUpdate();

            insertRegisteredUser = conn.prepareStatement(INSERT_REGISTERED_USER);
            insertRegisteredUser.setString(1, userName);
            insertRegisteredUser.setString(2, email);
            byte[] salt = Passwords.getSalt();
            byte[] hashpw = Passwords.hash(password.toCharArray(), salt);
            insertRegisteredUser.setBytes(3, hashpw);
            insertRegisteredUser.setBytes(4, salt);
            insertRegisteredUser.executeUpdate();

            insertMembership = conn.prepareStatement(INSERT_MEMBERSHIP_USER);
            insertMembership.setInt(1, autoRenew);
            insertMembership.executeUpdate();

            insertProvider = conn.prepareStatement(INSERT_PROVIDER);
            insertProvider.setString(1, role);
            insertProvider.executeUpdate();

            if (!role.equals(S.ADMIN)) {
                insertEmploy = conn.prepareStatement(INSERT_EMPLOY);
                insertEmploy.setString(1, theatreLocation);
                insertEmploy.executeUpdate();
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
            return "Fail to create staff user";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(insertUser);
            DBConnection.close(insertRegisteredUser);
            DBConnection.close(insertMembership);
            DBConnection.close(insertProvider);
            DBConnection.close(insertEmploy);
            DBConnection.close(conn);
        }
        return "";
    }

    public String deleteProvider(String staffId) throws Exception {
        String DELETE_EMPLOY = String.format(
            "DELETE FROM %s WHERE %s=?;",
            EmployDB.TABLE, EmployDB.STAFF_ID
        );
        String QUERY_ACCOUNT_ID = String.format(
            "SELECT %s FROM %s WHERE %s=?",
            ProviderDB.ACCOUNT_ID, ProviderDB.TABLE, ProviderDB.STAFF_ID
        );
        String DELETE_PROVIDER = String.format(
            "DELETE FROM %s WHERE %s=?;",
            ProviderDB.TABLE, ProviderDB.ACCOUNT_ID
        );
        String DELETE_MEMBERSHIP = String.format(
            "DELETE FROM %s WHERE %s=?;",
            MembershipDB.TABLE, MembershipDB.ACCOUNT_ID
        );
        String DELETE_REGISTERED_USER = String.format(
            "DELETE FROM %s WHERE %s=?;",
            RegisteredDB.TABLE, RegisteredDB.ACCOUNT_ID
        );
        String DELETE_USER = String.format(
            "DELETE FROM user WHERE account_id=?;",
            UserDB.TABLE, UserDB.ACCOUNT_ID
        );

        Connection conn = null;
        PreparedStatement deleteEmploy = null;
        PreparedStatement queryAccountId = null;
        PreparedStatement deleteProvider = null;
        PreparedStatement deleteMembership = null;
        PreparedStatement deleteRegisteredUser = null;
        PreparedStatement deleteUser = null;
        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            deleteEmploy = conn.prepareStatement(DELETE_EMPLOY);
            deleteEmploy.setString(1, staffId);
            deleteEmploy.executeUpdate();

            queryAccountId = conn.prepareStatement(QUERY_ACCOUNT_ID);
            queryAccountId.setString(1, staffId);
            ResultSet res = queryAccountId.executeQuery();
            String accountId = "";
            while (res.next()) {
                accountId = res.getString(ProviderDB.ACCOUNT_ID);
            }

            deleteProvider = conn.prepareStatement(DELETE_PROVIDER);
            deleteProvider.setString(1, accountId);
            deleteProvider.executeUpdate();

            deleteMembership = conn.prepareStatement(DELETE_MEMBERSHIP);
            deleteMembership.setString(1, accountId);
            deleteMembership.executeUpdate();

            deleteRegisteredUser = conn.prepareStatement(DELETE_REGISTERED_USER);
            deleteRegisteredUser.setString(1, accountId);
            deleteRegisteredUser.executeUpdate();

            deleteUser = conn.prepareStatement(DELETE_USER);
            deleteUser.setString(1, accountId);
            deleteUser.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "Fail to delete staff account";
        } finally {
            conn.setAutoCommit(true);
            DBConnection.close(deleteEmploy);
            DBConnection.close(queryAccountId);
            DBConnection.close(deleteProvider);
            DBConnection.close(deleteMembership);
            DBConnection.close(deleteRegisteredUser);
            DBConnection.close(deleteUser);
            DBConnection.close(conn);
        }
        return "";
    }
}
