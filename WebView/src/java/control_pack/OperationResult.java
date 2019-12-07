/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control_pack;

public class OperationResult {
    private String status;
    private String message;
    
    public OperationResult(){
        status = "";
        message = "";
    }
    
    public String getStatus(){
        return status;
    }
    
    public String getMessage(){
        return message;
    }
    
    public void setStatus(String _status){
        status = _status;
    }
    
    public void setMessage(String _message){
        message = _message;
    }
}
