import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class risingCity {

    public static void main(String[] args) throws Exception {

        // Trying to find out if arguments has file name provided or not.
        if (args.length <= 0) {
            throw new FileNameNotFoundInArgsException();
        }

        // Extract the file name
        String fileName = args[0];

        // Read the file and save the input as list of strings.
        List<String> input = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(input::add);
        } catch (IOException e) {
            throw new InputFileNotFoundException(fileName);
        }

        //Open the output file in append mode
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(Paths.get("output_file.txt").getFileName().toString(), false)  //Set true for append mode
        );

        // At this point we have input as List<String> in the variable named input
        // We will transform into a more code understandable format
        List<InputNode> inputNodes = InputParser.parseInput(input);
        ConstructionTask constructionTask = new ConstructionTask(inputNodes, writer);
        constructionTask.start();
        writer.close();
    }
}
