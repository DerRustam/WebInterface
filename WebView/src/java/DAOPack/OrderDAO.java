package DAOPack;

import EntityPack.Order;
import control_pack.ServiceController;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class OrderDAO extends DAO<Order> {

    private String sql_selectByKey;
    private String sql_getall;
    private String sql_insert;
    private String sql_update;
    private String sql_delete;
    private String err_msg;
    public OrderDAO() {
        try {
            sql_selectByKey = ServiceController.getQueryFromFile("Order_selectByKey.sql");
            sql_getall = ServiceController.getQueryFromFile("Order_getAll.sql");
            sql_insert = ServiceController.getQueryFromFile("Order_insert.sql");
            sql_update = ServiceController.getQueryFromFile("Order_update.sql");
            sql_delete = ServiceController.getQueryFromFile("Order_delete.sql");
            err_msg = "Order with Pay Code: %s ";
        } catch (IOException ex) {
            System.out.println("<Order> scripts not loaded");
        }
    }

    private SQLException notFoundException(String key) {
        return new SQLException(String.format(err_msg +"not found.", key));
    }

    private SQLException alreadyException(String key) {
        return new SQLException(String.format(err_msg + "already in database.", key));
    }

    @Override
    public ArrayList<Order> getAll() throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_getall);
            Order o;
            while (rs.next()) {
                o = convertToEntity(rs);
                orders.add(o);
            }
        }
        return orders;
    }

    public int getIdByKey(String pay_code) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
            ps.setString(1, pay_code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw notFoundException(pay_code);
        }
    }

    private int getId(String pay_code, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
        ps.setString(1, pay_code);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException();
    }

    @Override
    public Order convertToEntity(ResultSet rs) throws SQLException {
        Order order = new Order(
                rs.getString(2).trim(),
                rs.getTimestamp(3).toLocalDateTime().toString().replace('T', ' '),
                rs.getString(4).replace('?', '$').trim(),
                rs.getString(5).trim()
        );
        return order;
    }

    public void insert(int account_id, Timestamp order_datetime,
            int summary_price, String pay_code) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_insert);
            ps.setInt(1, account_id);
            ps.setTimestamp(2, order_datetime);
            ps.setInt(3, summary_price);
            ps.setString(4, pay_code);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw alreadyException(pay_code);
        }
    }

    public void delete(String pay_code) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(pay_code, connection);
            PreparedStatement ps = connection.prepareStatement(sql_delete);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch (SQLException ex) {
            throw notFoundException(pay_code);
        }
    }

    public void update(String key_pay_code, int account_id, Timestamp order_datetime,
            int summary_price, String pay_code) throws SQLException {
        boolean isFound = false;
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(key_pay_code, connection);
            isFound = true;
            PreparedStatement ps = connection.prepareStatement(sql_update);
            ps.setInt(1, account_id);
            ps.setTimestamp(2, order_datetime);
            ps.setInt(3, summary_price);
            ps.setString(4, pay_code);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            if (isFound) {
                throw alreadyException(pay_code);
            }
            throw notFoundException(pay_code);
        }
    }
}
