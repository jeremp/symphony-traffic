package symphony.traffic.services.here.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Summary {
    private long distance ;
    private long trafficTime;
    private long travelTime;
    private long baseTime;

    public Summary() {
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getTrafficTime() {
        return trafficTime;
    }

    public void setTrafficTime(long trafficTime) {
        this.trafficTime = trafficTime;
    }

    public long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(long travelTime) {
        this.travelTime = travelTime;
    }

    public long getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
    }
}