/**
 * If file name is not present in argument, then throw this exception
 */
public class FileNameNotFoundInArgsException extends Exception {
    public FileNameNotFoundInArgsException() {
        super("File name not provided in the args");
        System.out.println("File name not provided in the args");
    }
}

