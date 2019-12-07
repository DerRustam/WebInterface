/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author Richtofen
 */
public class SoftwareDTO {
    private int software_id;
    private String title;
    private String actual_price;
    
    public SoftwareDTO(int software_id, String title, String actual_price)
    {
        this.software_id = software_id;
        this.title = title;
        this.actual_price = actual_price;
    }
    
     public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getSoftware_id() {
        return software_id;
    }

    public void setSoftware_id(int software_id) {
        this.software_id = software_id;
    }
}
