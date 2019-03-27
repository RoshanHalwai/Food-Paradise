package com.citylights.foodparadise.pojos;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**

 * Created by Roshan Halwai on 3/22/2019
 */
public class TrainDetails implements Serializable {

    private String trainName;
    private String trainNumber;
    private List<Object> stations;

    public TrainDetails() {
    }

    public TrainDetails(String trainName, String trainNumber, List<Object> stations) {
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.stations = stations;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public List<Object> getStations() {
        return stations;
    }

    public List<String> getStationCodes() {
        List<String> stationCodesList = new LinkedList<>();
        if (stations != null) {
            for (Object station : stations) {
                stationCodesList.add((String) station);
            }
        }
        return stationCodesList;
    }

}
