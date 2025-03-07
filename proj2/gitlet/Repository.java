package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
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
    private String branchName;
    /** The hash of branch in git */
    private String branchHash;
    /** The head in git */
    private String head;
    /** The stage area in git */
    public static final File STAGE_DIR = join(GITLET_DIR, "stage");
    /** The stage for add */
    public static final File ADD_STAGE_DIR = join(STAGE_DIR, "addArea");
    /** The stage for remove */
    public static final File REMOVE_STAGE_DIR = join(STAGE_DIR, "removeArea");
    /** The area for commit */
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    /** The area store blob */
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");
    /** Array store the branch */
    private TreeMap<String, String> branches = new TreeMap<>();
    /** merge action helped variety */
    private boolean merged = false;
    /** the id of second parent */
    private String secondParentID0 = null;


    public void setUpRepository() {

        /** Create essential dir */
        if (!GITLET_DIR.mkdir()) {
            System.out.println("A Gitlet version-control system already exists "
                    + "in the current directory");
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

    /** add some file*/
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
        if (tm != null) {
            if (Objects.equals(tm.get(name), sha1(b))) {
                File f0 = join(Repository.ADD_STAGE_DIR, name);
                if (f0.exists()) {
                    f0.delete();
                    return;
                }
            }
        }
        File f2 = join(BLOB_DIR, sha1(b));
        File f1 = join(ADD_STAGE_DIR, name);
        writeContents(f1, b);
        writeContents(f2, b);

    }

    public void commitToRepository(String message) {
        Commit com = new Commit();
        com.gitCommit(message, head);
        if (merged) {
            com.setSecondParent(secondParentID0);
        }
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
            if (com.getSecondParent() != null) {
                System.out.println("Merge: " + parentID.substring(0, 7)
                        + " " + com.getSecondParent().substring(0, 7));
            }
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
        if (addtionFile.exists()) {
            restrictedDelete(addtionFile);
            return;
        }
        File commitClass = join(Repository.COMMIT_DIR, head);
        Commit com = readObject(commitClass, Commit.class);
        TreeMap<String, String> tm = com.getMap();
        if (tm.get(name) != null) {
            File removeFile = join(Repository.REMOVE_STAGE_DIR, name);
            File blobFile = join(Repository.BLOB_DIR, tm.get(name));
            byte[] b = readContents(blobFile);
            writeContents(removeFile, b);
            if (workFile.exists()) {
                restrictedDelete(workFile);
            }
            return;
        }
        System.out.println("No reason to remove the file");
    }

    public void globalLogTheCommit() {
        List<String> l = plainFilenamesIn(Repository.COMMIT_DIR);
        for (String current : l) {
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
        for (String current : l) {
            File f = new File(Repository.COMMIT_DIR, current);
            Commit com = readObject(f, Commit.class);
            if (message.equals(com.getMessage())) {
                System.out.println(current);
                existMessage = true;
            }
        }
        if (!existMessage) {
            System.out.println("Found no commit with that message");
        }
    }

    public void gitStatus() {
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
        if (!l.isEmpty()) {
            for (String str : l) {
                System.out.println(str);
            }
        }
        System.out.println();
        System.out.println("=== " + "Staged Files" + " ===");
        if (stageList != null) {
            for (String str : stageList) {
                System.out.println(str);
            }
        }
        System.out.println();
        System.out.println("=== " + "Removed Files" + " ===");
        if (removeList != null) {
            for (String str : removeList) {
                System.out.println(str);
            }
        }
        System.out.println();
        System.out.println("=== " + "Modification Not Stage For Commit" + " ===");
        System.out.println();
        System.out.println("=== " + "Untracked Files" + " ===");
        HashSet<String> hs = untrackedFile();
        if (!hs.isEmpty()) {
            Comparator<String> cp = Comparator.naturalOrder();
            ArrayList<String> l0 = new ArrayList<>(hs);
            l0.sort(cp);
            for (String str : l0) {
                System.out.println(str);
            }
        }
        System.out.println();
    }

    public void checkoutFilename(String fileName) {
        checkoutSpecificFile(head, fileName);
    }

    /** Return untracked file in the current branch*/
    private HashSet<String> untrackedFile() {
        List<String> allWorkFileName = plainFilenamesIn(CWD);
        List<String> addFileName = plainFilenamesIn(ADD_STAGE_DIR);
        List<String> removeFileName = plainFilenamesIn(REMOVE_STAGE_DIR);
        File currentCommit = join(COMMIT_DIR, head);
        Commit currentCom = readObject(currentCommit, Commit.class);
        TreeMap<String, String> allCurrentFileNameHash = currentCom.getMap();
        HashSet<String> untracked = new HashSet<>();
        HashSet<String> hsCommit = new HashSet<>();
        HashSet<String> hsWork = new HashSet<>();
        HashSet<String> hsRemove = new HashSet<>();
        HashSet<String> hsAdd = new HashSet<>();
        if (allCurrentFileNameHash != null) {
            hsCommit.addAll(allCurrentFileNameHash.keySet());
        }
        if (allWorkFileName != null) {
            hsWork.addAll(allWorkFileName);
        }
        if (addFileName != null) {
            hsAdd.addAll(addFileName);
        }
        if (removeFileName != null) {
            hsRemove.addAll(removeFileName);
        }
        if (allWorkFileName != null) {
            for (String s : allWorkFileName) {
                if (!hsAdd.contains(s) && !hsCommit.contains(s)) {
                    untracked.add(s);
                }
            }
        }
        return untracked;
    }

    /** solve the problem later */
    public void checkoutBranch(String nameBranch) {
        if (branches.get(nameBranch) == null) {
            System.out.println("No such branch exists");
            return;
        }
        if (branches.get(nameBranch).equals(head)) {
            System.out.println("No need to check out the current branch");
            return;
        }
        List<String> allWorkFileName = plainFilenamesIn(CWD);
        File currentCommit = join(COMMIT_DIR, head);
        Commit currentCom = readObject(currentCommit, Commit.class);
        String temptHead = branches.get(nameBranch);
        File checkoutCommit = join(COMMIT_DIR, temptHead);
        Commit checkoutCom = readObject(checkoutCommit, Commit.class);
        TreeMap<String, String> allCurrentFileNameHash = currentCom.getMap();
        TreeMap<String, String> allCheckoutFileNameHash = checkoutCom.getMap();
        TreeMap<String, String> copyAllCheckoutFileNameHash = new TreeMap<>(allCheckoutFileNameHash);
        if (allWorkFileName != null) {
            for (String fileName : allWorkFileName) {
                if (allCurrentFileNameHash.get(fileName) == null) {
                    if (allCheckoutFileNameHash.get(fileName) != null) {
                        System.out.println("There is an untracked file in the way; "
                                + "delete it, or add and commit it first.");
                        System.exit(0);
                    }
                }
            }
        }

        for (String name : allCheckoutFileNameHash.keySet()) {
            String hash = allCheckoutFileNameHash.get(name);
            File blobFile = join(BLOB_DIR, hash);
            File currentWorkFile = join(CWD, name);
            copyAllCheckoutFileNameHash.remove(name);
            writeContents(currentWorkFile, readContentsAsString(blobFile));
        }

        for (String name : allCurrentFileNameHash.keySet()) {
            if(allCheckoutFileNameHash.get(name) == null) {
                File f = join(CWD, name);
                restrictedDelete(f);
            }
        }

        List<String> addList = plainFilenamesIn(ADD_STAGE_DIR);
        if (addList != null) {
            for (String str : addList) {
                File f = join(ADD_STAGE_DIR, str);
                f.delete();
            }
        }

        List<String> removeList = plainFilenamesIn(REMOVE_STAGE_DIR);
        if (removeList != null) {
            for (String str : removeList) {
                File f = join(REMOVE_STAGE_DIR, str);
                f.delete();
            }
        }

        head = branches.get(nameBranch);
        branchName = nameBranch;
    }

    public void checkoutSpecificFile(String id , String fileName) {
        List<String> cl = plainFilenamesIn(COMMIT_DIR);
        String idChecked = null;
        if (cl != null) {
            for (String s : cl) {
                if (s.equals(id)) {
                   idChecked =id;
                } else {
                    if (s.length() > id.length()) {
                        String newString = s.substring(0, id.length());
                        if (newString.equals(id)) {
                            idChecked = s;
                        }
                    }
                }
            }
        }

        if (idChecked != null) {
            File f = join(COMMIT_DIR, idChecked);
            Commit com = readObject(f, Commit.class);
            TreeMap<String, String> nameHash = com.getMap();
            String idHash = nameHash.get(fileName);
            if (idHash != null) {
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

    public void setBranch(String name) {
        if (branches.get(name) != null) {
            System.out.println("A branch with that name already exists");
            return;
        }
        branches.put(branchName,branchHash);
    }

    public void rmBranch(String name) {
        if (branches.get(name) == null) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        if (branches.get(name).equals(head)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        branches.remove(name);
    }

    public void reSet(String id) {
        List<String> cl = plainFilenamesIn(COMMIT_DIR);
        String idChecked = null;
        if (cl != null) {
            for (String s : cl) {
                if (s.equals(id)) {
                    idChecked =id;
                } else {
                    if (s.length() > id.length()) {
                        String newString = s.substring(0, id.length());
                        if (newString.equals(id)) {
                            idChecked = s;
                        }
                    }
                }
            }
        }

        if(idChecked == null) {
            System.out.println("No commit with that id exists.");
            return;
        }

        List<String> allWorkFileName = plainFilenamesIn(CWD);
        File currentCommit = join(COMMIT_DIR, head);
        Commit currentCom = readObject(currentCommit, Commit.class);
        File checkoutCommit = join(COMMIT_DIR, idChecked);
        Commit checkoutCom = readObject(checkoutCommit, Commit.class);
        TreeMap<String, String> allCurrentFileNameHash = currentCom.getMap();
        TreeMap<String, String> allCheckoutFileNameHash = checkoutCom.getMap();
        TreeMap<String, String> copyAllCheckoutFileNameHash =new TreeMap<>(allCheckoutFileNameHash);
        if (allWorkFileName != null) {
            for (String fileName : allWorkFileName) {
                if (allCurrentFileNameHash.get(fileName) == null) {
                    if (allCheckoutFileNameHash.get(fileName) != null) {
                        System.out.println("There is an untracked file in the way; "
                                + "delete it, or add and commit it first.");
                        System.exit(0);
                    }
                }
            }
        }

        for (String name : allCheckoutFileNameHash.keySet()) {
            String hash = allCheckoutFileNameHash.get(name);
            File blobFile = join(BLOB_DIR, hash);
            File currentWorkFile = join(CWD, name);
            copyAllCheckoutFileNameHash.remove(name);
            writeContents(currentWorkFile, readContentsAsString(blobFile));
        }

        for (String name : allCurrentFileNameHash.keySet()) {
            if (allCheckoutFileNameHash.get(name) == null) {
                File f = join(CWD, name);
                restrictedDelete(f);
            }
        }

        List<String> addList = plainFilenamesIn(ADD_STAGE_DIR);
        if (addList != null) {
            for (String str : addList) {
                File f = join(ADD_STAGE_DIR, str);
                f.delete();
            }
        }

        List<String> removeList = plainFilenamesIn(REMOVE_STAGE_DIR);
        if (removeList != null) {
            for (String str : removeList) {
                File f = join(REMOVE_STAGE_DIR, str);
                f.delete();
            }
        }
        head = idChecked;
    }

    private boolean isMergedCheck(String nameBranch) {
        if (plainFilenamesIn(ADD_STAGE_DIR) != null || plainFilenamesIn(REMOVE_STAGE_DIR) != null) {
            System.out.println("You have uncommited changes.");
            return false;
        }
        if (branches.get(nameBranch) == null) {
            System.out.println("A branch with that name does not exist.");
            return false;
        }
        if (branches.get(nameBranch).equals(head)) {
            System.out.println("Cannot merge a branch with itself");
            return false;
        }
        return true;
    }
    public void mergeBranch(String nameBranch) {
        if (!isMergedCheck(nameBranch)) {
            return;
        }
        HashSet<String> untrackedFile = untrackedFile();
        File givenCommitFile = join(COMMIT_DIR, branches.get(nameBranch));
        Commit givenCommit = readObject(givenCommitFile, Commit.class);
        TreeMap<String, String> givenCommitTrackedNameHash = givenCommit.getMap();
        File currentCommitFile = join(COMMIT_DIR, head);
        Commit currentCommit = readObject(currentCommitFile, Commit.class);
        TreeMap<String, String> currentCommitTrackedNameHash = currentCommit.getMap();
        String currentID = head;
        String givenID = branches.get(nameBranch);
        String tempC = currentID;
        String tempG = givenID;
        String splitPointID = "Stuff";
        boolean meetConflict = false;
        int temp = 0;
        HashSet<String> givenHashFileName = new HashSet<>(givenCommitTrackedNameHash.keySet());
        HashSet<String> currentHashFileName = new HashSet<>(currentCommitTrackedNameHash.keySet());
        for (String str : untrackedFile) {
            if (givenCommitTrackedNameHash.get(str) != null) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                return;
            }
        }

        while (tempC != null && temp == 0) {
            File cm = join(COMMIT_DIR, tempC);
            Commit cc = readObject(cm, Commit.class);
            while (tempG != null && temp == 0) {
                if (tempC.equals(tempG)) {
                    splitPointID = tempC;
                    temp = 1;
                }
                File gm = join(COMMIT_DIR, tempG);
                Commit gc = readObject(gm, Commit.class);
                tempG = gc.getParentID();
            }
            tempC = cc.getParentID();
        }

        if (splitPointID.equals(givenID)) {
            System.out.println("Given branch is an ancestor of the current branch.");
        }

        if (splitPointID.equals(currentID)) {
            checkoutBranch(nameBranch);
            System.out.println("Current branch fast-forwarded.");
        }

        File splitCommitFile = join(COMMIT_DIR, splitPointID);
        Commit splitCommit = readObject(splitCommitFile, Commit.class);
        TreeMap<String, String> splitCommitTrackedNameHash = splitCommit.getMap();
        HashSet<String> splitHashFileName = new HashSet<>(splitCommitTrackedNameHash.keySet());

        for (String nameSplit : splitHashFileName) {
            String hashSplit = splitCommitTrackedNameHash.get(nameSplit);
            String hashCurrent = currentCommitTrackedNameHash.get(nameSplit);
            String hashGiven = givenCommitTrackedNameHash.get(nameSplit);
            if (!hashSplit.equals(hashCurrent) && hashSplit.equals(hashGiven) && hashCurrent != null) {
                    checkoutSpecificFile(givenID,nameSplit);
                    addFileToRepository(nameSplit);
                    continue;
            }
            if (hashSplit.equals(hashCurrent) && hashGiven == null) {
                rmFileToRepository(nameSplit);
                continue;
            }

            if (!hashSplit.equals(hashGiven) && !hashSplit.equals(hashCurrent)) {
                File cur = join(BLOB_DIR, hashCurrent);
                File giv = join(BLOB_DIR, hashGiven);
                File work = join(CWD, nameSplit);

                String writesContents = "<<<<<<< HEAD " + System.lineSeparator() + readContentsAsString(cur) +
                        "=======" + System.lineSeparator() + readContentsAsString(giv) + ">>>>>>>";
                writeContents(work, writesContents);
                addFileToRepository(nameSplit);
                meetConflict = true;
            }
        }

        for (String nameSplit : givenHashFileName) {
            String hashSplit = splitCommitTrackedNameHash.get(nameSplit);
            String hashCurrent = currentCommitTrackedNameHash.get(nameSplit);
            String hashGiven = givenCommitTrackedNameHash.get(nameSplit);
            if (hashCurrent == null && hashSplit == null && hashGiven != null) {
                checkoutSpecificFile(givenID, nameSplit);
                addFileToRepository(nameSplit);
            }
        }
        merged = true;
        secondParentID0 = branches.get(nameBranch);
        commitToRepository("Merged [" + nameBranch + "] into [" + branchName + "]." );
        merged = false;
        if (meetConflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }
}
