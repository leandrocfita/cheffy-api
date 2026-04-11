package br.com.fiap.cheffy.domain.restaurant.valueobject;

import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class WorkingHours {

    private static final long MINIMUM_WORKING_HOURS = 1;
    private static final int SECONDS_IN_DAY = 24 * 60 * 60;

    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final boolean open24Hours;

    private WorkingHours(
            LocalTime openingTime,
            LocalTime closingTime,
            boolean open24Hours,
            boolean skippValidation
    ) {

        if (!skippValidation) {
            validateWorkingTime(openingTime, closingTime, open24Hours);
        }

        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.open24Hours = open24Hours;
    }

    private void validateWorkingTime(LocalTime openingTime, LocalTime closingTime, boolean open24Hours) {
        if (open24Hours) {
            if (openingTime != null || closingTime != null) {
                throw new UserOperationNotAllowedException(
                        AROUND_THE_CLOCK_RESTAURANTS_SHOULDNT_DECLARE_WORKING_HOURS
                );
            }
        } else {
            if (openingTime == null || closingTime == null) {
                throw new UserOperationNotAllowedException(
                        RESTAURANT_INVALID_WORKING_TIME
                );
            }

            if (openingTime.equals(closingTime)) {
                throw new UserOperationNotAllowedException(
                        RESTAURANT_INVALID_WORKING_TIME
                );
            }

            validateWorkingHoursDuration(openingTime, closingTime);
        }
    }

    public static WorkingHours reconstitute(
            LocalTime opening,
            LocalTime closing,
            boolean open24hours
    ) {
        return new WorkingHours(opening, closing, open24hours, true);
    }

    public static WorkingHours open24Hours() {
        return new WorkingHours(null, null, true, false);
    }

    public static WorkingHours of(LocalTime opening, LocalTime closing) {
        return new WorkingHours(opening, closing, false, false);
    }

    private void validateWorkingHoursDuration(LocalTime openingTime, LocalTime closingTime) {
        int openingSeconds = openingTime.toSecondOfDay();
        int closingSeconds = closingTime.toSecondOfDay();
        int durationSeconds = (closingSeconds - openingSeconds + SECONDS_IN_DAY) % SECONDS_IN_DAY;

        if (durationSeconds == 0 || Duration.ofSeconds(durationSeconds).toHours() < MINIMUM_WORKING_HOURS) {
            throw new UserOperationNotAllowedException(WORKING_TIME_TOO_SHORT);
        }
    }

    public boolean isOpenAt(LocalTime localTime) {

        if (open24Hours) {
            return true;
        }

        int openingSeconds = openingTime.toSecondOfDay();
        int closingSeconds = closingTime.toSecondOfDay();
        int currentSeconds = localTime.toSecondOfDay();

        // Caso normal (ex: 10:00 - 22:00)
        if (openingSeconds < closingSeconds) {
            return currentSeconds >= openingSeconds &&
                    currentSeconds < closingSeconds;
        }

        // Caso que cruza meia-noite (ex: 18:00 - 02:00)
        return currentSeconds >= openingSeconds ||
                currentSeconds < closingSeconds;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public boolean isOpen24Hours() {
        return open24Hours;
    }
}
