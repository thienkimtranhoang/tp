package budgetflow.parser;
import budgetflow.exception.MissingDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;

public class DateValidator {
    private static final DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MM-yyyy");

    /**
     * Validates a full date string in the format "dd-MM-yyyy".
     *
     * @param dateStr The date string to validate.
     * @return True if the date is valid, false otherwise.
     */
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, fullDateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates a month-year string in the format "MM-yyyy".
     *
     * @param monthYearStr The month-year string to validate.
     * @return True if the month-year is valid, false otherwise.
     */
    public static boolean isValidMonthYear(String monthYearStr) {
        try {
            LocalDate.parse("01-" + monthYearStr, fullDateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Parses a month-year string into a LocalDate object representing the first day of the month.
     *
     * @param monthYearStr The month-year string in "MM-yyyy" format.
     * @return A LocalDate object representing the first day of the specified month.
     * @throws MissingDateException If the input is not a valid month-year.
     */
    public static LocalDate parseMonthYear(String monthYearStr) throws MissingDateException {
        try {
            return LocalDate.parse("01-" + monthYearStr, fullDateFormatter);
        } catch (DateTimeParseException e) {
            throw new MissingDateException("Invalid month-year format. Please use MM-yyyy.");
        }
    }

    /**
     * Returns the first and last day of the month for a given month-year string.
     *
     * @param monthYearStr The month-year string in "MM-yyyy" format.
     * @return An array of two LocalDate objects: [firstDayOfMonth, lastDayOfMonth].
     */
    public static LocalDate[] getMonthRange(String monthYearStr) throws MissingDateException {
        LocalDate firstDay = parseMonthYear(monthYearStr).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = parseMonthYear(monthYearStr).with(TemporalAdjusters.lastDayOfMonth());
        return new LocalDate[]{firstDay, lastDay};
    }

    /**
     * Provides access to the full date formatter (dd-MM-yyyy).
     *
     * @return The DateTimeFormatter for full dates.
     */
    public static DateTimeFormatter getFullDateFormatter() {
        return fullDateFormatter;
    }
}