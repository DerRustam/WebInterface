/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import control_pack.ServiceController;
import com.google.gson.Gson;
import control_pack.OperationResult;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author russa
 */
public class MainServlet extends HttpServlet {
    private List list_view;
    private Map<String, Runnable> viewmap = buildMapView();
    private Map<String, Runnable> execmap = buildMapExec();
    private OperationResult op_res = new OperationResult();
    private HashMap<String,String> hm_form;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private Map<String, Runnable> buildMapView(){
         Map<String, Runnable> _viewmap = new HashMap<>();
         _viewmap.put("Account", () -> list_view = ServiceController.getAccountList());
         _viewmap.put("Digital_Copy", () -> list_view = ServiceController.getDigitalCopyList());
         _viewmap.put("Order", () -> list_view = ServiceController.getOrderList());
         _viewmap.put("Product_Feedback", () -> list_view = ServiceController.getProductFeedbackList());
         _viewmap.put("Publisher", () -> list_view = ServiceController.getPubliserList());
         _viewmap.put("Purchase", () -> list_view = ServiceController.getPurchaseList());
         _viewmap.put("Software", () -> list_view = ServiceController.getSoftwareList());
         _viewmap.put("Software_Class", () -> list_view = ServiceController.getSoftwareClassList());
         _viewmap.put("Product List", () -> list_view = ServiceController.getSoftwarePriceList());
         return _viewmap;
    }
    
    private Map<String, Runnable> buildMapExec(){
        Map<String, Runnable> _execmap = new HashMap<>();
        _execmap.put("accountCreate", () -> op_res = ServiceController.accountCreate(hm_form));
        _execmap.put("accountUpdate", () -> op_res = ServiceController.accountUpdate(hm_form));
        _execmap.put("accountDelete", () -> op_res = ServiceController.accountDelete(hm_form));
        _execmap.put("digital CopyCreate", () -> op_res = ServiceController.digitalCopyCreate(hm_form));
        _execmap.put("digital CopyUpdate", () -> op_res = ServiceController.digitalCopyUpdate(hm_form));
        _execmap.put("digital CopyDelete", () -> op_res = ServiceController.digitalCopyDelete(hm_form));
        _execmap.put("orderCreate", () -> op_res = ServiceController.orderCreate(hm_form));
        _execmap.put("orderUpdate", () -> op_res = ServiceController.orderUpdate(hm_form));
        _execmap.put("orderDelete", () -> op_res = ServiceController.orderDelete(hm_form));
        _execmap.put("product FeedbackCreate", () -> op_res = ServiceController.productFeedbackCreate(hm_form));
        _execmap.put("product FeedbackUpdate", () -> op_res = ServiceController.productFeedbackUpdate(hm_form));
        _execmap.put("product FeedbackDelete", () -> op_res = ServiceController.productFeedbackDelete(hm_form));
        _execmap.put("publisherCreate", () -> op_res = ServiceController.publisherCreate(hm_form));
        _execmap.put("publisherUpdate", () -> op_res = ServiceController.publisherUpdate(hm_form));
        _execmap.put("publisherDelete", () -> op_res = ServiceController.publisherDelete(hm_form));
        _execmap.put("purchaseCreate", () -> op_res = ServiceController.purchaseCreate(hm_form));
        _execmap.put("purchaseUpdate", () -> op_res = ServiceController.purchaseUpdate(hm_form));
        _execmap.put("purchaseDelete", () -> op_res = ServiceController.purchaseDelete(hm_form));
        _execmap.put("softwareCreate", () -> op_res = ServiceController.softwareCreate(hm_form));
        _execmap.put("softwareUpdate", () -> op_res = ServiceController.softwareUpdate(hm_form));
        _execmap.put("softwareDelete", () -> op_res = ServiceController.softwareDelete(hm_form));
        _execmap.put("software ClassCreate", () -> op_res = ServiceController.softwareClassCreate(hm_form));
        _execmap.put("software ClassUpdate", () -> op_res = ServiceController.softwareClassUpdate(hm_form));
        _execmap.put("software ClassDelete", () -> op_res = ServiceController.softwareClassDelete(hm_form));
        return _execmap;
    }
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        String in = request.getParameter("tab");
        if (in != null) {
            viewmap.get(in).run();
        }
        if (list_view != null) {
            String json = new Gson().toJson(list_view);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            list_view = null;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        String tab = request.getParameter("tab");
        String op = request.getParameter("op");
        String s = request.getParameter("json");
        System.out.println(s);       
        if (tab != null && op != null) {
            Runnable func = execmap.get(tab+op);
            hm_form = gson.fromJson(request.getParameter("json"), HashMap.class);
            func.run();
            String json = new Gson().toJson(op_res);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            }  
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
