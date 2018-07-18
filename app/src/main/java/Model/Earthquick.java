package Model;

/**
 * Created by TINKU on 1/22/2018.
 */

public class Earthquick {
    String place;
    double lat;
    double lang;
    long time;
    double magnitude;
    String type;
    String detaillink;

    public Earthquick() {
    }

    public Earthquick(String place, double lat, double lang, long time, double magnitude, String type, String detaillink) {
        this.place = place;
        this.lat = lat;
        this.lang = lang;
        this.time = time;
        this.magnitude = magnitude;
        this.type = type;
        this.detaillink = detaillink;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetaillink() {
        return detaillink;
    }

    public void setDetaillink(String detaillink) {
        this.detaillink = detaillink;
    }
}
