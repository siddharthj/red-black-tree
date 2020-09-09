/**
 * Building data, it holds the information related to building construction status.
 */
public class BuildingData {
    /**
     * Get buildingNum.
     * @return
     */
    public Integer getBuildingNum() {
        return buildingNum;
    }

    /**
     * Set buildingNum.
     * @param buildingNum
     */
    public void setBuildingNum(Integer buildingNum) {
        this.buildingNum = buildingNum;
    }

    /**
     * Get executed time.
     * @return
     */
    public Integer getExecutedTime() {
        return executedTime;
    }

    /**
     * Set executed time.
     * @param executedTime
     */
    public void setExecutedTime(Integer executedTime) {
        this.executedTime = executedTime;
    }

    /**
     * Get total time needed for construction
     * @return
     */
    public Integer getTotalTime() {
        return totalTime;
    }

    /**
     * Set total time needed for building construction
     * @param totalTime
     */
    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    Integer buildingNum;
    Integer executedTime;
    Integer totalTime;

    /**
     * Constructor for building data
     * @param buildingNum
     * @param executedTime
     * @param totalTime
     */
    public BuildingData(Integer buildingNum, Integer executedTime, Integer totalTime) {
        this.buildingNum = buildingNum;
        this.executedTime = executedTime;
        this.totalTime = totalTime;
    }
}
