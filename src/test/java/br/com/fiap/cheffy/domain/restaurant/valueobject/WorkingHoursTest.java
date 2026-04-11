package br.com.fiap.cheffy.domain.restaurant.valueobject;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalTime;
import java.time.OffsetTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class WorkingHoursTest {

    @Test
    void shouldReconstituteWorkingHours() {

        WorkingHours wh = WorkingHours.reconstitute( LocalTime.parse("09:00:00"),
                LocalTime.parse("18:00:00"),
                false);

        assertEquals(LocalTime.parse("09:00"), wh.getOpeningTime());
        assertEquals(LocalTime.parse("18:00"), wh.getClosingTime());
        assertEquals(false, wh.isOpen24Hours());
    }

    @Test
    void shouldThrowWhenOpeningEqualsClosing() {
        LocalTime time = LocalTime.parse("09:00");

        assertThrows(UserOperationNotAllowedException.class, () ->
                WorkingHours.of(time, time)
        );
    }

    @Test
    void shouldAllowCrossingMidnight() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.parse("22:00"),
                LocalTime.parse("06:00")
        );

        assertEquals(
                LocalTime.parse("22:00"),
                wh.getOpeningTime()
        );
    }

    @Test
    void shouldReconstituteEvenIfInvalid() {
        LocalTime time = LocalTime.parse("09:00");

        WorkingHours wh = WorkingHours.reconstitute(time, time, false);

        assertEquals(time, wh.getOpeningTime());
    }

    @Test
    void shouldReturnTrueWhenInsideWorkingHours() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00")
        );

        assertTrue(wh.isOpenAt(LocalTime.parse("10:00")));
    }

    @Test
    void shouldReturnFalseWhenOutsideWorkingHours() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00")
        );

        assertFalse(wh.isOpenAt(LocalTime.parse("08:59")));
        assertFalse(wh.isOpenAt(LocalTime.parse("18:00")));
    }

    @Test
    void shouldHandleCrossingMidnightCorrectly() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.parse("22:00"),
                LocalTime.parse("06:00")
        );

        assertTrue(wh.isOpenAt(LocalTime.parse("23:00")));
        assertTrue(wh.isOpenAt(LocalTime.parse("02:00")));
        assertFalse(wh.isOpenAt(LocalTime.parse("21:59")));
        assertFalse(wh.isOpenAt(LocalTime.parse("06:00")));
    }

    @Test
    void shouldAlwaysReturnTrueWhenOpen24Hours() {
        WorkingHours wh = WorkingHours.open24Hours();

        assertTrue(wh.isOpenAt(LocalTime.parse("00:00")));
        assertTrue(wh.isOpenAt(LocalTime.parse("12:00")));
        assertTrue(wh.isOpenAt(LocalTime.parse("23:59")));
    }

    @Test
    void shouldThrowWhenOpen24HoursWithWorkingHoursDeclared() {
        assertThrows(UserOperationNotAllowedException.class, () ->
                WorkingHours.of(null, null)
        );
    }

    @Test
    void shouldThrowWhenNullOpeningOrClosingTime() {
        assertThrows(UserOperationNotAllowedException.class, () ->
                WorkingHours.of(null, LocalTime.parse("18:00"))
        );
    }

    @Test
    void shouldIncludeOpeningTimeAndExcludeClosingTime() {
        WorkingHours wh = WorkingHours.of(
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00")
        );

        assertTrue(wh.isOpenAt(LocalTime.parse("09:00")));
        assertFalse(wh.isOpenAt(LocalTime.parse("18:00")));
    }

    @Test
    void shouldThrowWhenOpen24HoursButTimesProvided() throws Exception {
        var constructor = WorkingHours.class.getDeclaredConstructor(
                LocalTime.class, LocalTime.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        assertThrows(java.lang.reflect.InvocationTargetException.class, () ->
                constructor.newInstance(LocalTime.parse("09:00"), LocalTime.parse("18:00"), true, false)
        );
    }
}