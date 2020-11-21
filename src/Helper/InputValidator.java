package Helper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ValueObject.Gender;
import ValueObject.Nationality;
import ValueObject.School;
import ValueObject.Venue;

public class InputValidator {
    public static boolean courseStrMatcher(String courseInput) {
        boolean match = false;
        final String regex = "[a-zA-Z]\\d";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(courseInput);
        if (m.find() && courseInput.length() == 6) {
            match = true;
        }
        return match;
    }

    public static boolean schoolStrMatcher(String schoolInput) {
        boolean match = false;
        for (School NTUschool : School.values()) {
            if (schoolInput.equals(NTUschool.toString())) {
                match = true;
            }
        }
        return match;
    }

    public static boolean genderStrMatcher(String genderInput) {
        boolean match = false;
        for (Gender gender : Gender.values()) {
            if (genderInput.equals(gender.toString())) {
                match = true;
            }
        }
        return match;
    }

    public static boolean nationalityStrMatcher(String nationalityInput) {
        boolean match = false;
        for (Nationality nationality : Nationality.values()) {
            if (nationalityInput.equals(nationality.toString())) {
                match = true;
            }
        }
        return match;
    }

    public static boolean indexStrMatcher(String indexInput) {
        boolean valid;
        valid = indexInput.matches("\\d{6}");
        return valid;
    }

    public static boolean validateDateTimeInput(String dateTimeStr) {
        boolean valid;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            LocalDateTime.parse(dateTimeStr , format);
            valid = true;
        } catch (DateTimeParseException e) {
            valid = false;
        }
        return valid;
    }

    public static boolean validateNameInput(String nameStr) {
        boolean valid;
        valid = !nameStr.matches(".*\\d.*");
        return valid;
    }

    public static boolean validateTimeInput(String timeStr) {
        boolean valid;
        if(timeStr.matches("\\d{2}:\\d{2}")){
            String splitTime[] = timeStr.split(":");
            int hour = Integer.parseInt(splitTime[0]);
            int min = Integer.parseInt(splitTime[1]);
            valid = ((hour >= 0 && hour < 24) && (min == 0 || min == 30));
        }
        else{
            valid = false;
        }
        return valid;
    }

    public static boolean validateTimeInput(String timeStr, String schoolStartTime, String schoolEndTime, int duration) {
        boolean valid;
        LocalTime classStartTime = LocalTime.parse(timeStr);
        LocalTime classEndTime = classStartTime.plusHours(duration);
        LocalTime earliestTime = LocalTime.parse(schoolStartTime);
        LocalTime latestTime = LocalTime.parse(schoolEndTime);
        valid = !classStartTime.isBefore(earliestTime) && classStartTime.isBefore(latestTime) &&
                !classEndTime.isAfter(latestTime) && !classEndTime.isBefore(earliestTime);
        return valid;
    }

    public static boolean validateYNInput(String input) {
        boolean valid;
        List<String> yList = List.of("y", "Y", "yes", "Yes");
        List<String> nList = List.of("n", "N", "no", "No");
        valid = yList.contains(input) || nList.contains(input);
        return valid;
    }

    public static boolean validateVenue(String venueInput) {
        boolean match = false;
        for (Venue NTUvenue : Venue.values()) {
            if (venueInput.equals(NTUvenue.toString())) {
                match = true;
            }
        }
        return match;
    }
}