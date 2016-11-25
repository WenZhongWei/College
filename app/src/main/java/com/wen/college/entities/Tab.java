package com.wen.college.entities;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Tab entity. @author MyEclipse Persistence Tools
 */
@Table(name = "tab")
public class Tab implements java.io.Serializable {


    // Fields    
    @Id(column = "tid")
    @NoAutoIncrement
    private Integer tid;
    @Column(column = "tab")
    private String tab;
    @Column(column = "ismy")
    private int ismy;//0是，1不是


    // Constructors

    /**
     * default constructor
     */
    public Tab() {
    }


    /**
     * full constructor
     */
    public Tab(String tab) {
        this.tab = tab;
    }


    // Property accessors

    public Integer getTid() {
        return this.tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getTab() {
        return this.tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public int getIsmy() {
        return ismy;
    }

    public void setIsmy(int ismy) {
        this.ismy = ismy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Tab) {
            if (this.tid == ((Tab) o).getTid() && this.tab.equals(((Tab) o).getTab())) {
                return true;
            }
        }
        return false;
    }
}