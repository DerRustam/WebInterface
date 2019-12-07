package DAOPack;

import EntityPack.Product_Feedback;
import control_pack.ServiceController;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Product_FeedbackDAO extends DAO<Product_Feedback> {

    private String sql_selectByKey;
    private String sql_getall;
    private String sql_insert;
    private String sql_update;
    private String sql_delete;
    private String err_msg;

    public Product_FeedbackDAO() {
        try {
            sql_selectByKey = ServiceController.getQueryFromFile("Feedback_selectByKey.sql");
            sql_getall = ServiceController.getQueryFromFile("Feedback_getAll.sql");
            sql_insert = ServiceController.getQueryFromFile("Feedback_insert.sql");
            sql_update = ServiceController.getQueryFromFile("Feedback_update.sql");
            sql_delete = ServiceController.getQueryFromFile("Feedback_delete.sql");
            err_msg = "Product Feedback with EMail: %s , Title: %s , Software Class Name: %s ";
        } catch (IOException ex) {
            System.out.println("<Product_Feedback> scripts not loaded");
        }
    }

    private SQLException notFoundException(String e_mail, String title, String class_name) {
        return new SQLException(String.format(err_msg + "not found.", e_mail, title, class_name));
    }

    private SQLException alreadyException(String e_mail, String title, String class_name) {
        return new SQLException(String.format(err_msg + "already in database.", e_mail, title, class_name));
    }

    @Override
    public ArrayList<Product_Feedback> getAll() throws SQLException {
        ArrayList<Product_Feedback> product_feedbacks = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_getall);
            Product_Feedback o;
            while (rs.next()) {
                o = convertToEntity(rs);
                product_feedbacks.add(o);
            }
        }
        return product_feedbacks;
    }

    public int[] getIdByKey(String e_mail, String title, String class_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
            ps.setString(1, e_mail);
            ps.setString(2, title);
            ps.setString(3, class_name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new int[]{rs.getInt(1), rs.getInt(2)};
            }
            throw notFoundException(e_mail, title, class_name);
        }
    }

    private int[] getIds(String e_mail, String title, String class_name, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
        ps.setString(1, e_mail);
        ps.setString(2, title);
        ps.setString(3, class_name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new int[]{rs.getInt(1), rs.getInt(2)};
        }
        throw new SQLException();
    }

    @Override
    public Product_Feedback convertToEntity(ResultSet rs) throws SQLException {
        return new Product_Feedback(
                rs.getString(3).trim(),
                rs.getString(4).trim(),
                rs.getString(5).trim(),
                rs.getString(6).trim()
        );
    }

    public void delete(String e_mail, String title, String class_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int[] ids = getIds(e_mail, title, class_name, connection);
            PreparedStatement ps = connection.prepareStatement(sql_delete);
            ps.setInt(1, ids[0]);
            ps.setInt(2, ids[1]);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw notFoundException(e_mail, title, class_name);
        }
    }

    public void insert(String e_mail, String title, String class_name, int account_id, int software_id, String message) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_insert);
            ps.setInt(1, account_id);
            ps.setInt(2, software_id);
            ps.setString(3, message);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw alreadyException(e_mail, title, class_name);
        }
    }

    public void update(String key_e_mail, String key_title, String key_class_name,
            String e_mail, String title, String class_name,
            int account_id_new, int software_id_new, String message) throws SQLException {
        boolean isFound = false;
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int[] ids = getIds(key_e_mail, key_title, key_class_name, connection);
            PreparedStatement ps = connection.prepareStatement(sql_update);
            ps.setInt(1, account_id_new);
            ps.setInt(2, software_id_new);
            ps.setString(3, message);
            ps.setInt(4, ids[0]);
            ps.setInt(5, ids[1]);
            ps.executeUpdate();
        } catch (SQLException ex) {
            if (isFound) {
                throw alreadyException(e_mail, title, class_name);
            }
            throw notFoundException(key_e_mail, key_title, key_class_name);
        }
    }
}
