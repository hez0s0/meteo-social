package es.uned.grc.pfc.meteo.client.activity;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.web.bindery.event.shared.EventBus;

public class CustomActivityManager extends ActivityManager {
   
   private ActivityMapper mapper = null;
   
   public CustomActivityManager (ActivityMapper mapper, EventBus eventBus) {
      super (mapper, eventBus);

      this.mapper = mapper;
   }

   public ActivityMapper getMapper () {
      return mapper;
   }

   public void setMapper (ActivityMapper mapper) {
      this.mapper = mapper;
   }
}
