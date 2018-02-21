package com.app.efficiclean.classes;

import java.util.ArrayList;

public class Team {

    private ArrayList<String> members;

    public Team() {
        members = new ArrayList<String>();
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void addMember(String member) {
        members.add(member);
    }

    public String removeMember(int i) {
        String thisMember = "";
        if (i < members.size() && i >= 0) {
            thisMember = members.remove(i);
        }
        return thisMember;
    }

    public String getMember(int i) {
        String thisMember = "";
        if (i < members.size() && i >= 0) {
            thisMember = members.get(i);
        }
        return thisMember;
    }
}
