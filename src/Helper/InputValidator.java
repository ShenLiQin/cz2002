package Helper;

import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ValueObject.School;
import ValueObject.Venue;

public class InputValidator {
    public boolean courseStrMatcher(String courseInput) {
        boolean match = false;
        final String regex = "[a-zA-Z]\\d";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(courseInput);
        if (m.find() && courseInput.length() == 6) {
            match = true;
        }
        return match;
    }

    public boolean schoolStrMatcher(String schoolInput) {
        boolean match = false;
        for (School NTUschool : School.values()) {
            if (schoolInput.equals(NTUschool.toString())) {
                match = true;
            }
        }
        return match;

    }

    public boolean indexStrMatcher(String indexInput) {

        final String regex = "\\d";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(indexInput);
        if (m.find() && indexInput.length() == 6) {
            return true;
        }
        return false;
    }

    public boolean validateTimeInput(String timeStr) {
        boolean valid;
        try {
            String[] time = timeStr.split(":");
            if ((time[0].length() == 2) && (time[1].equals("00") || time[1].equals("30"))) {
                valid = true;
            } else {
                valid = false;
            }
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    public boolean validateTimeInput(String timeStr, String schoolStartTime, String schoolEndTime, int duration) {
        boolean valid;
        LocalTime classStartTime = LocalTime.parse(timeStr);
        LocalTime classEndTime = classStartTime.plusHours(duration);
        LocalTime earliestTime = LocalTime.parse(schoolStartTime);
        LocalTime latestTime = LocalTime.parse(schoolEndTime);
        if (!classStartTime.isBefore(earliestTime) && !classEndTime.isAfter(latestTime) && !classEndTime.isBefore(earliestTime)) {
            valid = true;
        } else {
            valid = false;
        }
        return valid;
    }

    public boolean validateYNInput(String input) {
        boolean valid;
        List<String> yList = List.of("y", "Y", "yes", "Yes");
        List<String> nList = List.of("n", "N", "no", "No");
        valid = yList.contains(input) || nList.contains(input);
        return valid;
    }

    public boolean validateVenue(String venueInput) {
        boolean match = false;
        for (Venue NTUvenue : Venue.values()) {
            if (venueInput.equals(NTUvenue.toString())) {
                match = true;
            }
        }
        return match;
    }
}