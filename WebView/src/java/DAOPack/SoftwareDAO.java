package DAOPack;

import EntityPack.Software;
import DTO.SoftwareDTO;
import control_pack.ServiceController;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class SoftwareDAO extends DAO<Software> {

    private String sql_getall;
    private String sql_insert;
    private String sql_update;
    private String sql_delete;
    private String sql_selectByKey;
    private String sql_view;
    private String err_msg;

    public SoftwareDAO() {
        try {
            sql_selectByKey = ServiceController.getQueryFromFile("Software_selectByKey.sql");
            sql_getall = ServiceController.getQueryFromFile("Software_getAll.sql");
            sql_insert = ServiceController.getQueryFromFile("Software_insert.sql");
            sql_update = ServiceController.getQueryFromFile("Software_update.sql");
            sql_delete = ServiceController.getQueryFromFile("Software_delete.sql");
            sql_view = ServiceController.getQueryFromFile("Software_getView.sql");
            err_msg = "Software with Title: %s , Software Class Name: %s ";
        } catch (IOException ex) {
            System.out.println("<Software> scripts not loaded");
        }
    }

    private SQLException notFoundException(String title, String class_name) {
        return new SQLException(String.format(err_msg + "not found.", title, class_name));
    }

    private SQLException alreadyException(String title, String class_name) {
        return new SQLException(String.format(err_msg + "already in database.", title, class_name));
    }

    public ArrayList<SoftwareDTO> getList() throws SQLException {
        ArrayList<SoftwareDTO> alist = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_view);
            SoftwareDTO sdto;
            while (rs.next()) {
                sdto = new SoftwareDTO(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3).replace('?', '$')
                );
                alist.add(sdto);
            }
        }
        return alist;
    }

    @Override
    public ArrayList<Software> getAll() throws SQLException {
        ArrayList<Software> list = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_getall);
            Software o;
            while (rs.next()) {
                o = convertToEntity(rs);
                list.add(o);
            }
        }
        return list;
    }

    public int getIdByKey(String title, String class_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
            ps.setString(1, class_name);
            ps.setString(2, title);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw notFoundException(title, class_name);
        }
    }

    private int getId(String title, String class_name, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
        ps.setString(1, class_name);
        ps.setString(2, title);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException();
    }

    @Override
    public Software convertToEntity(ResultSet rs) throws SQLException {
        String pub = rs.getString(3) == null
                ? null
                : rs.getString(3).trim();
        String rel = rs.getDate(5) == null
                ? null
                : rs.getDate(5).toString().trim();
        Software software = new Software(
                rs.getString(2).trim(),
                pub,
                rs.getString(4).trim(),
                rel,
                rs.getString(6).charAt(0),
                rs.getString(7).replace('?', '$').trim()
        );
        return software;
    }

    public void insert(int class_id, int publisher_id, String title, Date release_date, String esrb, int actual_price) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT nextval('software_seq');");
            rs.next();
            PreparedStatement ps = connection.prepareStatement(sql_insert);

            ps.setInt(1, class_id);
            if (publisher_id == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, publisher_id);
            }
            ps.setString(3, title);
            ps.setDate(4, release_date);
            ps.setString(5, esrb);
            ps.setInt(6, actual_price);
            ps.executeUpdate();
        }
    }

    public void delete(String title, String class_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(title, class_name, connection);
            PreparedStatement ps = connection.prepareStatement(sql_delete);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw notFoundException(title, class_name);
        }
    }

    public void update(String key_title, String key_class_name, String class_name, int class_id,
            int publisher_id, String title, Date release_date, String esrb,
            int actual_price) throws SQLException {
        boolean isFound = false;
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(key_title, key_class_name, connection);
            isFound = true;
            PreparedStatement ps = connection.prepareStatement(sql_update);
            ps.setInt(1, class_id);
            if (publisher_id == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, publisher_id);
            }
            ps.setString(3, title);
            if (release_date == null) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, release_date);
            }
            ps.setString(5, esrb);
            ps.setInt(6, actual_price);
            ps.setInt(7, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            if (isFound) {
                throw alreadyException(title, class_name);
            }
            throw notFoundException(key_title, key_class_name);
        }
    }
}
