package es.uned.grc.pfc.meteo.client.util;

import java.util.Date;

import com.ibm.icu.util.Calendar;

import es.uned.grc.pfc.meteo.server.util.IServerConstants;

public class DerivedUtils {

   private DerivedUtils () {
      
   }
   
   public static final Date getHourIni (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.MINUTE, 0);
      calendar.set (Calendar.SECOND, 0);
      return calendar.getTime ();
   }

   public static final Date getHourEnd (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.MINUTE, 59);
      calendar.set (Calendar.SECOND, 59);
      return calendar.getTime ();
   }

   public static final Date getEveningIni (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, IServerConstants.NIGHT_START_HOUR);
      calendar.set (Calendar.MINUTE, 0);
      calendar.set (Calendar.SECOND, 0);
      return calendar.getTime ();
   }

   public static final Date getEveningEnd (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, IServerConstants.NIGHT_END_HOUR - 1);
      calendar.set (Calendar.MINUTE, 59);
      calendar.set (Calendar.SECOND, 59);
      return calendar.getTime ();
   }

   public static final Date getMorningIni (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, IServerConstants.MORNING_START_HOUR);
      calendar.set (Calendar.MINUTE, 0);
      calendar.set (Calendar.SECOND, 0);
      return calendar.getTime ();
   }

   public static final Date getMorningEnd (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, IServerConstants.MORNING_END_HOUR - 1);
      calendar.set (Calendar.MINUTE, 59);
      calendar.set (Calendar.SECOND, 59);
      return calendar.getTime ();
   }

   public static final Date getAfternoonIni (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, IServerConstants.AFTERNOON_START_HOUR);
      calendar.set (Calendar.MINUTE, 0);
      calendar.set (Calendar.SECOND, 0);
      return calendar.getTime ();
   }

   public static final Date getAfternoonEnd (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, IServerConstants.AFTERNOON_END_HOUR - 1);
      calendar.set (Calendar.MINUTE, 59);
      calendar.set (Calendar.SECOND, 59);
      return calendar.getTime ();
   }

   public static final Date getNightIni (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, IServerConstants.NIGHT_START_HOUR);
      calendar.set (Calendar.MINUTE, 0);
      calendar.set (Calendar.SECOND, 0);
      return calendar.getTime ();
   }

   public static final Date getNightEnd (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, IServerConstants.NIGHT_END_HOUR - 1);
      calendar.set (Calendar.MINUTE, 59);
      calendar.set (Calendar.SECOND, 59);
      return calendar.getTime ();
   }

   public static final Date getDayIni (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, 0);
      calendar.set (Calendar.MINUTE, 0);
      calendar.set (Calendar.SECOND, 0);
      return calendar.getTime ();
   }

   public static final Date getDayEnd (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.HOUR_OF_DAY, 23);
      calendar.set (Calendar.MINUTE, 59);
      calendar.set (Calendar.SECOND, 59);
      return calendar.getTime ();
   }

   public static final Date getMonthIni (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.DAY_OF_MONTH, 1);
      calendar.set (Calendar.HOUR_OF_DAY, 0);
      calendar.set (Calendar.MINUTE, 0);
      calendar.set (Calendar.SECOND, 0);
      return calendar.getTime ();
   }

   public static final Date getMonthEnd (Date observed) {
      Calendar calendar = Calendar.getInstance ();
      calendar.setTime (observed);
      calendar.set (Calendar.DAY_OF_MONTH, calendar.getActualMaximum (Calendar.DAY_OF_MONTH));
      calendar.set (Calendar.HOUR_OF_DAY, 23);
      calendar.set (Calendar.MINUTE, 59);
      calendar.set (Calendar.SECOND, 59);
      return calendar.getTime ();
   }
}
