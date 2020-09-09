/**
 * If the input is not in the correct format, then throw this exception
 */
public class InputNotInCorrectFormatException extends Exception {
    public InputNotInCorrectFormatException(String input) {
        super("Input - "+  input + " - not in the correct format");
        System.out.println("Input - "+  input + " - not in the correct format");
    }
}
