package com.example.teamo_android;

import java.io.Serializable;

public class Team implements Serializable {
    private int id;
    private String title;
    private String content;
    private String leaderName;
    private int maxMemberNum;
    private String subject;
    private String semester;
    private String professor;
    private String courseClass;

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