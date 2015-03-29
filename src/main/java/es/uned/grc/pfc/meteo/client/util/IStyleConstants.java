package es.uned.grc.pfc.meteo.client.util;

/**
 * A collection of names of styles used within classes
 */
public interface IStyleConstants {
   /** prefix for all the GWT styles */
   String GWT_PREFIX = "gwt-";
   /** prefix for all the app styles */
   String APP_PREFIX = "meteo-";
   
   /** style to display an informative message */
   String MESSAGE = APP_PREFIX + "message";
   /** style to display a warning message */
   String WARNING = APP_PREFIX + "warning";
   
   /** style for tooltip-like popups */
   String TOOLTIP = APP_PREFIX + "tooltip";

   /** style for a cell within the the synchronization view that displays an outdated element */
   String OUTDATED_CELL = APP_PREFIX + "outdated";
   /** style for a cell with narrow padding */
   String NARROW_CELL = APP_PREFIX + "cell-narrow";
   
   /** style for the list in the token input suggest list box */
   String TOKEN_LIST = APP_PREFIX + "token-input-list";
   /** style for the list in the token input suggest read only mode list box */
   String TOKEN_READ_ONLY_LIST = APP_PREFIX + "token-input-readonly-list";
   
   /** style for the input in the token input suggest list box */
   String TOKEN_INPUT = APP_PREFIX + "token-input-input-token";
   /** style for the suggestBox in the token input suggest list box */
   String TOKEN_SUGGEST = APP_PREFIX + "token-input-suggest-box";
   /** style for an item in the token input suggest list box */
   String TOKEN_TOKEN = APP_PREFIX + "token-input-token";
   /** style for a selected item in the token input suggest list box */
   String TOKEN_TOKEN_SELECTED = APP_PREFIX + "token-input-selected-token";

   /** style for an item in the token input suggest list box */
   String ALERT_TOKEN_TOKEN = APP_PREFIX + "alert-token-input-token";
   
   /** mask for text-indent to be composed */
   String TEXT_INDENT_MASK = "text-indent: %spx";
   /** level of indent used for every paragraph */
   int LEVEL_INDENT = 15;

   /** style for a popup that must be displayed with high z-index */
   String ALERT_DIALOG_BOX = APP_PREFIX + "alert-dialog";   
   /** style for a popup that must be displayed with high z-index */
   String CONFIRM_DIALOG_BOX = APP_PREFIX + "confirm-dialog";   
   /** style for a popup that is wider than usual */
   String VERY_WIDE_DIALOG_BOX = APP_PREFIX + "very-wide-dialog";   
   /** a style for wider dialogs than usual */
   String WIDE_DIALOG_BOX = APP_PREFIX + "wide-dialog";
   /** a style for bigger dialogs than usual */
   String BIG_DIALOG_BOX = APP_PREFIX + "full-dialog";

   /** style for the container of a numberSpinner */
   String SPINNER_CONTAINER = APP_PREFIX + "spinner-container";
   /** style for the box of a numberSpinner */
   String SPINNER_BOX = APP_PREFIX + "spinner-box";
   /** style for the arrow of a numberSpinner */
   String SPINNER_ARROW = APP_PREFIX + "spinner-arrow";

   /** style for the toolbar of a richTextBox */
   String GWT_RICHTTEXTTOOLBAR = GWT_PREFIX + "RichTextToolbar";
   /** style for a richTextBox that has a toolbar */
   String HAS_RICHTEXTTOOLBAR = "hasRichTextToolbar";
   
   /** style to display errors */
   String ERROR_WIDGET = "errorWidget";
   
   /** style for left side of the screen elements in normal width */
   String LEFT_NORMAL = "left";
   /** style for left side of the screen elements in reduced width */
   String LEFT_SMALL = "left_small";
   
   /** style for right side of the screen elements in normal width */
   String RIGHT_NORMAL = "right";
   /** style for right side of the screen elements in reduced width */
   String RIGHT_SMALL = "right_small";
   
   /** style for buttons */
   String BUTTON = "button";
   
   /** style for even rows within tables */
   String EVEN_ROW = APP_PREFIX + "cellTableEvenRowBg";
   /** style for even rows within tables */
   String ODD_ROW = APP_PREFIX + "cellTableOddRowBg";
   
   /** header of a table */
   String TABLE_HEADER = APP_PREFIX + "table-header";
}
