package com.example.teamo_android;

public class User {
    private int id;
    private String userId;
    private String password;
    private String email;
    private String name;
    private String department;
    private int studentNum;

    User(int id, String userId, String password, String email, String name, String department, int studentNum) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.department = department;
        this.studentNum = studentNum;
    }

    public int getId() { return this.id; }
    public String getUserId() { return this.userId; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
    public String getUserName() { return this.name; }
    public String getDepartment() { return this.department; }
    public int getStudentNum() { return this.studentNum; }

    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setStudentNum(int studentNum) { this.studentNum = studentNum; }
}
