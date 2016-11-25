package com.wen.college.entities;



/**
 * AppVersion entity. @author MyEclipse Persistence Tools
 */

public class AppVersion  implements java.io.Serializable {


    // Fields    

     private Integer verId;
     private Integer verCode;
     private String verName;
     private String verUrl;


    // Constructors

    /** default constructor */
    public AppVersion() {
    }

    
    /** full constructor */
    public AppVersion(Integer verCode, String verName, String verUrl) {
        this.verCode = verCode;
        this.verName = verName;
        this.verUrl = verUrl;
    }

   
    // Property accessors

    public Integer getVerId() {
        return this.verId;
    }
    
    public void setVerId(Integer verId) {
        this.verId = verId;
    }

    public Integer getVerCode() {
        return this.verCode;
    }
    
    public void setVerCode(Integer verCode) {
        this.verCode = verCode;
    }

    public String getVerName() {
        return this.verName;
    }
    
    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getVerUrl() {
        return this.verUrl;
    }
    
    public void setVerUrl(String verUrl) {
        this.verUrl = verUrl;
    }
   








}