package com.knoema;

import java.util.*;

public class TimeFormat {
    public static final TimeFormat INVARIANT_TIME_FORMAT = new TimeFormat(Locale.ROOT);

    private final Calendar localeCalendar;

    public TimeFormat(Locale locale) {
        localeCalendar = Calendar.getInstance(locale);
    }

    public Calendar getInstance() {
        return (Calendar)localeCalendar.clone();
    }

    public Calendar getCalendarDate(Date value) {
        Calendar result = getInstance();
        result.setTime(value);
        return result;
    }

    @SuppressWarnings("MagicConstant")
    public List<Date> expandRangeSelection(Date startDate, Date endDate, Frequency frequency) {
        ArrayList<Date> result = new ArrayList<>();

        Calendar endCalendar = getCalendarDate(endDate);
        Calendar calendar = endCalendar;
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);

        calendar = getCalendarDate(startDate);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Date lastWeekDate = new Date(0);
        for (; ; ) {

            switch (frequency) {
                case Annual:
                    if (month % 12 == Calendar.JANUARY && day == 1) {
                        calendar.set(year, month, 1, 0, 0, 0);
                        result.add(calendar.getTime());
                    }
                    break;
                case SemiAnnual:
                    if (month % 6 == Calendar.JANUARY && day == 1) {
                        calendar.set(year, month, 1, 0, 0, 0);
                        result.add(calendar.getTime());
                    }
                    break;
                case Quarterly:
                    if (month % 3 == Calendar.JANUARY && day == 1) {
                        calendar.set(year, month, 1, 0, 0, 0);
                        result.add(calendar.getTime());
                    }
                    break;
                case Monthly:
                    if (day == 1) {
                        calendar.set(year, month, 1, 0, 0, 0);
                        result.add(calendar.getTime());
                    }
                    break;
                case Weekly: {
                    //Find num of weeks in that month
                    int numberOfDays;
                    if (year == endYear && month == endMonth) {
                        numberOfDays = endCalendar.get(Calendar.DAY_OF_MONTH);
                    } else {
                        calendar.set(year, month, 1, 0, 0, 0);
                        numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    }
                    int firstDayOfWeek = Calendar.MONDAY;
                    int curDay = day;
                    while (curDay <= numberOfDays) {
                        calendar.set(year, month, curDay, 0, 0, 0);
                        if (calendar.get(Calendar.DAY_OF_WEEK) == firstDayOfWeek)
                            break;
                        curDay++;
                    }
                    while (curDay <= numberOfDays) {
                        calendar.set(year, month, curDay, 0, 0, 0);
                        Date weekDate = calendar.getTime();
                        if (!weekDate.equals(lastWeekDate)) {
                            lastWeekDate = weekDate;
                            result.add(weekDate);
                        }
                        curDay += 7;
                    }
                }
                break;
                case Daily: {
                    int numberOfDays;
                    if (year == endYear && month == endMonth) {
                        numberOfDays = endCalendar.get(Calendar.DAY_OF_MONTH);
                    } else {
                        calendar.set(year, month, 1, 0, 0, 0);
                        numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    }
                    for (int i = day; i <= numberOfDays; i++) {
                        calendar.set(year, month, i, 0, 0, 0);
                        result.add(calendar.getTime());
                    }
                }
                break;
            }

            if (year > endYear || (year == endYear && month >= endMonth))
                break;
            day = 1;
            if (month < Calendar.DECEMBER) {
                month++;
            }
            else {
                year++;
                month = Calendar.JANUARY;
            }
        }
        return result;
    }
}
