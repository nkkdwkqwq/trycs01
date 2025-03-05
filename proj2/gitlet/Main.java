package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if(args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if (args.length > 1) {
                    System.out.println("Incorrect operands.");
                    break;
                }
                Repository repInit = new Repository();
                repInit.setUpRepository();
                repInit.saveRepository();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    break;
                }
                if(!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep0 = Repository.readRepository();
                rep0.addFileToRepository(args[1]);
                rep0.saveRepository();
                break;
            // TODO: FILL THE REST IN

            case "commit":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if(!Repository.GITLET_DIR.exists()) {
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
                if(!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep2 = Repository.readRepository();
                rep2.logTheCommit();
                break;

            case "rm":
                if(args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if(!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep3 = Repository.readRepository();
                rep3.rmFileToRepository(args[1]);
                rep3.saveRepository();
                break;

            case "global-log":
                if(args.length != 1) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if(!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep4 = Repository.readRepository();
                rep4.globalLogTheCommit();
                break;

            case "find":
                if(args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if(!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep5 = Repository.readRepository();
                rep5.findCommitMessageId(args[1]);
                break;

            case "status":
                if(args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if(!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep6 = Repository.readRepository();
                rep6.gitState();
                break;

            case "checkout":
                if(args.length > 4 || args.length == 1) {
                    System.out.println("Incorrect operands");
                    break;
                }
                if(!Repository.GITLET_DIR.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repository rep7 = Repository.readRepository();
                if(args.length == 2) {
                    rep7.checkoutBranch(args[1]);
                } else if (args.length == 3) {
                    rep7.checkoutFilename(args[2]);
                } else {
                    rep7.checkoutSpecificFile(args[1], args[3]);
                }
                break;

            default:
                System.out.println("No command with that name exists");
                System.exit(0);
                break;

        }
    }
}
