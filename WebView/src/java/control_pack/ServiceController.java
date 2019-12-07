package control_pack;

import EntityPack.*;
import DAOPack.*;
import DTO.*;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;

public abstract class ServiceController {

    private final static Properties prop = initProperties();
    private final static AccountDAO accountDAO = new AccountDAO();
    private final static Digital_CopyDAO digital_copyDAO = new Digital_CopyDAO();
    private final static OrderDAO orderDAO = new OrderDAO();
    private final static Product_FeedbackDAO product_feedbackDAO = new Product_FeedbackDAO();
    private final static PublisherDAO publisherDAO = new PublisherDAO();
    private final static PurchaseDAO purchaseDAO = new PurchaseDAO();
    private final static SoftwareDAO softwareDAO = new SoftwareDAO();
    private final static Software_ClassDAO software_classDAO = new Software_ClassDAO();
    private final static String ncr_msg = "Record not created.";
    private final static String nup_msg = "Record not updated.";
    private final static String ndl_msg = "Record not deleted.";
    private final static String ok_msg = "OK.";

    private static Properties initProperties() {
        boolean isRead = false;
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream("F:/Web/WebView/data/connection.properties")) {
            p.load(fis);
            if (!p.containsKey("username") || !p.containsKey("password") || !p.containsKey("url")) {
                throw new IOException();
            }
            isRead = true;
        } catch (IOException ioe) {
            p.setProperty("url", String.format("jdbc:postgresql://%s:%s/%s", "localhost", 5432, "software_store"));
            p.setProperty("username", "russa");
            p.setProperty("password", "StReLock97");
        }

        if (!isRead) {
            File dir = new File("F:/Web/WebView/data");
            File f = new File(dir, "connection.properties");
            try (FileOutputStream fos = new FileOutputStream("F:/Web/WebView/data/connection.properties")) {
                if (!f.isFile()) {
                    f.createNewFile();
                }
                p.store(fos, "Created default");
            } catch (IOException ex) {
                isRead = false;
            }
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return p;
    }

