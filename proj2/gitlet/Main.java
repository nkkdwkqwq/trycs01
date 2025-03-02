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
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if (args.length > 1) {
                    System.out.println("Incorrect operands");
                    break;
                }
                Repository.setUpRepository();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                Repository.addFileToRepository(args[1]);
                break;
            // TODO: FILL THE REST IN

            case "commit":
                if (args.length != 2) {
                    System.out.println("Incorrect operands");
                    break;
                }
                Repository.commitToRepository(args[1]);

            case "":
                System.out.println("Please enter a command");
                System.exit(0);
                break;

            default:
                System.out.println("No command with that name exists");
                System.exit(0);
                break;

        }
    }
}
