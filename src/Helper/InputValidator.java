package Helper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean indexStrMatcher(String indexInput) {
        boolean valid;
        valid = indexInput.matches("\\d{6}");
        return valid;
    }

    public static boolean validateDateTimeInput(String dateTimeStr) {
        boolean valid;
        DateTimeFormatter format = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm")
                .parseDefaulting(ChronoField.ERA, 1)
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT); //edited for 30/31 days checking + negative years
        try {
            LocalDateTime.parse(dateTimeStr, format);
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
}