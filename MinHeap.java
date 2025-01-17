import java.util.Optional;

/**
 * Min heap, it will keep the building in a heap structure keyed on executed_time
 * so that we can work on the building with least executed_time
 */
public class MinHeap {
    // Array based implementation
    private BuildingData[] items;
    // Size of the heap
    private Integer heapSize;

    /**
     * Contructor
     */
    public MinHeap() {
        items = new BuildingData[2100];
        heapSize = 0;
    }

    /**
     * Swap the building data
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        BuildingData k = items[i];
        items[i] = items[j];
        items[j] = k;
    }

    /**
     * Is the min heap empty
     * @return
     */
    public boolean isEmpty() {
        return heapSize == 0;
    }

    /**
     * Increase key of a particuar building, basically update the new execution time.
     * @param index
     * @param newExecutionTime
     */
    public void increaseKey(Integer index, Integer newExecutionTime) {
        BuildingData buildingData = items[index];
        buildingData.setExecutedTime(newExecutionTime);
        items[index] = buildingData;
        heapify(index);
    }

    /**
     * Insert the building into the hea.
     * @param buildingData
     */
    public void insertKey(BuildingData buildingData) {
        heapSize++;
        int index = heapSize - 1;
        items[index] = buildingData;

        while (index != 0) {
            Integer parentIndex = ((index - 1) / 2);
            Integer parentExecutedTime = items[parentIndex].getExecutedTime();
            Integer indexExecutedTime = items[index].getExecutedTime();
            Integer parentBuildingNum = items[parentIndex].getBuildingNum();
            Integer indexBuildingNum = items[index].getBuildingNum();
            if (parentExecutedTime > indexExecutedTime ||
                    (parentExecutedTime.equals(indexExecutedTime) && parentBuildingNum > indexBuildingNum)) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * Remove min from the heap.
     * @return
     */
    public Optional<BuildingData> removeMin() {
        if (heapSize == 0) {
            return Optional.empty();
        }
        BuildingData buildingData = items[0];
        heapSize--;
        items[0] = items[heapSize];
        if (heapSize > 0)
            heapify(0);
        return Optional.of(buildingData);
    }

    /**
     * Peek the min value of the heap.
     * @return
     */
    public BuildingData peekMin() {
        return items[0];
    }

    /**
     * Heapify
     * @param index
     */
    private void heapify(Integer index) {
        int leftIndex = (index * 2) + 1;
        int rightIndex = (index * 2) + 2;


        if (!(index >= heapSize / 2 && index <= heapSize)) { // Non leaf node
            Integer leftExecutedTime = items[leftIndex].getExecutedTime();
            Integer rightExecutedTime = items[rightIndex].getExecutedTime();
            Integer indexExecutedTime = items[index].getExecutedTime();

            Integer leftBuildingNum = items[leftIndex].getBuildingNum();
            Integer rightBuildingNum = items[rightIndex].getBuildingNum();
            Integer indexBuildingNum = items[index].getBuildingNum();


            if (((indexExecutedTime > leftExecutedTime) || (leftExecutedTime.equals(indexExecutedTime) && (leftBuildingNum < indexBuildingNum))) ||
                    ((indexExecutedTime > rightExecutedTime) || (rightExecutedTime.equals(indexExecutedTime) && (rightBuildingNum < indexBuildingNum)))) {
                // Check for smaller child, whether its right or left
                if ((leftExecutedTime < rightExecutedTime) || (leftExecutedTime.equals(rightExecutedTime) && (leftBuildingNum < rightBuildingNum))) {
                    swap(index, leftIndex);
                    heapify(leftIndex);
                } else {
                    swap(index, rightIndex);
                    heapify(rightIndex);
                }
            }
        }
    }
}
