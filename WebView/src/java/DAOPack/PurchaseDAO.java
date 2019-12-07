package DAOPack;

import EntityPack.Purchase;
import control_pack.ServiceController;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class PurchaseDAO extends DAO<Purchase> {

    private String sql_selectByKey;
    private String sql_getall;
    private String sql_insert;
    private String sql_update;
    private String sql_delete;
    private String err_msg;

    public PurchaseDAO() {
        try {
            sql_selectByKey = ServiceController.getQueryFromFile("Purchase_selectByKey.sql");
            sql_getall = ServiceController.getQueryFromFile("Purchase_getAll.sql");
            sql_insert = ServiceController.getQueryFromFile("Purchase_insert.sql");
            sql_update = ServiceController.getQueryFromFile("Purchase_update.sql");
            sql_delete = ServiceController.getQueryFromFile("Purchase_delete.sql");
            err_msg = "Purchase with Pay Code: %s , Title: %s , Software Class Name: %s ";
        } catch (IOException ex) {
            System.out.println("<Purchase> scripts not loaded");
        }
    }

    private SQLException notFoundException(String pay_code, String title, String class_name) {
        return new SQLException(String.format(err_msg + "not found.", pay_code, title, class_name));
    }

    private SQLException alreadyException(String pay_code, String title, String class_name) {
        return new SQLException(String.format(err_msg + "already in database.", pay_code, title, class_name));
    }

    @Override
    public ArrayList<Purchase> getAll() throws SQLException {
        ArrayList<Purchase> purchases = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_getall);
            Purchase o;
            while (rs.next()) {
                o = convertToEntity(rs);
                purchases.add(o);
            }
        }
        return purchases;
    }

    public int getIdByKey(String pay_code, String title, String class_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
            ps.setString(1, pay_code);
            ps.setString(2, title);
            ps.setString(3, class_name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw notFoundException(pay_code, title, class_name);

        }
    }

    private int getId(String pay_code, String title, String class_name, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
        ps.setString(1, pay_code);
        ps.setString(2, title);
        ps.setString(3, class_name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException();
    }

    @Override
    public Purchase convertToEntity(ResultSet rs) throws SQLException {
        Purchase purchase = new Purchase(
                rs.getString(2).trim(),
                rs.getString(3).trim(),
                rs.getString(4).trim(),
                rs.getShort(5),
                rs.getString(6).replace('?', '$').trim()
        );
        return purchase;
    }

    public void insert(String pay_code, String title, String class_name, int order_id, int software_id, short count, int price_single) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_insert);
            ps.setInt(1, order_id);
            ps.setInt(2, software_id);
            ps.setShort(3, count);
            ps.setInt(4, price_single);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw alreadyException(pay_code, title, class_name);
        }
    }

    public void delete(String pay_code, String title, String class_name) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(pay_code, title, class_name, connection);
            PreparedStatement ps = connection.prepareStatement(sql_delete);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw notFoundException(pay_code, title, class_name);
        }
    }

    public void update(String key_pay_code, String key_title, String key_class_name,
            String pay_code, String title, String class_name, int order_id,
            int software_id, short count, int price_single) throws SQLException {
        boolean isFound = false;
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(key_pay_code, key_title, key_class_name, connection);
            isFound = true;
            PreparedStatement ps = connection.prepareStatement(sql_update);
            ps.setInt(1, order_id);
            ps.setInt(2, software_id);
            ps.setShort(3, count);
            ps.setInt(4, price_single);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            if (isFound) {
                throw alreadyException(pay_code, title, class_name);
            }
            throw notFoundException(key_pay_code, key_title, key_class_name);
        }
    }
}
