package com.example.teamo_android;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Team implements Serializable {
    private int id = 0;
    private String title = null;
    private String content = null;
    private String leaderName = null;
    private int maxMemberNum= 0;
    private String subject = null;
    private String semester = null;
    private String professor = null;
    private String courseClass = null;

    Team() {}

    Team(int id, String title, String leaderName, int memberNum, String subject, String semester, String professor, String courseClass) {
        this.id = id;
        this.title = title;
        this.leaderName = leaderName;
        this.maxMemberNum = memberNum;
        this.subject = subject;
        this.semester = semester;
        this.professor = professor;
        this.courseClass = courseClass;
        this.content = makeContent(this.subject, this.semester, this.professor, this.courseClass);
    }

    private String makeContent(String subject, String semester, String professor, String courseClass) {
        String content = ("#" + subject + " #" + semester + "학기" + "\n#" + professor + "교수님 " +
                "#" + courseClass + "분반");
        return content;
    }

    public int getTeamId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getLeaderName() { return this.leaderName; }
    public int getMaxNumber() { return this.maxMemberNum; }
    public String getSubject() { return this.subject; }
    public String getSemester() { return this.semester; }
    public String getProfessor() { return this.professor; }
    public String getCourseClass() { return this.courseClass; }
    public String getContent() { return this.content; }

    public void setTitle(String title) { this.title = title; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setSemester(String semester) { this.semester = semester; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setCourseClass(String num) { this.courseClass = num; }

    public void setContent(String subject, String semester, String professor, String courseClass) {
        makeContent(subject, semester, professor, courseClass);
    }

}