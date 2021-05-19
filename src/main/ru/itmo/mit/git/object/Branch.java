package ru.itmo.mit.git.object;

import java.io.Serializable;

public class Branch implements Serializable {

    private final String name;
    private String commitID;

    public Branch(String name, String commitID) {
        this.name = name;
        this.commitID = commitID;
    }

    public String getName() {
        return name;
    }

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }
}
