package com.example.teamo_android;

import java.io.Serializable;

public class Team implements Serializable {
    private String id = null;
    private String title = null;
    private String tag = null;
    private int maxMemberNum= 0;
    private String subject = null;
    private String semester = null;
    private String professor = null;
    private String courseClass = null;
    private String date = null;
    private String content = null;
    private String writerId = null;

    Team() {}

    Team(String id, String title, int memberNum, String subject, String semester, String professor, String courseClass, String date, String content,
         String writerId) {
        this.id = id;
        this.title = title;
        this.maxMemberNum = memberNum;
        this.subject = subject;
        this.semester = semester;
        this.professor = professor;
        this.courseClass = courseClass;
        this.tag = makeContent();
        this.content = content;
        this.date = date;
        this.writerId = writerId;
    }

    public String makeContent() {
        String tags = ("#" + this.subject + " #" + this.semester + "학기" + "\n#" + this.professor + "교수님 " +
                "#" + this.courseClass + "분반");
        return tags;
    }

    public String getTeamId() { return this.id; }
    public String getTitle() { return this.title; }
    public int getMaxNumber() { return this.maxMemberNum; }
    public String getSubject() { return this.subject; }
    public String getSemester() { return this.semester; }
    public String getProfessor() { return this.professor; }
    public String getCourseClass() { return this.courseClass; }
    public String getTag() { return this.tag; }
    public int getMaxMemberNum() {
        return maxMemberNum;
    }
    public String getDate() {
        return date;
    }
    public String getContent() {
        return content;
    }
    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTitle(String title) { this.title = title; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setSemester(String semester) { this.semester = semester; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setCourseClass(String num) { this.courseClass = num; }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setMaxMemberNum(int maxMemberNum) {
        this.maxMemberNum = maxMemberNum;
    }

}