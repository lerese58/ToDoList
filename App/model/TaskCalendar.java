package App.model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

public class TaskCalendar implements Comparable<TaskCalendar> {
    private LocalDateTime _deadline;
    private int _minutesToDeadline;
    private int _hoursToDeadline;
    private int _daysToDeadline;
    private int _monthsToDeadline;
    private int _yearsToDeadline;

    public TaskCalendar(LocalDateTime dateTime) {
        _deadline = dateTime;
        calculateTimeLeft();
    }

    public TaskCalendar() {
        _deadline = LocalDateTime.now();
    }

    public TaskCalendar(int year, int month, int day, int hour, int minute) {
        _deadline = LocalDateTime.of(year, month, day, hour, minute);
        calculateTimeLeft();
    }

    public TaskCalendar(String source) {
        int h = Integer.parseInt(source.substring(0, 2));
        int mins = Integer.parseInt(source.substring(3, 5));
        int d = Integer.parseInt(source.substring(6, 8));
        int m = Integer.parseInt(source.substring(9, 11));
        int y = Integer.parseInt(source.substring(12, 16));
        _deadline = LocalDateTime.of(y, m, d, h, mins);
        calculateTimeLeft();
    }

    public LocalDateTime getDateTime() {
        return _deadline;
    }

    private void calculateTimeLeft() {
        LocalDateTime _time = LocalDateTime.now();
        _yearsToDeadline = _deadline.getYear() - _time.getYear();
        _monthsToDeadline = _deadline.getMonthValue() - _time.getMonthValue();
        if (_monthsToDeadline < 0) {
            _monthsToDeadline += 12;
            _yearsToDeadline--;
        }
        _daysToDeadline = _deadline.getDayOfMonth() - _time.getDayOfMonth();
        if (_daysToDeadline < 0) {
            _daysToDeadline += Month.of(_time.getMonthValue()).length(_time.toLocalDate().isLeapYear());
            _monthsToDeadline--;
        }
        _hoursToDeadline = _deadline.getHour() - _time.getHour();
        if (_hoursToDeadline < 0) {
            _hoursToDeadline += 24;
            _daysToDeadline--;
        }
        _minutesToDeadline = _deadline.getMinute() - _time.getMinute();
        if (_minutesToDeadline < 0) {
            _minutesToDeadline += 60;
            _hoursToDeadline--;
        }
    }

    public int getMinutesToDeadline() {
        calculateTimeLeft();
        return _minutesToDeadline;
    }

    public int getHoursToDeadline() {
        calculateTimeLeft();
        return _hoursToDeadline;
    }

    public int getDaysToDeadline() {
        calculateTimeLeft();
        return _daysToDeadline;
    }

    public int getMonthsToDeadline() {
        calculateTimeLeft();
        return _monthsToDeadline;
    }

    public int getYearsToDeadline() {
        calculateTimeLeft();
        return _yearsToDeadline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskCalendar that = (TaskCalendar) o;
        return Objects.equals(_deadline, that._deadline);
    }

    @Override
    public int hashCode() {
        return ((Integer) _deadline.getMinute()).hashCode() +
                ((Integer) _deadline.getHour()).hashCode() +
                ((Integer) _deadline.getDayOfMonth()).hashCode() +
                ((Integer) _deadline.getMonthValue()).hashCode() +
                ((Integer) _deadline.getYear()).hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (_deadline.getHour() < 10)
            sb.append("0");
        sb.append(_deadline.getHour());
        sb.append(":");
        if (_deadline.getMinute() < 10)
            sb.append("0");
        sb.append(_deadline.getMinute());
        sb.append(" ");
        if (_deadline.getDayOfMonth() < 10)
            sb.append("0");
        sb.append(_deadline.getDayOfMonth());
        sb.append(".");
        if (_deadline.getMonthValue() < 10)
            sb.append("0");
        sb.append(_deadline.getMonthValue());
        sb.append(".");
        sb.append(this._deadline.getYear());
        return sb.toString();
    }

    @Override
    public int compareTo(TaskCalendar o) {
        int result = Integer.compare(this.getYearsToDeadline(), o.getYearsToDeadline());
        if (result == 0) {
            result = Integer.compare(this.getMonthsToDeadline(), o.getMonthsToDeadline());
            if (result == 0) {
                result = Integer.compare(this.getDaysToDeadline(), o.getDaysToDeadline());
                if (result == 0) {
                    result = Integer.compare(this.getHoursToDeadline(), o.getHoursToDeadline());
                    if (result == 0) {
                        return Integer.compare(this.getMinutesToDeadline(), o.getMinutesToDeadline());
                    }
                }
            }
        }
        return result;
    }
}
