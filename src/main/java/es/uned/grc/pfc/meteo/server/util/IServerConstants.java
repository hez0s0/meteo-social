package es.uned.grc.pfc.meteo.server.util;

public interface IServerConstants {
   String SERVICE_EXCEPTION_KEY = "serviceException";
   
   String VALIDATION_KEY = "validation";

   /** size of buffer to be used on a general basis */
   int BUF_SIZE = 1024 * 1024;
   
   /** convenience variable */
   long ONE_MINUTE = 60 * 1000;
   
   /** time between executions of the collector, in milliseconds */
   long COLLECTION_POLLING_TIME = 60000;
   
   /** time between executions of the quality control, in milliseconds */
   long QUALITY_POLLING_TIME = 60000;

   /** time between executions of the derived calculation job, in milliseconds */
   long DERIVED_POLLING_TIME = 60000;

   /** start hour of the night period */
   int NIGHT_START_HOUR = 0;
   /** end hour of the night period */
   int NIGHT_END_HOUR = 6;
   /** start hour of the morning period */
   int MORNING_START_HOUR = 6;
   /** end hour of the morning period */
   int MORNING_END_HOUR = 12;
   /** start hour of the afternoon period */
   int AFTERNOON_START_HOUR = 12;
   /** end hour of the afternoon period */
   int AFTERNOON_END_HOUR = 18;
   /** start hour of the evening period */
   int EVENING_START_HOUR = 18;
   /** end hour of the evening period */
   int EVENING_END_HOUR = 24;
   
   /** derived variable: minimum in the night */
   String NIGHT_MINIMUM = "NightMin";
   /** derived variable: average in the night */
   String NIGHT_AVERAGE = "NightAvg";
   /** derived variable: maximum in the night */
   String NIGHT_MAXIMUM = "NightMax";
   /** derived variable: minimum in the morning */
   String MORNING_MINIMUM = "MorningMin";
   /** derived variable: average in the morning */
   String MORNING_AVERAGE = "MorningAvg";
   /** derived variable: maximum in the morning */
   String MORNING_MAXIMUM = "MorningMax";
   /** derived variable: minimum in the afternoon */
   String AFTERNOON_MINIMUM = "AfternoonMin";
   /** derived variable: average in the afternoon */
   String AFTERNOON_AVERAGE = "AfternoonAvg";
   /** derived variable: maximum in the afternoon */
   String AFTERNOON_MAXIMUM = "AfternoonMax";
   /** derived variable: minimum in the evening */
   String EVENING_MINIMUM = "EveningMin";
   /** derived variable: average in the evening */
   String EVENING_AVERAGE = "EveningAvg";
   /** derived variable: maximum in the evening */
   String EVENING_MAXIMUM = "EveningMax";
   /** derived variable: minimum in the day */
   String DAY_MINIMUM = "DayMin";
   /** derived variable: average in the day */
   String DAY_AVERAGE = "DayAvg";
   /** derived variable: maximum in the day */
   String DAY_MAXIMUM = "DayMax";
   /** derived variable: minimum in the month */
   String MONTH_MINIMUM = "MonthMin";
   /** derived variable: average in the month */
   String MONTH_AVERAGE = "MonthAvg";
   /** derived variable: maximum in the month */
   String MONTH_MAXIMUM = "MonthMax";
}
