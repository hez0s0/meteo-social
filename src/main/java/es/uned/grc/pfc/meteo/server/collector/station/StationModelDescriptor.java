package es.uned.grc.pfc.meteo.server.collector.station;

public class StationModelDescriptor {
   private String name = null;
   private String description = null;
   
   public String getName () {
      return name;
   }
   public void setName (String name) {
      this.name = name;
   }
   public String getDescription () {
      return description;
   }
   public void setDescription (String description) {
      this.description = description;
   }
}
