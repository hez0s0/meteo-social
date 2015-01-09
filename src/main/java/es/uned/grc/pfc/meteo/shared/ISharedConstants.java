package es.uned.grc.pfc.meteo.shared;


/**
 * Constants that are shared by both the client and the server.
 */
public class ISharedConstants {
      /** the URL to invoke to perform a logout */
   public static final String LOGOUT_URL = "logout";
   /** the URL of the login page */
   public static final String LOGIN_URL = "login.jsp";
   
   public static final String SHARED_SHORT_DATE_FORMAT = "yyyyMMdd";

   public static final String OPEN_BRACKET = "[";
   public static final String CLOSE_BRACKET = "]";
   
   public static final String VALIDATION_PATH_SEP = ".";
   
   public static final String IMAGES_BASE_PATH = "images/";
   public static final String ICONS_BASE_PATH = IMAGES_BASE_PATH + "icons/";
   
   /** intented to mark an active element in a mask */
   public static final Character ACTIVE = '1';
   /** intented to mark an inactive element in a mask */
   public static final Character INACTIVE = '0';
   
   /** separator in object graph string representations */
   public static final String OBJECT_GRAPH_SEPARATOR = ".";
   /** separator in object graph string representations in RE compatible form (for split, for example) */
   public static final String OBJECT_GRAPH_SEPARATOR_RE = "\\.";
   /** separator for free text expressions that conform a list of words */
   public static final String WORD_LIST_SEPARATOR = ",";
   /** separator for free text words */
   public static final String WORD_SEPARATOR = " ";
   /** separator for free text expressions that conform a list of sentences */
   public static final String SENTENCE_LIST_SEPARATOR = ";";

   public static final String ID_LIST_ALTERNATIVE_SEPARATOR = "|";
   public static final String ID_LIST_ALTERNATIVE_SEPARATOR_RE = "\\|";
   
   /** the url to reach the user image servlet */
   public static final String USER_IMAGE_SERVLET_URL = "%s/userImage";
   
   /** the url to reach an image within flags folder */
   public static final String FLAG_IMAGE_URL = "%s/images/flags/%s.png";
  
   public static final String PARAM_SEP = "&";
   public static final String MULTIPLE_SEP = ";";
   public static final String ENCODED_PARAM_SEP = "!!!";
   
   /** marks a sort field as special for sorting (custom sorting strategy on the persistence class) */
   public static final String SPECIAL_ORDER_MARK ="#";
   
   public static final String DECIMAL_SEPARATOR = ".";
   public static final String DECIMAL_SEPARATOR_RE = "\\.";
}
