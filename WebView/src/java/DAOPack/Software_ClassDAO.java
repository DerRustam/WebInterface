package DAOPack;

import EntityPack.Software_Class;
import control_pack.ServiceController;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Software_ClassDAO extends DAO<Software_Class> {

    private String sql_selectByKey;
    private String sql_getall;
    private String sql_insert;
    private String sql_update;
    private String sql_delete;
    private String err_msg;

    public Software_ClassDAO() {
        try {
            sql_selectByKey = ServiceController.getQueryFromFile("SClass_selectByKey.sql");
            sql_getall = ServiceController.getQueryFromFile("SClass_getAll.sql");
            sql_insert = ServiceController.getQueryFromFile("SClass_insert.sql");
            sql_update = ServiceController.getQueryFromFile("SClass_update.sql");
            sql_delete = ServiceController.getQueryFromFile("SClass_delete.sql");
            err_msg = "Software Class with Name: %s ";
        } catch (IOException ex) {
            System.out.println("<Software_Class> scripts not loaded");
        }
    }

    private SQLException notFoundException(String class_name) {
        return new SQLException(String.format(err_msg + "not found.", class_name));
    }

    private SQLException alreadyException(String class_name) {
        return new SQLException(String.format(err_msg + "already in database.", class_name));
    }

    @Override
    public ArrayList<Software_Class> getAll() throws SQLException {
        ArrayList<Software_Class> software_classes = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_getall);
            Software_Class o;
            while (rs.next()) {
                o = convertToEntity(rs);
                software_classes.add(o);
            }
        }
        return software_classes;
    }

    public int getIdByKey(String class_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
            ps.setString(1, class_name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw notFoundException(class_name);
        }
    }

    private int getId(String class_name, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
        ps.setString(1, class_name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException();
    }

    @Override
    public Software_Class convertToEntity(ResultSet rs) throws SQLException {
        String t = rs.getString(3) == null
            ? null
            : rs.getString(3).trim();
        
        Software_Class sc = new Software_Class(
                rs.getString(2).trim(),
                t
        );
        return sc;
    }

    public void insert(String class_name, int class_parent_id) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_insert);
            ps.setString(1, class_name);
            if (class_parent_id == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, class_parent_id);
            }
            ps.executeUpdate();
        }
    }

    public void delete(String class_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(class_name, connection);
            PreparedStatement ps = connection.prepareStatement(sql_delete);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void update(String key_class_name, String class_name, int class_parent_id) throws SQLException {
        boolean isFound = false;
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(key_class_name, connection);
            isFound = true;
            PreparedStatement ps = connection.prepareStatement(sql_update);
            ps.setString(1, class_name);
            if (class_parent_id == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, class_parent_id);
            }
            ps.setString(3, key_class_name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            if (isFound) {
                throw alreadyException(class_name);
            }
            throw notFoundException(key_class_name);
        }
    }
}
