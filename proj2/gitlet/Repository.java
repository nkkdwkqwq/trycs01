package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The branch in git. */
    public static String branch;
    /** The head in git */
    public static String head;
    /** The stage area in git */
    public static File STAGE_DIR = join(GITLET_DIR,"stage");
    /** The stage for add */
    public static File ADD_STAGE_DIR = join(STAGE_DIR, "addArea");
    /** The stage for remove */
    public static File REMOVE_STAGE_DIR = join(STAGE_DIR, "removeArea");
    /** The area for commit */
    public static File COMMIT_DIR = join(GITLET_DIR, "commit");
    /** The area store blob */
    public static File BLOB_DIR = join(GITLET_DIR, "blob");
    /** Array store the branch */
    public static LinkedHashMap<String, String> branches = new LinkedHashMap<>();
    /* TODO: fill in the rest of this class. */

    public static void setUpRepository() {

        /** Create essential dir */
        if(!GITLET_DIR.mkdir()) {
            System.out.println("A Gitlet version-control system already exists in the current directory");
            return;
        }
        STAGE_DIR.mkdir();
        ADD_STAGE_DIR.mkdir();
        REMOVE_STAGE_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();

        /** initial commit */
        Commit com = new Commit();
        com.initialSetUp();
        byte[] b = serialize(com);
        branches.put("master", sha1(b));
        File f = join(COMMIT_DIR, sha1(b));
        head = sha1(b);
        writeContents(f, b);
    }

    /** DO NOT solve the problem that If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added,
     * and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it’s original version).
     * The file will no longer be staged for removal (see gitlet rm),
     * if it was at the time of the command.
     * @param name
     */
    public static void addFileToRepository(String name) {
        File readFile = new File(name);
        if(!readFile.exists()) {
            System.out.println("File does not exist");
            return;
        }
        byte[] b = readContents(readFile);
        File f = join(STAGE_DIR, name);
        File addFile = join(BLOB_DIR, sha1(b));
        writeContents(f, b);
        writeContents(addFile,b);
    }

    public static void commitToRepository(String message) {
        Commit com = new Commit();
        List<String> l = plainFilenamesIn(ADD_STAGE_DIR);
        com.gitCommit(message, head, ADD_STAGE_DIR);
        byte[] b = serialize(com);
        head = sha1(b);
        File f = join(COMMIT_DIR, head);
        branch = head;
        writeContents(f, b);
    }
}
