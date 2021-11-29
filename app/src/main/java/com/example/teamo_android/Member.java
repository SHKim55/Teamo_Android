package com.example.teamo_android;

import java.util.ArrayList;

public class Member {
    private int teamId;
    private ArrayList<Member> members;
    private boolean state;

    Member(int teamId, ArrayList<Member> members, Boolean state) {
        this.teamId = teamId;
        this.members = members;
        this.state = state;
    }

    public int getTeamId() { return this.teamId; }
    public ArrayList<Member> getAllMembers() { return this.members; }
    public Member getMember(int index) { return this.members.get(index); }
    public Boolean isCompleted() { return this.state; }

    public void addMember(Member member) { members.add(member); }
    public void deleteMember(int index) { members.remove(index); }
}
