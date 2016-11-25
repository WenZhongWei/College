package com.wen.college.auxiliary;

/**
 * Created by Administrator on 2016/8/20.
 */

public class Code {
    public static final String EXIT_LOGIN = "com.wen.college.EXIT_LOGIN";
    public static String PHONE_MATCH = "^1(3|4|5|7|8)\\d{9}$";
    public static String EMAIL_MATCH = "^\\w+@\\w+\\.(com|cn)(.cn)?$";
    public static String PWD_MATCH = "\\w{6,20}$";
    public static final int REQ_SELECT_PHOTO = 20;
    public static final int REQ_CUT_PHOTO = 21;
    public static final int REQ_UPDATE_SCHOOL = 22;
    public static final int RESP_UPDATE_SCHOOL = 23;
    public static final int RESP_SELECT_SCHOOL = 24;
    public static final int REQ_SELECT_SCHOOL = 25;
    public static final int REQ_SELECT_TAB = 26;
    public static final int RESP_SELECT_TAB = 27;
}