    public static ArrayList<SoftwareDTO> getSoftwarePriceList() {
        try {
            return softwareDAO.getList();
        } catch (SQLException ex) {
            return null;
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public static ArrayList<Account> getAccountList() {
        ArrayList<Account> alist = new ArrayList<>();
        try {
           alist = accountDAO.getAll();
        } catch (SQLException | NullPointerException ex) {
        }
        return alist;
    }

    public static ArrayList<Digital_Copy> getDigitalCopyList() {
        ArrayList<Digital_Copy> alist = new ArrayList<>();
        try {
            alist = digital_copyDAO.getAll();
        } catch (SQLException | NullPointerException ex) {
            String t = ex.getMessage();
        }
        return alist;
    }

    public static ArrayList<Order> getOrderList() {
        ArrayList<Order> alist = new ArrayList<>();
        try {
            alist = orderDAO.getAll();
        } catch (SQLException | NullPointerException ex) {
        }
        return alist;
    }

    public static ArrayList<Product_Feedback> getProductFeedbackList() {
        ArrayList<Product_Feedback> alist = new ArrayList<>();
        try {
            alist = product_feedbackDAO.getAll();
        } catch (SQLException | NullPointerException ex) {
        }
        return alist;
    }

    public static ArrayList<Publisher> getPubliserList() {
        ArrayList<Publisher> alist = new ArrayList<>();
        try {
            alist = publisherDAO.getAll();
        } catch (SQLException | NullPointerException ex) {
        }
        return alist;
    }

    public static ArrayList<Purchase> getPurchaseList() {
        ArrayList<Purchase> alist = new ArrayList<>();
        try {
            alist = purchaseDAO.getAll();
        } catch (SQLException | NullPointerException ex) {
        }
        return alist;
    }

    public static ArrayList<Software> getSoftwareList() {
        ArrayList<Software> alist = new ArrayList<>();
        try {
            alist = softwareDAO.getAll();
        } catch (SQLException | NullPointerException ex) {
        }
        return alist;
    }

    public static ArrayList<Software_Class> getSoftwareClassList() {
        ArrayList<Software_Class> alist = new ArrayList<>();
        try {
            alist = software_classDAO.getAll();
        } catch (SQLException | NullPointerException ex) {
        }
        return alist;
    }

    public static OperationResult accountCreate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            accountDAO.insert(hm.get("e_mail"), hm.get("password"), hm.get("owner_name"), hm.get("gender"), hm.get("date_of_birth"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ncr_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult accountUpdate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            accountDAO.update(hm.get("e_mail_old"), hm.get("e_mail_new"),
                    hm.get("password"), hm.get("owner_name"), hm.get("gender"),
                    Date.valueOf(hm.get("date_of_birth")));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(nup_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult accountDelete(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            accountDAO.delete(hm.get("e_mail"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ndl_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }
    
    public static OperationResult softwareClassCreate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int parent_id = software_classDAO.getIdByKey(hm.get("class_parent_name"));
            software_classDAO.insert(hm.get("class_name"),parent_id);
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ncr_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult softwareClassUpdate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int parent_id = software_classDAO.getIdByKey(hm.get("class_parent_name_new"));
            software_classDAO.update(hm.get("class_name_old"), hm.get("class_name_new"), parent_id);
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(nup_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult softwareClassDelete(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            software_classDAO.delete(hm.get("class_name"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ndl_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult publisherCreate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            publisherDAO.insert(hm.get("publisher_name"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ncr_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult publisherUpdate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            publisherDAO.update(hm.get("publisher_name_old"),hm.get("publisher_name_new"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(nup_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult publisherDelete(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            publisherDAO.delete(hm.get("publisher_name"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ndl_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }
    
    public static OperationResult softwareCreate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
           int class_id = software_classDAO.getIdByKey(hm.get("class_name"));
           int publisher_id = 0;
           String p_name = hm.get("publisher_name");
           if (!p_name.equals("")){
              publisher_id = publisherDAO.getIdByKey(p_name);
           }
           String rel_date_s = hm.get("release_date");
           Date release_date = null;
           if (!rel_date_s.equals("")){
               release_date = Date.valueOf(hm.get("release_date"));
           }
           softwareDAO.insert(class_id, publisher_id, hm.get("title"),
                   release_date , hm.get("esrb"), Integer.valueOf(hm.get("actual_price")));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ncr_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult softwareUpdate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
           int class_id = software_classDAO.getIdByKey(hm.get("class_name_new"));
           int publisher_id = 0;
           String p_name = hm.get("publisher_name");
           if (!p_name.equals("")){
              publisher_id = publisherDAO.getIdByKey(p_name);
           }
           String rel_date_s = hm.get("release_date");
           Date release_date = null;
           if (!rel_date_s.equals("")){
               release_date = Date.valueOf(hm.get("release_date"));
           }
            softwareDAO.update(hm.get("title_old"),hm.get("class_name_old"),hm.get("class_name_new"),
                    class_id, publisher_id,hm.get("title_new"), release_date,
                    hm.get("esrb"), Integer.valueOf(hm.get("actual_price")));
            
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(nup_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult softwareDelete(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
           softwareDAO.delete(hm.get("title"), hm.get("class_name"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ndl_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }
    
    public static OperationResult orderCreate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
           int account_id = accountDAO.getIdByKey(hm.get("e_mail"));
           orderDAO.insert(account_id, Timestamp.valueOf(hm.get("order_datetime")),Integer.valueOf(hm.get("summary_price")), hm.get("pay_code"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ncr_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult orderUpdate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int account_id = accountDAO.getIdByKey(hm.get("e_mail"));
            orderDAO.update(hm.get("pay_code_old"), account_id,
                    Timestamp.valueOf(hm.get("order_datetime")),
                    Integer.valueOf(hm.get("summary_price")),
                    hm.get("pay_code_new"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(nup_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult orderDelete(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
           orderDAO.delete(hm.get("pay_code"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ndl_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }
    
    public static OperationResult purchaseCreate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int order_id = orderDAO.getIdByKey(hm.get("pay_code"));
            int software_id = softwareDAO.getIdByKey(hm.get("title"), hm.get("class_name"));
            purchaseDAO.insert(hm.get("pay_code"), hm.get("title"), hm.get("class_name"),
                    order_id, software_id,Short.valueOf(hm.get("count")),
                    Integer.valueOf(hm.get("price_single")));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ncr_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult purchaseUpdate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int order_id = orderDAO.getIdByKey(hm.get("pay_code_new"));
            int software_id = softwareDAO.getIdByKey(hm.get("title_new"), hm.get("class_name_new"));
            purchaseDAO.update(hm.get("pay_code_old"), hm.get("title_old"),
                    hm.get("class_name_old"), hm.get("pay_code_new"), hm.get("title_new"),
                    hm.get("class_name_new"),order_id, software_id, Short.valueOf(hm.get("count")),
                    Integer.valueOf(hm.get("price_single")) );
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(nup_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult purchaseDelete(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            purchaseDAO.delete(hm.get("pay_code"), hm.get("title"), hm.get("class_name"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ndl_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }
    
    public static OperationResult digitalCopyCreate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int purchase_id = purchaseDAO.getIdByKey(hm.get("pay_code"),hm.get("title"), hm.get("class_name"));
            int software_id = softwareDAO.getIdByKey(hm.get("title"), hm.get("class_name"));
            digital_copyDAO.insert(hm.get("title"), hm.get("class_name"), software_id, purchase_id, hm.get("product_key"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ncr_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult digitalCopyUpdate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int purchase_id = purchaseDAO.getIdByKey(hm.get("pay_code_new"),hm.get("title_new"), hm.get("class_name_new"));
            int software_id = softwareDAO.getIdByKey(hm.get("title"), hm.get("class_name"));
            digital_copyDAO.update(hm.get("title_old"), hm.get("class_name_old"),
                hm.get("product_key_old"), hm.get("title_new"), hm.get("class_name_new"),
                hm.get("product_key_new"), software_id, purchase_id);
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(nup_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult digitalCopyDelete(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            digital_copyDAO.delete(hm.get("title"), hm.get("class_name"), hm.get("product_key"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ndl_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult productFeedbackCreate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int account_id = accountDAO.getIdByKey(hm.get("e_mail"));
            int software_id = softwareDAO.getIdByKey(hm.get("title"), hm.get("class_name"));
            product_feedbackDAO.insert(hm.get("e_mail"), hm.get("title"), hm.get("class_name"), account_id,software_id, hm.get("message"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ncr_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult productFeedbackUpdate(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            int account_id = accountDAO.getIdByKey(hm.get("e_mail_new"));
            int software_id = softwareDAO.getIdByKey(hm.get("title_new"), hm.get("class_name_new"));
            product_feedbackDAO.update(hm.get("e_mail_old"), hm.get("title_old"),
                    hm.get("class_name_old"),hm.get("e_mail_new"),hm.get("title_new"),
                    hm.get("class_name_new"), account_id, software_id, hm.get("message"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(nup_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }

    public static OperationResult productFeedbackDelete(HashMap<String, String> hm) {
        OperationResult op_res = new OperationResult();
        try {
            product_feedbackDAO.delete(hm.get("e_mail"), hm.get("title"), hm.get("class_name"));
            op_res.setStatus(ok_msg);
        } catch (SQLException ex) {
            op_res.setStatus(ndl_msg);
            op_res.setMessage(ex.getMessage());
        }
        return op_res;
    }    
    
    public static final Connection getConnectionFromPool() throws SQLException {
        return DriverManager.getConnection(prop.getProperty("url"), prop);
    }

    public static String getQueryFromFile(String file) throws IOException, NullPointerException {
        Path target = Paths.get("F:/Web/WebView/src/java/SQLScripts/" + file);
        return new String(Files.readAllBytes(target));
    }
}
