package es.uned.grc.pfc.meteo.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface IApplicationResources extends ClientBundle {
   /** image for an add button */
   @Source ("img/add.png")
   ImageResource add ();
   /** image for a delete button */
   @Source ("img/delete.png")
   ImageResource delete ();
   
   /** image for generic user to be displayed on the header */
   @Source ("img/user.jpg")
   ImageResource unknownUser ();

   // next come the images for NumberSpinner
   @Source ("img/arrow_sans_up_8.png")
   ImageResource upBullet ();
   @Source ("img/arrow_sans_down_8.png")
   ImageResource downBullet ();
   
   // next come the images for ButtonPanel
   @Source ("img/cancel.png")
   ImageResource close ();
   @Source ("img/arrow_up.png")
   ImageResource up ();
   @Source ("img/arrow_down.png")
   ImageResource down ();
   
   @Source ("img/table_relationship.png")
   ImageResource compareVersions ();
   @Source ("img/table_row_delete.png")
   ImageResource deleteVersion ();
   @Source ("img/table_row_insert.png")
   ImageResource undeleteVersion ();
   @Source ("img/server_go.png")
   ImageResource syncVersion ();
   @Source ("img/page_key.png")
   ImageResource expropriateVersion ();
   @Source ("img/page_white_put.png")
   ImageResource restoreVersion ();
   
   @Source ("img/page_edit.png")
   ImageResource edit ();
   @Source ("img/disk.png")
   ImageResource save ();
   @Source ("img/page_white_delete.png")
   ImageResource discard ();
   @Source ("img/page_white_go.png")
   ImageResource commit ();
   @Source ("img/lock.png")
   ImageResource approve ();
   @Source ("img/lock_go.png")
   ImageResource deny ();
   @Source ("img/table.png")
   ImageResource showVersions ();
   
   // images to represent types of tasks
   @Source ("img/eye.png")
   ImageResource proposal ();
   @Source ("img/page_red.png")
   ImageResource refusal ();
   
   // images to represent drug item LifeStatus
   @Source ("img/chain-link-icon.png")
   ImageResource drugSynchedOpen ();
   @Source ("img/chain-unlinked-icon.png")
   ImageResource drugUnsynchedOpen ();
   @Source ("img/lock.png")
   ImageResource drugLocked ();
   @Source ("img/lock_wide_open.png")
   ImageResource drugLockedByMe ();
   @Source ("img/delete.png")
   ImageResource drugDelete ();
   
   // images to represent syncStatus
   @Source ("img/chain-link-icon.png")
   ImageResource drugSynched ();
   @Source ("img/chain-unlinked-icon.png")
   ImageResource drugUnsynched ();
   @Source ("img/brick_error.png")
   ImageResource drugUnsynchedIncomplete ();
   
   // next come the images for RichTextToobar
   @Source ("img/bold.gif")
   ImageResource bold ();
   @Source ("img/createLink.gif")
   ImageResource createLink ();
   @Source ("img/hr.gif")
   ImageResource hr ();
   @Source ("img/indent.gif")
   ImageResource indent ();
   @Source ("img/insertImage.gif")
   ImageResource insertImage ();
   @Source ("img/italic.gif")
   ImageResource italic ();
   @Source ("img/justifyCenter.gif")
   ImageResource justifyCenter ();
   @Source ("img/justifyLeft.gif")
   ImageResource justifyLeft ();
   @Source ("img/justifyRight.gif")
   ImageResource justifyRight ();
   @Source ("img/ol.gif")
   ImageResource ol ();
   @Source ("img/outdent.gif")
   ImageResource outdent ();
   @Source ("img/removeFormat.gif")
   ImageResource removeFormat ();
   @Source ("img/removeLink.gif")
   ImageResource removeLink ();
   @Source ("img/strikeThrough.gif")
   ImageResource strikeThrough ();
   @Source ("img/subscript.gif")
   ImageResource subscript ();
   @Source ("img/superscript.gif")
   ImageResource superscript ();
   @Source ("img/ul.gif")
   ImageResource ul ();
   @Source ("img/underline.gif")
   ImageResource underline ();

   /** image to represent an information message */
   @Source ("img/information.png")
   ImageResource information ();
}
