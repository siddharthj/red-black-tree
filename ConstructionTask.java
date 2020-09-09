import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contruction Task class, this class handles the contruction and execution of commands
 */
public class ConstructionTask {
    // Min heap, it will keep the building in a heap structure keyed on executed_time
    // so that we can work on the building with least executed_time
    private MinHeap minHeap;
    // Red black tree, it will keep the buildings keyed on buildingNum to perform dynamic dictionary operations
    private RedBlackTree redBlackTree;
    // this will hold the input commands in the code readable format.
    private List<InputNode> inputNodes;
    // CLock keeps the track of the time.
    private static Integer GLOBAL_CLOCK = 0;
    // Current building in contruction
    private BuildingData currentBuilding;
    // Current building start time
    private Integer startTime;
    // Buffered writer, to output data in the file
    private BufferedWriter writer;

    /**
     * Construction for the contruction task
     * @param inputNodes
     * @param writer
     */
    ConstructionTask(List<InputNode> inputNodes, BufferedWriter writer) {
        this.minHeap = new MinHeap();
        this.redBlackTree = new RedBlackTree();
        this.inputNodes = inputNodes;
        this.writer = writer;
        this.currentBuilding = null;
        this.startTime = null;

    }

    /**
     * Start the construction task.
     * @throws Exception
     */
    void start() throws Exception {
        for (InputNode inputNode : inputNodes) {
            // The time of the next command
            Integer timeOfAppearance = inputNode.getGlobalTime();

            // In the current implementation, the global time will always be behind the time of next command
            // So the idea is to do the construction until the time of the input command.
            BuildingData buildingFinishedAtTimeOfInput = doConstruction(timeOfAppearance);
            // Read input type in the input node
            switch (inputNode.getInputType()) {
                case PRINT:
                    // Print building data
                    printBuilding(inputNode);
                    break;
                case INSERT:
                    // Insert the building data in the min heap and the red black tree
                    BuildingData data = new BuildingData(inputNode.getInput1(), 0, inputNode.getInput2());
                    minHeap.insertKey(data);
                    redBlackTree.addBuildingToTree(data.getBuildingNum(), data);
                    break;
            }
            // We need to print any buildings due to finished construction after reading the command.
            if (buildingFinishedAtTimeOfInput != null) {
                // Print the buildingNum and its completed time
                outputData("(" + buildingFinishedAtTimeOfInput.getBuildingNum() + ", " + GLOBAL_CLOCK + ")");
                // remove the building from red black tree too.
                redBlackTree.deleteTheGivenBuildingNum(buildingFinishedAtTimeOfInput.getBuildingNum());
            }
        }
        // We are done reading the input commands, now finish the construction of the remaining buildings
        doConstructionTillEnd();
    }

