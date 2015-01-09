package es.uned.grc.pfc.meteo.client.util;

/**
 * A collection of names of styles used within classes
 */
public interface IStyleConstants {
   /** prefix for all the GWT styles */
   public static final String GWT_PREFIX = "gwt-";
   /** prefix for all the app styles */
   public static final String APP_PREFIX = "meteo-";
   
   /** style to display an informative message */
   public static final String MESSAGE = APP_PREFIX + "message";
   /** style to display a warning message */
   public static final String WARNING = APP_PREFIX + "warning";
   
   /** style to set in a panel that displays a rule test passed correctly */
   public static final String RULE_TEST_OK_PANEL = APP_PREFIX + "rule-ok-panel";
   /** style to set in a panel that displays a rule test did not pass correctly */
   public static final String RULE_TEST_KO_PANEL = APP_PREFIX + "rule-ko-panel";
   
   /** style for tooltip-like popups */
   public static final String TOOLTIP = APP_PREFIX + "tooltip";

   /** style for a cell within the comparator that displays a comment */
   public static final String COMPARE_COMMENTS_CELL = APP_PREFIX + "compare-comments-cell";
   /** style for a cell within the comparator that displays a header */
   public static final String COMPARE_HEADER_MAIN_CELL = APP_PREFIX + "compare-header-main-cell";
   /** style for a cell within the comparator that displays a header */
   public static final String COMPARE_HEADER_CELL = APP_PREFIX + "compare-header-cell";
   /** style for a cell within the comparator that displays a difference */
   public static final String COMPARE_DIFF_CELL = APP_PREFIX + "compare-diff-cell";
   /** style for a cell within the comparator that displays a similarity */
   public static final String COMPARE_EQ_CELL = APP_PREFIX + "compare-eq-cell";

   /** style for a cell within the the synchronization view that displays an outdated element */
   public static final String OUTDATED_CELL = APP_PREFIX + "outdated";
   /** style for a cell with narrow padding */
   public static final String NARROW_CELL = APP_PREFIX + "cell-narrow";
   
   /** style for the list in the token input suggest list box */
   public static final String TOKEN_LIST = APP_PREFIX + "token-input-list";
   /** style for the list in the token input suggest read only mode list box */
   public static final String TOKEN_READ_ONLY_LIST = APP_PREFIX + "token-input-readonly-list";
   
   /** style for the input in the token input suggest list box */
   public static final String TOKEN_INPUT = APP_PREFIX + "token-input-input-token";
   /** style for the suggestBox in the token input suggest list box */
   public static final String TOKEN_SUGGEST = APP_PREFIX + "token-input-suggest-box";
   /** style for an item in the token input suggest list box */
   public static final String TOKEN_TOKEN = APP_PREFIX + "token-input-token";
   /** style for a selected item in the token input suggest list box */
   public static final String TOKEN_TOKEN_SELECTED = APP_PREFIX + "token-input-selected-token";

   /** style for an item in the token input suggest list box */
   public static final String ALERT_TOKEN_TOKEN = APP_PREFIX + "alert-token-input-token";
   
   /** mask for text-indent to be composed */
   public static final String TEXT_INDENT_MASK = "text-indent: %spx";
   /** level of indent used for every paragraph */
   public static final int LEVEL_INDENT = 15;

   /** style for a popup that must be displayed with high z-index */
   public static final String ALERT_DIALOG_BOX = APP_PREFIX + "alert-dialog";   
   /** style for a popup that must be displayed with high z-index */
   public static final String CONFIRM_DIALOG_BOX = APP_PREFIX + "confirm-dialog";   
   /** style for a popup that is wider than usual */
   public static final String VERY_WIDE_DIALOG_BOX = APP_PREFIX + "very-wide-dialog";   
   /** a style for wider dialogs than usual */
   public static final String WIDE_DIALOG_BOX = APP_PREFIX + "wide-dialog";
   /** a style for bigger dialogs than usual */
   public static final String BIG_DIALOG_BOX = APP_PREFIX + "full-dialog";

   /** style to mark indications which are in OPEN state */
   public static final String INDICATION_OPEN = APP_PREFIX + "indication-open";
   /** style to mark indications which are in LOCKED state */
   public static final String INDICATION_LOCKED = APP_PREFIX + "indication-locked";

   /** style for the container of a numberSpinner */
   public static final String SPINNER_CONTAINER = APP_PREFIX + "spinner-container";
   /** style for the box of a numberSpinner */
   public static final String SPINNER_BOX = APP_PREFIX + "spinner-box";
   /** style for the arrow of a numberSpinner */
   public static final String SPINNER_ARROW = APP_PREFIX + "spinner-arrow";

   /** style for the toolbar of a richTextBox */
   public static final String GWT_RICHTTEXTTOOLBAR = GWT_PREFIX + "RichTextToolbar";
   /** style for a richTextBox that has a toolbar */
   public static final String HAS_RICHTEXTTOOLBAR = "hasRichTextToolbar";
   
   /** style to display errors */
   public static final String ERROR_WIDGET = "errorWidget";
   
   /** style for left side of the screen elements in normal width */
   public static final String LEFT_NORMAL = "left";
   /** style for left side of the screen elements in reduced width */
   public static final String LEFT_SMALL = "left_small";
   
   /** style for right side of the screen elements in normal width */
   public static final String RIGHT_NORMAL = "right";
   /** style for right side of the screen elements in reduced width */
   public static final String RIGHT_SMALL = "right_small";
   
   /** style for buttons */
   public static final String BUTTON = "button";
   
   /** style for even rows within tables */
   public static final String EVEN_ROW = APP_PREFIX + "cellTableEvenRowBg";
   /** style for even rows within tables */
   public static final String ODD_ROW = APP_PREFIX + "cellTableOddRowBg";
} //end of IStyleConstants
