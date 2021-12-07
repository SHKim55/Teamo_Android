package com.example.teamo_android;

public class Member {
    private String teamId, name, department, stdNum, state, memberId;

    public Member() {
    }

    public Member(String teamId, String name, String department, String stdNum, String state, String memberId) {
        this.teamId = teamId;
        this.name = name;
        this.department = department;
        this.stdNum = stdNum;
        this.state = state;
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStdNum() {
        return stdNum;
    }

    public void setStdNum(String stdNum) {
        this.stdNum = stdNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
