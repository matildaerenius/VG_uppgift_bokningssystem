package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeFrame {
    //man får deleta tiden om man vill ändra
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final LocalDate date;

    public TimeFrame(String date, String startTime, String endTime) {
        this.date = LocalDate.parse(date);
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return date + ", " + startTime + "-" + endTime;
    }
}
