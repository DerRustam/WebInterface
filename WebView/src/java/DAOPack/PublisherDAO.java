package DAOPack;

import EntityPack.Publisher;
import control_pack.ServiceController;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class PublisherDAO extends DAO<Publisher> {

    private String sql_selectByKey;
    private String sql_getall;
    private String sql_insert;
    private String sql_update;
    private String sql_delete;
    private String err_msg;

    public PublisherDAO() {
        try {
            sql_selectByKey = ServiceController.getQueryFromFile("Publisher_selectByKey.sql");
            sql_getall = ServiceController.getQueryFromFile("Publisher_getAll.sql");
            sql_insert = ServiceController.getQueryFromFile("Publisher_insert.sql");
            sql_update = ServiceController.getQueryFromFile("Publisher_update.sql");
            sql_delete = ServiceController.getQueryFromFile("Publisher_delete.sql");
            err_msg = "Publisher with Name: %s ";
        } catch (IOException ex) {
            System.out.println("<Publisher> scripts not loaded");
        }
    }

    private SQLException notFoundException(String publisher_name) {
        return new SQLException(String.format(err_msg + "not found.", publisher_name));
    }

    private SQLException alreadyException(String publisher_name) {
        return new SQLException(String.format(err_msg + "already in database.", publisher_name));
    }

    @Override
    public ArrayList<Publisher> getAll() throws SQLException {
        ArrayList<Publisher> publishers = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_getall);
            publishers.clear();
            Publisher a;
            while (rs.next()) {
                a = convertToEntity(rs);
                publishers.add(a);
            }
        }
        return publishers;
    }

    public int getIdByKey(String name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw notFoundException(name);
        }
    }

    private int getId(String publisher_name, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
        ps.setString(1, publisher_name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException();
    }

    @Override
    public Publisher convertToEntity(ResultSet rs) throws SQLException {
        Publisher publisher = new Publisher(rs.getString(2).trim());
        return publisher;
    }

    public void delete(String publisher_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(publisher_name, connection);
            PreparedStatement ps = connection.prepareStatement(sql_delete);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw notFoundException(publisher_name);
        }
    }

    public void insert(String publisher_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_insert);
            ps.setString(1, publisher_name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw alreadyException(publisher_name);
        }
    }

    public void update(String key_publisher_name, String publisher_name) throws SQLException {
        boolean isFound = false;
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(key_publisher_name, connection);
            isFound = true;
            PreparedStatement ps = connection.prepareStatement(sql_update);
            ps.setString(1, publisher_name);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            if (isFound) {
                throw alreadyException(publisher_name);
            }
            throw notFoundException(publisher_name);
        }
    }
}
