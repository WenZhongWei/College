package com.wen.college.entities;
// default package



/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User  implements java.io.Serializable {


    // Fields    

     private Integer uid;
     private String phone;
     private String pwd;
     private String email;
     private String nick;
     private Integer roleId;
     private String school;
     private String area;
     private String department;
     private String gradeClass;
     private String year;
     private String photoUrl;
     private String name;
     private Integer gender;
     private String token;


    // Constructors

    /** default constructor */
    public User() {
    }

	/** minimal constructor */
    public User(Integer roleId, Integer gender, String token) {
        this.roleId = roleId;
        this.gender = gender;
        this.token = token;
    }
    
    /** full constructor */
    public User(String phone, String pwd, String email, String nick, Integer roleId, String school, String area, String department, String gradeClass, String year, String photoUrl, String name, Integer gender, String token) {
        this.phone = phone;
        this.pwd = pwd;
        this.email = email;
        this.nick = nick;
        this.roleId = roleId;
        this.school = school;
        this.area = area;
        this.department = department;
        this.gradeClass = gradeClass;
        this.year = year;
        this.photoUrl = photoUrl;
        this.name = name;
        this.gender = gender;
        this.token = token;
    }

   
    // Property accessors

    public Integer getUid() {
        return this.uid;
    }
    
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return this.pwd;
    }
    
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return this.nick;
    }
    
    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getSchool() {
        return this.school;
    }
    
    public void setSchool(String school) {
        this.school = school;
    }

    public String getArea() {
        return this.area;
    }
    
    public void setArea(String area) {
        this.area = area;
    }

    public String getDepartment() {
        return this.department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGradeClass() {
        return this.gradeClass;
    }
    
    public void setGradeClass(String gradeClass) {
        this.gradeClass = gradeClass;
    }

    public String getYear() {
        return this.year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return this.gender;
    }
    
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
   








}