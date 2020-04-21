/**
 * Thomas Scott - S1824798
 * Description - This class is used to create a new instance of futureroadwork and is used when getting the data from the feed
 * It stores title, description and coordinates and has getters, setters and tostring functions.
 */
package com.example.tscott202mpd_cw;

public class FutureRoadwork {

    private String title;
    private String description;
    private String coordinates;

    public FutureRoadwork() {
        this.title = "";
        this.description = "";
        this.coordinates = "";
    }

    public FutureRoadwork(String title, String description, String coordinates) {
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        String string;
        string = title + "\n" + description + "\n" + coordinates + "\n\n";
        return string;
    }
}
