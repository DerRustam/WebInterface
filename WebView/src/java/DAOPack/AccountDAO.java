package DAOPack;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import EntityPack.Account;
import control_pack.ServiceController;

public class AccountDAO extends DAO<Account>{
    private String sql_getall;
    private String sql_insert;
    private String sql_update;
    private String sql_delete;
    private String sql_selectByKey;

    private String err_msg;
    public AccountDAO() {
        try{
            sql_selectByKey = ServiceController.getQueryFromFile("Account_selectByKey.sql");
            sql_getall = ServiceController.getQueryFromFile("Account_getAll.sql");
            sql_insert = ServiceController.getQueryFromFile("Account_insert.sql");
            sql_update = ServiceController.getQueryFromFile("Account_update.sql");
            sql_delete = ServiceController.getQueryFromFile("Account_delete.sql");
            err_msg = "Account with e_mail: %s ";
        }
        catch (IOException ex) {System.out.println("<Account> scripts not loaded");}
    }
    
    private SQLException notFoundException(String e_mail){
        return new SQLException(String.format(err_msg+ "not found.", e_mail));
    }
    
    private SQLException alreadyException(String e_mail){
        return new SQLException(String.format(err_msg+ "already in database.", e_mail));
    }
  
    public int getIdByKey(String e_mail) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
            ps.setString(1,e_mail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt(1);
            }
            throw notFoundException(e_mail);
        }
    }
    
    private int getId(String e_mail, Connection connection) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(sql_selectByKey);
        ps.setString(1, e_mail);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException();
    }
    @Override
    public Account convertToEntity(ResultSet rs) throws SQLException{
        Account account = new Account(
                rs.getString(2).trim(),
                rs.getDate(3).toString(),
                rs.getString(4).charAt(0),
                rs.getString(5).trim(),
                rs.getString(6).trim()
                );
        return account;
    }
    
    public void insert(String _email, String _password, String _name, String _gender, String _birth) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            PreparedStatement ps = connection.prepareStatement(sql_insert);
            ps.setString(1, _name);
            ps.setDate(2,Date.valueOf(_birth));
            ps.setString(3, _gender);
            ps.setString(4,_email);
            ps.setString(5,_password);
            ps.executeUpdate();
        }
        catch (SQLException ex){ 
            throw alreadyException(_email);
        }
    }
    @Override
    public ArrayList<Account> getAll() throws SQLException {
        ArrayList<Account> accounts = new ArrayList<>();
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql_getall);
            Account a;
            while (rs.next()) {
                a = convertToEntity(rs);
                accounts.add(a);
            }
        }
        return accounts;
    }

    public void update(String key_email, String _email, String _password,
            String _name, String _gender, Date _birth) throws SQLException {
        boolean isFound = false;
        try (Connection connection = ServiceController.getConnectionFromPool()){
            int id = getId(key_email, connection);
            isFound = true;
            PreparedStatement ps = connection.prepareStatement(sql_update);
            ps.setString(1,_name);
            ps.setDate(2,_birth);
            ps.setString(3,_gender);
            ps.setString(4,_email);
            ps.setString(5,_password);
            ps.setInt(6,id);
            ps.executeUpdate();
        }
        catch (SQLException ex) {
            if (isFound){
                throw alreadyException(_email);
            }
            throw notFoundException(key_email);
        }
    }

    public final void delete(String key_e_mail) throws SQLException {
        try (Connection connection = ServiceController.getConnectionFromPool()) {
            int id = getId(key_e_mail, connection);
            PreparedStatement ps = connection.prepareStatement(sql_delete);
            ps.setInt(1,id);
            ps.executeUpdate();
        }
        catch(SQLException ex){
            throw notFoundException(key_e_mail);
        }
    }
}
