package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */

/** I add serializable to this class */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String parentID;
    private long date;

    /** The first string is name, the second is hashValue */
    private TreeMap<String, String> treeMap;
    /* TODO: fill in the rest of this class. */

    /** The zero commit when init the repository */
    public void initialSetUp() {
        message = "initial commit";
        date = 0L;
        parentID = null;
        treeMap = null;
    }

    public void gitCommit(String messages, String parent, File dir) {
        message = messages;
        parentID = parent;
        date = System.currentTimeMillis();
        List<String> l = Utils.plainFilenamesIn(dir);
        if(l != null) {
            for (String s : l) {
                File f = new File(s);
                byte[] b = readContents(f);
                treeMap.put(s, sha1(b));
                restrictedDelete(f);
            }
        }
    }

    public String getParentID() {
        return parentID;
    }

    public long getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public TreeMap<String, String> getMap() {
        return treeMap;
    }
}
