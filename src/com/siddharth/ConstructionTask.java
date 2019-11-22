package com.siddharth;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConstructionTask {
    private MinHeap minHeap;
    private RedBlackTree redBlackTree;
    private List<InputNode> inputNodes;
    private static Integer GLOBAL_CLOCK = 0;
    private BuildingData currentBuilding;
    private Integer startTime;
    private BufferedWriter writer;

    ConstructionTask(List<InputNode> inputNodes, BufferedWriter writer) {
        this.minHeap = new MinHeap();
        this.redBlackTree = new RedBlackTree();
        this.inputNodes = inputNodes;
        this.writer = writer;
        this.currentBuilding = null;
        this.startTime = null;

    }

    void start() throws Exception {
        for (InputNode inputNode : inputNodes) {
            Integer timeOfAppearance = inputNode.getGlobalTime();

            //In the current implementation, the global time will always be behind the input time, so the idea is to do
            // all the construction until the time of the input comes.
            BuildingData buildingFinishedAtTimeOfInput = doConstruction(timeOfAppearance);

            switch (inputNode.getInputType()) {
                case PRINT:
                    printBuilding(inputNode);
                    break;
                case INSERT:
                    BuildingData data = new BuildingData(inputNode.getInput1(), 0, inputNode.getInput2());
                    minHeap.insertKey(data);
                    redBlackTree.put(data.getBuildingNum(), data);
                    break;
            }
            if (buildingFinishedAtTimeOfInput != null) {
                outputData("(" + buildingFinishedAtTimeOfInput.getBuildingNum() + "," + GLOBAL_CLOCK + ")");
                redBlackTree.delete(buildingFinishedAtTimeOfInput.getBuildingNum());
            }
        }
        doConstructionTillEnd();
        outputData("City finish time :" + GLOBAL_CLOCK);
    }

    private BuildingData doConstruction(Integer until) throws IOException {
        BuildingData finishedAtTimeOfNewInput = null;
        while (GLOBAL_CLOCK < until) {
            if (currentBuilding == null) {
                Optional<BuildingData> min = minHeap.removeMin();
                if (!min.isPresent()) {
                    GLOBAL_CLOCK = until;
                    return finishedAtTimeOfNewInput;
                } else {
                    currentBuilding = min.get();
                    startTime = GLOBAL_CLOCK;
                }
            }

            Integer remainingTime = currentBuilding.getTotalTime() - currentBuilding.getExecutedTime();
            Integer allowedToWorkTime = Math.min((startTime + 5), until) - GLOBAL_CLOCK;
            if (remainingTime <= allowedToWorkTime) {
                GLOBAL_CLOCK += remainingTime;
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + remainingTime);
                if (GLOBAL_CLOCK.equals(until)) {
                    finishedAtTimeOfNewInput = currentBuilding;
                } else {
                    outputData("(" + currentBuilding.getBuildingNum() + "," + GLOBAL_CLOCK + ")");
                    redBlackTree.delete(currentBuilding.getBuildingNum());
                    finishedAtTimeOfNewInput = null;
                }
                currentBuilding = null;
                startTime = null;
            } else {
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + allowedToWorkTime);
                GLOBAL_CLOCK += allowedToWorkTime;
                if ((startTime + 5) <= until) {
                    minHeap.insertKey(currentBuilding);
                    currentBuilding = null;
                    startTime = null;
                }

            }
        }
        return finishedAtTimeOfNewInput;

    }

    private void doConstructionTillEnd() throws IOException {
        while (!minHeap.isEmpty() || currentBuilding != null) {
            if (currentBuilding == null) {
                currentBuilding = minHeap.removeMin().orElse(null);
                startTime = GLOBAL_CLOCK;
            }
            Integer remainingTime = currentBuilding.getTotalTime() - currentBuilding.getExecutedTime();
            Integer allowedToWorkTime = (startTime + 5) - GLOBAL_CLOCK;

            if (remainingTime <= allowedToWorkTime) {
                GLOBAL_CLOCK += remainingTime;
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + remainingTime);
                outputData("(" + currentBuilding.getBuildingNum() + "," + GLOBAL_CLOCK + ")");
                redBlackTree.delete(currentBuilding.getBuildingNum());
                currentBuilding = null;
                startTime = null;
            } else {
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + allowedToWorkTime);
                GLOBAL_CLOCK += allowedToWorkTime;
                minHeap.insertKey(currentBuilding);
                currentBuilding = null;
                startTime = null;
            }
        }
    }

    private void printBuilding(InputNode inputNode) throws IOException {
        if (inputNode.getInput2() == null) {
            printSingleBuilding(inputNode);
        } else {
            printRangeBuildings(inputNode);
        }
    }

    private void printSingleBuilding(InputNode inputNode) throws IOException {
        Optional<BuildingData> buildingData = redBlackTree.get(inputNode.getInput1());
        if (buildingData.isPresent()) {
            outputData(getPrintData(buildingData.get()));
        } else {
            outputData("(0,0,0)");
        }
    }

    private void printRangeBuildings(InputNode inputNode) throws IOException {
        List<BuildingData> buildingDataList = redBlackTree.keys(inputNode.getInput1(), inputNode.getInput2());
        if (buildingDataList.isEmpty()) {
            outputData("(0,0,0)");
        } else {
            outputData(buildingDataList.stream().map(this::getPrintData).collect(Collectors.joining(",")));
        }
    }

    private String getPrintData(BuildingData buildingData) {
        return String.format("(%d,%d,%d)",
                buildingData.getBuildingNum(),
                buildingData.getExecutedTime(),
                buildingData.getTotalTime());
    }

    private void outputData(String output) throws IOException {
        System.out.println(output);
        writer.newLine();
        writer.append(output);
    }
}
