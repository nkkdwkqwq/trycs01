package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private String formattedTime;

    /** The first string is name, the second is hashValue */
    private TreeMap<String, String> treeMap = new TreeMap<>();
    /* TODO: fill in the rest of this class. */

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

    public void gitCommit(String messages, String parent, File dir) {
        message = messages;
        parentID = parent;

        /** Format date */
        long timestampMillis = System.currentTimeMillis();
        Date date0 = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        formattedTime = sdf.format(date0);

        File f0 = join(Repository.COMMIT_DIR, parent);
        treeMap = readObject(f0, Commit.class).getMap   ();

        /* handle the file in the add stage */
        /** some problems have not finish */
        List<String> addList = Utils.plainFilenamesIn(dir);
        if(addList != null) {
            for (String s : addList) {
                File f = new File(Repository.ADD_STAGE_DIR, s);
                byte[] b = readContents(f);
                treeMap.put(s, sha1(b));
                f.delete();
                /**
                restrictedDelete(f);
                 */
            }
        }

        /* handle the file in the remove stage */
        List<String> removeList = plainFilenamesIn(Repository.REMOVE_STAGE_DIR);
        if(removeList != null) {
            for (String s : removeList) {
                File f = new File(Repository.REMOVE_STAGE_DIR, s);
                treeMap.remove(s);
                restrictedDelete(f);
            }
        }
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
