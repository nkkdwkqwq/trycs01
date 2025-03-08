package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author listu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if (args.length > 1) {
                    System.out.println("Incorrect operands.");
                    break;
                }
                Repository repInit = new Repository();
                repInit.setUpRepository();
                repInit.saveRepository();
                break;

            case "add":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep0 = Repository.readRepository();
                rep0.addFileToRepository(args[1]);
                rep0.saveRepository();
                break;

            case "commit":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep1 = Repository.readRepository();
                rep1.commitToRepository(args[1]);
                rep1.saveRepository();
                break;

            case "log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep2 = Repository.readRepository();
                rep2.logTheCommit();
                break;

            case "rm":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep3 = Repository.readRepository();
                rep3.rmFileToRepository(args[1]);
                rep3.saveRepository();
                break;

            case "global-log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep4 = Repository.readRepository();
                rep4.globalLogTheCommit();
                break;

            case "find":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep5 = Repository.readRepository();
                rep5.findCommitMessageId(args[1]);
                break;

            case "status":
                if (args.length != 1) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep6 = Repository.readRepository();
                rep6.gitStatus();
                break;

            case "checkout":
                if (args.length > 4 || args.length == 1) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep7 = Repository.readRepository();
                if (args.length == 2) {
                    rep7.checkoutBranch(args[1]);
                } else if (args.length == 3) {
                    if(!args[1].equals("--")) {
                        System.out.println("Incorrect operands");
                        break;
                    }
                    rep7.checkoutFilename(args[2]);
                } else {
                    if(!args[2].equals("--")) {
                        System.out.println("Incorrect operands");
                        break;
                    }
                    rep7.checkoutSpecificFile(args[1], args[3]);
                }
                rep7.saveRepository();
                break;

            case "branch":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep8 = Repository.readRepository();
                rep8.setBranch(args[1]);
                rep8.saveRepository();
                break;

            case "rm-branch":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep9 = Repository.readRepository();
                rep9.rmBranch(args[1]);
                rep9.saveRepository();
                break;

            case "reset":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep10 = Repository.readRepository();
                rep10.reSet(args[1]);
                rep10.saveRepository();
                break;

            case "merge":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if (!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep11 = Repository.readRepository();
                rep11.mergeBranch(args[1]);
                rep11.saveRepository();
                break;

            default:
                System.out.println("No command with that name exists");
                System.exit(0);
                break;

        }
    }
}
