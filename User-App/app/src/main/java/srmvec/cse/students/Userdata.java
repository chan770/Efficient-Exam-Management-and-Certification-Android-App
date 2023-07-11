package srmvec.cse.students;

public class Userdata {
    public static String regnum;
    public static String name;
    public static String dob;
    public static String lang;
    public static String filename;
    public static Long et;
    public static String marks;
    public Userdata(){
    }

    public static void setregnum(String regnum) {
        Userdata.regnum = regnum;
    }
    public static void setname(String name) {
        Userdata.name = name;
    }
    public static void setdob(String dob) {
        Userdata.dob = dob;
    }
    public static void setlang(String lang) {
        Userdata.lang = lang;
    }
    public static void setet(Long et) {
        Userdata.et = et;
    }
    public static void setsfilename(String filename) {
        Userdata.filename = filename;
    }
    public static void setsmarks(String marks) {
        Userdata.marks = marks;
    }


}
