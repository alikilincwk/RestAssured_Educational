package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Location {
    private String postcode;
    private String country;
    private String countryabbrevation;
    private List<Place> places;

    public String getPostcode() {
        return postcode;
    }

    @JsonProperty("post code")
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public String getCountryabbrevation() {
        return countryabbrevation;
    }
    @JsonProperty("country abbreviation")
    public void setCountryabbrevation(String countryabbrevation) {
        this.countryabbrevation = countryabbrevation;
    }

    @Override
    public String toString() {
        return "Location{" +
                "postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                ", countryabbrevation='" + countryabbrevation + '\'' +
                ", places=" + places +
                '}';
    }

}
