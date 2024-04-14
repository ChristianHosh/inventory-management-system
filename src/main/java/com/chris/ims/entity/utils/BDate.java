package com.chris.ims.entity.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressWarnings("unused")
public class BDate {

  private final Calendar calendar = new GregorianCalendar();

  @Getter
  @Setter
  private long time;

  public static BDate currentDate() {
    return new BDate();
  }

  public static BDate of(long time) {
    return new BDate(time);
  }

  public static BDate of(Date date) {
    return new BDate(date.getTime());
  }

  public BDate() {
    this.time = System.currentTimeMillis();
  }

  public BDate(long time) {
    this.time = time;
  }

  public Date toDate() {
    return new Date(this.time);
  }

  public LocalDateTime toLocalDateTime() {
    return LocalDateTime.ofEpochSecond(this.time / 1000, 0, ZoneOffset.of(calendar.getTimeZone().getID()));
  }

  public BDate zeroTime() {
    calendar.setTimeInMillis(this.time);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    setTime(calendar.getTimeInMillis());
    return this;
  }

  public BDate addSecond(int amount) {
    return add(Calendar.SECOND, amount);
  }

  public BDate addMinute(int amount) {
    return add(Calendar.MINUTE, amount);
  }

  public BDate addHour(int amount) {
    return add(Calendar.HOUR_OF_DAY, amount);
  }

  public BDate addDay(int amount) {
    return add(Calendar.DATE, amount);
  }

  public BDate addMonth(int amount) {
    return add(Calendar.MONTH, amount);
  }

  public BDate addYear(int amount) {
    return add(Calendar.YEAR, amount);
  }

  public BDate add(int field, int amount) {
    calendar.setTimeInMillis(this.time);
    calendar.add(field, amount);
    setTime(calendar.getTimeInMillis());
    return this;
  }

  @Override
  public String toString() {
    return this.toLocalDateTime().toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BDate date) {
      return this.time == date.time;
    } else if (obj instanceof Date date) {
      return this.time == date.getTime();
    } else if (obj instanceof LocalDateTime dateTime) {
      return this.time == dateTime.toInstant(ZoneOffset.of(this.calendar.getTimeZone().getID())).toEpochMilli();
    }
    return super.equals(obj);
  }
}
