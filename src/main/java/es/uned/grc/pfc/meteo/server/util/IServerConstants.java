package es.uned.grc.pfc.meteo.server.util;


public class IServerConstants {
   public static final String SERVICE_EXCEPTION_KEY = "serviceException";
   
   public static final String VALIDATION_KEY = "validation";

   /** size of buffer to be used on a general basis */
   public static final int BUF_SIZE = 1024 * 1024;
   
   /** convenience variable */
   public static final long ONE_MINUTE = 60 * 1000;
   
   /** time between executions of the collector, in milliseconds */
   public static final long COLLECTION_POLLING_TIME = 60000;
   
   /** time between executions of the quality control, in milliseconds */
   public static final long QUALITY_POLLING_TIME = 60000;

   /** time between executions of the derived calculation job, in milliseconds */
   public static final long DERIVED_POLLING_TIME = 60000;
   
   /** derived variable: minimum in the hour */
   public static final String HOUR_MINIMUM = "HourMin%s";
   /** derived variable: average in the hour */
   public static final String HOUR_AVERAGE = "HourAvg%s";
   /** derived variable: maximum in the hour */
   public static final String HOUR_MAXIMUM = "HourMax%s";
   /** derived variable: minimum in the morning */
   public static final String MORNING_MINIMUM = "MorningMin%s";
   /** derived variable: average in the morning */
   public static final String MORNING_AVERAGE = "MorningAvg%s";
   /** derived variable: maximum in the morning */
   public static final String MORNING_MAXIMUM = "MorningMax%s";
   /** derived variable: minimum in the afternoon */
   public static final String AFTERNOON_MINIMUM = "AfternoonMin%s";
   /** derived variable: average in the afternoon */
   public static final String AFTERNOON_AVERAGE = "AfternoonAvg%s";
   /** derived variable: maximum in the afternoon */
   public static final String AFTERNOON_MAXIMUM = "AfternoonMax%s";
   /** derived variable: minimum in the evening */
   public static final String NIGHT_MINIMUM = "EveningMin%s";
   /** derived variable: average in the evening */
   public static final String NIGHT_AVERAGE = "EveningAvg%s";
   /** derived variable: maximum in the evening */
   public static final String NIGHT_MAXIMUM = "EveningMax%s";
   /** derived variable: minimum in the day */
   public static final String DAY_MINIMUM = "DayMin%s";
   /** derived variable: average in the day */
   public static final String DAY_AVERAGE = "DayAvg%s";
   /** derived variable: maximum in the day */
   public static final String DAY_MAXIMUM = "DayMax%s";
   /** derived variable: minimum in the month */
   public static final String MONTH_MINIMUM = "MonthMin%s";
   /** derived variable: average in the month */
   public static final String MONTH_AVERAGE = "MonthAvg%s";
   /** derived variable: maximum in the month */
   public static final String MONTH_MAXIMUM = "MonthMax%s";
}
