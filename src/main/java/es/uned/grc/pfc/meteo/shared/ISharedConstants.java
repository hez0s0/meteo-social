package es.uned.grc.pfc.meteo.shared;

/**
 * Constants that are shared by both the client and the server.
 */
public interface ISharedConstants {
   
   /** one day in milliseconds */
   long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;
   
   /** the URL to invoke to perform a logout */
   String LOGOUT_URL = "%s/logout";
   /** the URL of the login page */
   String LOGIN_URL = "login.jsp";

   String SHARED_FULL_DATE_FORMAT = "yyyyMMddHHmmss";
   String SHARED_SHORT_DATE_FORMAT = "yyyyMMdd";

   String OPEN_BRACKET = "[";
   String CLOSE_BRACKET = "]";
   
   String VALIDATION_PATH_SEP = ".";
   
   String IMAGES_BASE_PATH = "images/";
   String ICONS_BASE_PATH = IMAGES_BASE_PATH + "icons/";
   
   /** intented to mark an active element in a mask */
   Character ACTIVE = '1';
   /** intented to mark an inactive element in a mask */
   Character INACTIVE = '0';
   
   /** separator in object graph string representations */
   String OBJECT_GRAPH_SEPARATOR = ".";
   /** separator in object graph string representations in RE compatible form (for split, for example) */
   String OBJECT_GRAPH_SEPARATOR_RE = "\\.";
   /** separator for free text expressions that conform a list of words */
   String WORD_LIST_SEPARATOR = ",";
   /** separator for free text words */
   String WORD_SEPARATOR = " ";
   /** separator for free text expressions that conform a list of sentences */
   String SENTENCE_LIST_SEPARATOR = ";";

   String ID_LIST_ALTERNATIVE_SEPARATOR = "|";
   String ID_LIST_ALTERNATIVE_SEPARATOR_RE = "\\|";
   
   /** the url to reach the user image servlet */
   String USER_IMAGE_SERVLET_URL = "%s/userImage";
   
   /** the url to reach an image within flags folder */
   String FLAG_IMAGE_URL = "%s/images/flags/%s.png";

   String PARAM_VALUE_SEP = "=";
   String PARAM_SEP = "&";
   String MULTIPLE_SEP = ";";
   String ENCODED_PARAM_SEP = "!!!";
   
   /** marks a sort field as special for sorting (custom sorting strategy on the persistence class) */
   String SPECIAL_ORDER_MARK ="#";
   
   String DECIMAL_SEPARATOR = ".";
   String DECIMAL_SEPARATOR_RE = "\\.";
   
   /** the filters that can be applied over stations */
   enum StationFilter {OWN, NAME, COUNTRY, CITY, ZIP, LAT, LON, RADIUS};
   
   /** the filters that can be applied over observations */
   enum ObservationFilter {OWN, STATION_ID, START_DATE, END_DATE, VARIABLE_IDS, MEASURED_ONLY, DERIVED_ONLY};
   
   /** type of graphic to represent a variable */
   enum GraphType {NONE, LINE, BAR, AREA, COLUMN};
   
   /** the type of derived variables that we want to seek, when searching for a range */
   enum DerivedRangeType {MONTH, DAY, NIGHT, MORNING, AFTERNOON, EVENING};
}
