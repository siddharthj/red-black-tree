/**
 * If input file is not present in current directory, then throw this exception
 */
public class InputFileNotFoundException extends Exception {
    public InputFileNotFoundException(String message) {
        super("File - " + message+"- not present in the current directory.");
        System.out.println("File - " + message+"- not present in the current directory.");
    }
}

