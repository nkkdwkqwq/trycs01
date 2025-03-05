package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
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
    /** The name of branch in git. */
    public String branchName;
    /** The hash of branch in git */
    public String branchHash;
    /** The head in git */
    public String head = "hello";

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
    public TreeMap<String, String> branches = new TreeMap<>();
    /** TreeMap for added file to stage area */
    public TreeMap<String, String> stageForAdd = new TreeMap<>();
    /** TreeMap for removed file to stage area*/
    public TreeMap<String, String> stageForRemove = new TreeMap<>();
    /* TODO: fill in the rest of this class. */


    public void setUpRepository() {

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
        branchHash = sha1(b);
        branchName = "master";
        branches.put(branchName, branchHash);
        File f = join(COMMIT_DIR, sha1(b));
        head = sha1(b);
        writeContents(f, b);
    }

    public void saveRepository() {
        File f = join(GITLET_DIR, "repository");
        byte[] b = serialize(this);
        writeContents(f, b);
    }

    public static Repository readRepository() {
        File f0 = join(GITLET_DIR, "repository");
        return readObject(f0, Repository.class);
    }

    /** DO NOT solve the problem that If the current working version of the file is identical to the version in the current commit,
     * do not stage it to be added,
     * and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it’s original version).
     * The file will no longer be staged for removal (see gitlet rm),
     * if it was at the time of the command.
     * @param name
     */
    public void addFileToRepository(String name) {
        File readFile = join(Repository.CWD, name);
        if (!readFile.exists()) {
            System.out.println("File does not exist");
            return;
        }

        byte[] b = readContents(readFile);

        File commitClass = join(Repository.COMMIT_DIR, head);
        Commit com = readObject(commitClass, Commit.class);
        TreeMap<String, String> tm = com.getMap();
        if(tm != null) {
            if (Objects.equals(tm.get(name), sha1(b))) {
                File f0 = join(Repository.ADD_STAGE_DIR, name);
                if (f0.exists()) {
                    f0.delete();
                    return;
                }
            }
        }
        File f2 = join (BLOB_DIR, sha1(b));
        File f1 = join(ADD_STAGE_DIR, name);
        writeContents(f1, b);
        writeContents(f2, b);

    }

    public void commitToRepository(String message) {
        Commit com = new Commit();
        List<String> l = plainFilenamesIn(ADD_STAGE_DIR);
        com.gitCommit(message, head, ADD_STAGE_DIR);
        byte[] b = serialize(com);
        head = sha1(b);
        File f = join(COMMIT_DIR, head);
        branchHash = sha1(b);
        branches.put(branchName, branchHash);
        writeContents(f, b);
    }

    /** did not solve the two parents case */
    public void logTheCommit() {
        String current = head;

        while (current != null) {
            String commitOfHash = current;
            File f = new File(Repository.COMMIT_DIR, current);
            Commit com = readObject(f, Commit.class);
            String parentID = com.getParentID();
            String date = com.getFormattedTime();
            String commitMessage = com.getMessage();

            System.out.println("===");
            System.out.println("commit " + commitOfHash);
            System.out.println("Date: " + date);
            System.out.println(commitMessage);
            System.out.println();

            current = parentID;
        }


    }

    /** rm */
    public void rmFileToRepository(String name) {
        File workFile = new File(CWD, name);
        File addtionFile = new File(Repository.ADD_STAGE_DIR, name);
        if(addtionFile.exists()) {
            restrictedDelete(addtionFile);
            return;
        }
        File commitClass = join(Repository.COMMIT_DIR, head);
        Commit com = readObject(commitClass, Commit.class);
        TreeMap<String, String> tm = com.getMap();
        if(tm.get(name) != null) {
            File removeFile = join(Repository.REMOVE_STAGE_DIR, name);
            File blobFile = join(Repository.BLOB_DIR, tm.get(name));
            byte[] b = readContents(blobFile);
            writeContents(removeFile, b);
            if(workFile.exists()) {
                restrictedDelete(workFile);
            }
            return;
        }
        System.out.println("No reason to remove the file");
    }

    public void globalLogTheCommit() {
        List<String> l = plainFilenamesIn(Repository.COMMIT_DIR);
        for(String current : l) {
            File f = new File(Repository.COMMIT_DIR, current);
            Commit com = readObject(f, Commit.class);
            String date = com.getFormattedTime();
            String commitMessage = com.getMessage();

            System.out.println("===");
            System.out.println("commit " + current);
            System.out.println("Date: " + date);
            System.out.println(commitMessage);
            System.out.println();
        }
    }

    public void findCommitMessageId(String message) {
        Boolean existMessage = false;
        List<String> l = plainFilenamesIn(Repository.COMMIT_DIR);
        for(String current : l) {
            File f = new File(Repository.COMMIT_DIR, current);
            Commit com = readObject(f, Commit.class);
            if(message.equals(com.getMessage())) {
                System.out.println(current);
                existMessage= true;
            }
        }
        if(!existMessage) {
            System.out.println("Found no commit with that message");
        }
    }

    public void gitState() {
        ArrayList<String> l = new ArrayList<>();
        for (String s : branches.keySet()) {
            if (s.equals("master")) {
                continue;
            }
            l.add(s);
        }
        if (!l.isEmpty()) {
            Comparator<String> cp = Comparator.naturalOrder();
            l.sort(cp);
        }
        List<String> stageList = plainFilenamesIn(ADD_STAGE_DIR);
        List<String> removeList = plainFilenamesIn(REMOVE_STAGE_DIR);
        System.out.println("=== " + "Branches" + " ===");
        System.out.println("*master");
        if(!l.isEmpty()) {
            for(String str : l) {
                System.out.println(str);
            }
        }
        System.out.println();
        System.out.println("=== " + "Staged Files" +" ===");
        if(stageList != null) {
            for (String str : stageList) {
                System.out.println(str);
            }
        }
        System.out.println();
        System.out.println("=== " + "Removed Files" + " ===");
        if(removeList != null) {
            for(String str : removeList) {
                System.out.println(str);
            }
        }
        System.out.println();
        System.out.println("=== " + "Modification Not Stage For Commit" + " ===");
        System.out.println();
        System.out.println("=== " + "Untracked Files" + " ===");
        System.out.println();
    }

    public void checkoutFilename(String fileName) {
        checkoutSpecificFile(head, fileName);
    }

    public void checkoutBranch(String NameBranch) {

    }

    public void checkoutSpecificFile(String id , String fileName) {
        List<String> cl = plainFilenamesIn(COMMIT_DIR);
        String idChecked = null;
        if (cl != null) {
            for (String s : cl) {
                if (s.equals(id)) {
                   idChecked =id;
                } else {
                    if(s.length() > id.length()) {
                        String newString = s.substring(0, id.length());
                        if(newString.equals(id)) {
                            idChecked = s;
                        }
                    }
                }
            }
        }

        if(idChecked != null) {
            File f = join(COMMIT_DIR, idChecked);
            Commit com = readObject(f, Commit.class);
            TreeMap<String, String> nameHash = com.getMap();
            String idHash = nameHash.get(fileName);
            if(idHash != null) {
                File blopFile = join(BLOB_DIR, idHash);
                File workFile = join(CWD, fileName);
                String rc = readContentsAsString(blopFile);
                writeContents(workFile, rc);
            } else {
                System.out.println("File does not exist in that commit");
            }

        } else {
            System.out.println("No commit with that id exists");
        }
    }
}
