package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {
    private String placename;
    private String longitude;
    private String state;
    private String stateabbrevation;
    private String latitude;

    public String getPlacename() {
        return placename;
    }
    @JsonProperty("place name")
    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateabbrevation() {
        return stateabbrevation;
    }
    @JsonProperty("state abbreviation")
    public void setStateabbrevation(String stateabbrevation) {
        this.stateabbrevation = stateabbrevation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Place{" +
                "placename='" + placename + '\'' +
                ", longitude='" + longitude + '\'' +
                ", state='" + state + '\'' +
                ", stateabbrevation='" + stateabbrevation + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}