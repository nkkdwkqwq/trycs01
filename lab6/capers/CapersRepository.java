package capers;

import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** Main metadata folder. */
    static final File CAPERS_FOLDER = join(CWD ,".capers");
                                            //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
        public static void setupPersistence(){
            //TODO
            CAPERS_FOLDER.mkdir();
            Dog.DOG_FOLDER.mkdir();
        }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        // TODO
        File Story = join( CAPERS_FOLDER,"story");
        if (Story.exists()){
            text = readContentsAsString(Story) + "\n" + text;
        }
        writeContents(Story, text);
        System.out.println(text);
        /*
        try {

            if(Story.createNewFile()){
                File inFile = new File("story.txt");
                File outFile = new File("story.txt");
                writeContents(outFile, text);
                String s = readContentsAsString(inFile);
                System.out.print(s);
            } else {
                File inFile = new File("story.txt");
                String s = readContentsAsString(inFile);
                File outFile = new File("story.txt");
                writeContents(outFile, s, System.lineSeparator(),text);
                s = readContentsAsString(inFile);
                System.out.print(s);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

         */

    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        // TODO
        Dog d = new Dog(name, breed, age);
        d.saveDog();
        System.out.print(d);
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog d = Dog.fromFile(name);

        d.haveBirthday();

        d.saveDog();

    }
}
