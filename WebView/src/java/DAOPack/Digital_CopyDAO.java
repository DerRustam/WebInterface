package DAOPack;

import EntityPack.Digital_Copy;
import control_pack.ServiceController;
import java.io.IOException;

import java.sql.*;
import java.util.ArrayList;

public class Digital_CopyDAO extends DAO<Digital_Copy>{

    private String sql_getall;
    private String sql_insert;
    private String sql_update;
    private String sql_delete;
    private String sql_selectByKey;
    private String err_msg;

    public Digital_CopyDAO() {
        try {
            sql_selectByKey = ServiceController.getQueryFromFile("Copy_selectByKey.sql");
            sql_getall = ServiceController.getQueryFromFile("Copy_getAll.sql");
            sql_insert = ServiceController.getQueryFromFile("Copy_insert.sql");
            sql_update = ServiceController.getQueryFromFile("Copy_update.sql");
            sql_delete = ServiceController.getQueryFromFile("Copy_delete.sql");
            err_msg = "Digital Copy with Title: %s , Class name: %s , product_key: %s ";
        } catch (IOException ex) {
            System.out.println("<Digital_Copy> scripts not loaded");
        }
    }

    private SQLException notFoundException(String title, String class_name, String product_key) {
        return new SQLException(String.format(err_msg + "not found.",title, class_name, product_key));
    }

    private SQLException alreadyException(String title, String class_name, String product_key) {
        return new SQLException(String.format(err_msg + "already in database.",
                title, class_name, product_key));
    }

    @Override
    public ArrayList<Digital_Copy> getAll() throws SQLException {
        ArrayList<Digital_Copy> digital_copies = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_getall);
            Digital_Copy o;
            while (rs.next()) {
                o = convertToEntity(rs);
                digital_copies.add(o);
            }
        }
        return digital_copies;
    }

    public int[] getIdByKey(String title, String software_class, String product_key) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
            ps.setString(1, title);
            ps.setString(2, software_class);
            ps.setString(3, product_key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new int[]{rs.getInt(1), rs.getInt(2), rs.getInt(3)};
            }
            throw notFoundException(title,software_class,product_key);
        }
    }
    
    private int getId(String title, String class_name, String product_key, Connection connection) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
           ps.setString(1, title);
            ps.setString(2, class_name);
            ps.setString(3, product_key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException();
    }

    @Override
    public Digital_Copy convertToEntity(ResultSet rs) throws SQLException {
        String val = rs.getString(2) == null
                ? null
                : rs.getString(2).trim();
        Digital_Copy copy = new Digital_Copy(
                val,
                rs.getString(3).trim(),
                rs.getString(4).trim(),
                rs.getString(5).trim()
        );
        return copy;
    }

    public void insert(String title, String class_name,int software_id, int purchase_id, String product_key) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_insert);
            ps.setInt(1, software_id);
            if (purchase_id == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, purchase_id);
            }
            ps.setString(3, product_key);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw alreadyException(title, class_name, product_key);
        }
    }

    public void delete(String title, String software_class, String product_key) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int key = getId(title, software_class, product_key, connection);
            PreparedStatement ps = connection.prepareStatement(sql_delete);
            ps.setInt(1, key);
            ps.executeUpdate();
        }
        catch(SQLException ex){throw notFoundException(title, software_class, product_key);}
    }

    public void update(String key_title, String key_class_name, String key_product_key,
            String title, String class_name, String product_key, int software_id,
            int purchase_id) throws SQLException {
        boolean isFound = false; 
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(key_title, key_class_name, key_product_key, connection);
            isFound = true;
            PreparedStatement ps = connection.prepareStatement(sql_update);
            ps.setInt(1, software_id);
            if (purchase_id == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, purchase_id);
            }
            ps.setString(3, product_key);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
        catch(SQLException ex){
            if (isFound){
                throw alreadyException(title, class_name, product_key);
            }
            throw notFoundException(key_title, key_class_name, key_product_key);
        }
    }
}
