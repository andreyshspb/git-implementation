package ru.itmo.mit.git.object;
import ru.itmo.mit.git.util.HashHelper;

import java.io.Serializable;
import java.util.Date;

public class Commit implements Serializable {

    private final String parentID;
    private final String contentID;
    private final String commitMessage;
    private final long time;

    public Commit(String parentID, String contentID,
                  String commitMessage, long time) {
        this.parentID = parentID;
        this.contentID = contentID;
        this.commitMessage = commitMessage;
        this.time = time;
    }

    public String getParentID() {
        return parentID;
    }

    public String getContentID() {
        return contentID;
    }

    public String getHash() {
        return HashHelper.getHashFromString(parentID + contentID + commitMessage);
    }

    public String info() {
        return "\n" + "commit " + getHash() + "\n" +
                "Data: " + new Date(time) + "\n" +
                "\n\t" + commitMessage;
    }

}
