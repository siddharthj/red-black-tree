package com.siddharth;

public class BuildingData {
    public Integer getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(Integer buildingNum) {
        this.buildingNum = buildingNum;
    }

    public Integer getExecutedTime() {
        return executedTime;
    }

    public void setExecutedTime(Integer executedTime) {
        this.executedTime = executedTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    Integer buildingNum;
    Integer executedTime;
    Integer totalTime;





    public BuildingData(Integer buildingNum, Integer executedTime, Integer totalTime) {
        this.buildingNum = buildingNum;
        this.executedTime = executedTime;
        this.totalTime = totalTime;
    }
}
