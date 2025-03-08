package gitlet;


import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author listu
 */

/** I add serializable to this class */
public class Commit implements Serializable {
    /**
     * message
     * parentID
     * formattedTime
     * secondParent
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String parentID;
    private String formattedTime;
    private String secondParent = null;

    /** The first string is name, the second is hashValue */
    private TreeMap<String, String> treeMap = new TreeMap<>();

    /** The zero commit when init the repository */
    public void initialSetUp() {
        message = "initial commit";
        long timestampMillis = 0L;
        Date date0 = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        formattedTime = sdf.format(date0);
        parentID = null;
    }

    public boolean gitCommit(String messages, String parent) {
        List<String> addList = plainFilenamesIn(Repository.ADD_STAGE_DIR);
        List<String> removeList = plainFilenamesIn(Repository.REMOVE_STAGE_DIR);

        // addList and removeList must exist
        if (addList.isEmpty() && removeList.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return false;
        }

        if (messages.trim().isEmpty()) {
            System.out.println("Please enter a commit message");
            return false;
        }
        message = messages;
        parentID = parent;

        /** Format date */
        long timestampMillis = System.currentTimeMillis();
        Date date0 = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        formattedTime = sdf.format(date0);

        File f0 = join(Repository.COMMIT_DIR, parent);
        treeMap = readObject(f0, Commit.class).getMap();

        if (addList != null) {
            for (String s : addList) {
                File f = new File(Repository.ADD_STAGE_DIR, s);
                byte[] b = readContents(f);
                treeMap.put(s, sha1(b));
                f.delete();
            }
        }

        if (removeList != null) {
            for (String s : removeList) {
                File f = new File(Repository.REMOVE_STAGE_DIR, s);
                treeMap.remove(s);
                f.delete();
            }
        }
        return true;
    }
    public void setSecondParent(String id) {
        secondParent = id;
    }
    public String getSecondParent() {
        return secondParent;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public String getParentID() {
        return parentID;
    }

    public String getMessage() {
        return message;
    }

    public TreeMap<String, String> getMap() {
        return treeMap;
    }
}