    /**
     * Do construction of the buildings until the time of the next command.
     * @param until
     * @return
     * @throws IOException
     */
    private BuildingData doConstruction(Integer until) throws IOException {
        // This variable will hold the node that finishes at the time of the next command.
        BuildingData finishedAtTimeOfNewInput = null;
        // Do construction until the GLOBAL_CLOCK reaches the time of the next command.
        while (GLOBAL_CLOCK < until) {
            // If the current building, that means we need to pick another building for construction
            if (currentBuilding == null) {
                // Extract the building from minHeap
                Optional<BuildingData> min = minHeap.removeMin();
                // If the minHeap in empty then move the global clock to the next input time,
                // since nothing would be done during that time
                if (!min.isPresent()) {
                    GLOBAL_CLOCK = until;
                    return finishedAtTimeOfNewInput;
                } else {
                    // Get the building with least executed_time
                    currentBuilding = min.get();
                    startTime = GLOBAL_CLOCK;
                }
            }

            // Calculate the total remaining time left in the completion of the building
            Integer remainingTime = currentBuilding.getTotalTime() - currentBuilding.getExecutedTime();
            // Calculate the remaining time that we are allowed to work on this building
            // i.e it should be less than 5 days or until the time of the next input
            Integer allowedToWorkTime = Math.min((startTime + 5), until) - GLOBAL_CLOCK;
            if (remainingTime <= allowedToWorkTime) {
                // If the remaining time is less than the allowed time to work,
                // move the global clock to remaining time of the building
                GLOBAL_CLOCK += remainingTime;
                // Update the execution time of the building
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + remainingTime);
                // If at this point we have reached the time of the next command, the priority will
                // be given to the command and we will hold of print of the building data.
                if (GLOBAL_CLOCK.equals(until)) {
                    finishedAtTimeOfNewInput = currentBuilding;
                } else {
                    // else, print the completion date of the building along with buildingNum.
                    outputData("(" + currentBuilding.getBuildingNum() + ", " + GLOBAL_CLOCK + ")");
                    // since bulding construction is complete, remove it from red black tree
                    redBlackTree.deleteTheGivenBuildingNum(currentBuilding.getBuildingNum());
                    finishedAtTimeOfNewInput = null;
                }
                // Building contruction is done, so currently no building is in contruction
                currentBuilding = null;
                startTime = null;
            } else {
                // Remaining time of the building is more than the allowed time to construct, so move
                // the clock by the time we do construction
                GLOBAL_CLOCK += allowedToWorkTime;
                // Udpate the executed time of the building
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + allowedToWorkTime);
                // Now, we need to check that,
                // if the building was constructed for max 5 days,
                //      then we need to pick another building,
                // else
                //      we will continue construction on the same building after reading the input command.
                if ((startTime + 5) <= until) {
                    // Insert the building back into the min heap.
                    minHeap.insertKey(currentBuilding);
                    // Since no building is in the construction now, so set the currentBuilding to null
                    // and its start time to null as well.
                    currentBuilding = null;
                    startTime = null;
                }

            }
        }
        // Return the building that got finished at the time of the input, it can be null.
        return finishedAtTimeOfNewInput;

    }

    /**
     * Do construction of the buildings until the city is complete.
     *
     * @throws IOException
     */
    private void doConstructionTillEnd() throws IOException {
        // Do construction until minHeap is empty and no building in construction
        while (!minHeap.isEmpty() || currentBuilding != null) {
            // If the current building, that means we need to pick another building for construction
            if (currentBuilding == null) {
                // extract the builing from the minHeap
                currentBuilding = minHeap.removeMin().orElse(null);
                startTime = GLOBAL_CLOCK;
            }
            // Calculate the total remaining time left in the completion of the building
            Integer remainingTime = currentBuilding.getTotalTime() - currentBuilding.getExecutedTime();
            Integer allowedToWorkTime = (startTime + 5) - GLOBAL_CLOCK;

            if (remainingTime <= allowedToWorkTime) {
                // If the remaining time is less than the allowed time to work,then BUILDING IS COMPLETE
                // move the global clock to remaining time of the building
                GLOBAL_CLOCK += remainingTime;
                // Update the execution time of the building
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + remainingTime);
                outputData("(" + currentBuilding.getBuildingNum() + ", " + GLOBAL_CLOCK + ")");
                redBlackTree.deleteTheGivenBuildingNum(currentBuilding.getBuildingNum());
                currentBuilding = null;
                startTime = null;
            } else {
                // Remaining time of the building is more than the allowed time to construct, so move
                // the clock by the time we did construction
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + allowedToWorkTime);
                // Move the global time
                GLOBAL_CLOCK += allowedToWorkTime;
                minHeap.insertKey(currentBuilding);
                currentBuilding = null;
                startTime = null;
            }
        }
    }

    /**
     * Read the print input command and print the building data
     * @param inputNode
     * @throws IOException
     */
    private void printBuilding(InputNode inputNode) throws IOException {
        if (inputNode.getInput2() == null) {
            printSingleBuilding(inputNode);
        } else {
            printRangeBuildings(inputNode);
        }
    }

    /**
     * Print single building data
     * @param inputNode
     * @throws IOException
     */
    private void printSingleBuilding(InputNode inputNode) throws IOException {
        Optional<BuildingData> buildingData = redBlackTree.get(inputNode.getInput1());
        if (buildingData.isPresent()) {
            outputData(getPrintData(buildingData.get()));
        } else {
            outputData("(0, 0, 0)");
        }
    }

    /**
     * Print data of the buildings for the specified range
     * @param inputNode
     * @throws IOException
     */
    private void printRangeBuildings(InputNode inputNode) throws IOException {
        List<BuildingData> buildingDataList = new ArrayList<>();
        redBlackTree.getBuildingsInRange(buildingDataList, inputNode.getInput1(), inputNode.getInput2());
        if (buildingDataList.isEmpty()) {
            outputData("(0, 0, 0)");
        } else {
            outputData(buildingDataList.stream().map(this::getPrintData).collect(Collectors.joining(",")));
        }
    }

    /**
     * Get the output string for printing it.
     * @param buildingData
     * @return
     */
    private String getPrintData(BuildingData buildingData) {
        return String.format("(%d, %d, %d)",
                buildingData.getBuildingNum(),
                buildingData.getExecutedTime(),
                buildingData.getTotalTime());
    }

    /**
     * Output the data into the file
     * @param output
     * @throws IOException
     */
    private void outputData(String output) throws IOException {
        writer.newLine();
        writer.append(output);
    }
}
