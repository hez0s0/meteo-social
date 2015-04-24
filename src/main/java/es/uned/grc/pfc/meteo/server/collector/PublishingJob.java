package es.uned.grc.pfc.meteo.server.collector;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import winterwell.jtwitter.Twitter;

import com.ibm.icu.text.SimpleDateFormat;

import es.uned.grc.pfc.meteo.server.model.Observation;
import es.uned.grc.pfc.meteo.server.model.Station;
import es.uned.grc.pfc.meteo.server.model.Variable;
import es.uned.grc.pfc.meteo.server.persistence.IObservationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IParameterPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IStationPersistence;
import es.uned.grc.pfc.meteo.server.persistence.IVariablePersistence;
import es.uned.grc.pfc.meteo.server.util.IServerConstants;

@Component
public class PublishingJob {

   protected static Logger logger = LoggerFactory.getLogger (PublishingJob.class);
   
   private static final String [] VARIABLES_TO_PUBLISH = {IServerConstants.DAY_MINIMUM, IServerConstants.DAY_AVERAGE, IServerConstants.DAY_MAXIMUM,
                                                          IServerConstants.MONTH_MINIMUM, IServerConstants.MONTH_AVERAGE, IServerConstants.MONTH_MAXIMUM};

   private static final String COMMENT_TEMPLATE = "I got %s as average %s in %s (min=%s,max=%s)";
   
   @Autowired
   private IStationPersistence stationPersistence = null;
   @Autowired
   private IParameterPersistence parameterPersistence = null;
   @Autowired
   private IObservationPersistence observationPersistence = null;
   @Autowired
   private IVariablePersistence variablePersistence = null;
   
   private static final String JTWITTER_OAUTH_KEY = "471222984-nM0Yfg6xvTIALv6ORAnsKODKuRD71BWPhimwc33f";
   private static final String JTWITTER_OAUTH_SECRET = "4PMTzMtf8HPJ0usqeZTw4RRzVf2UVjMaOIIqRtnWtzO8v";

   /**
    * To be executed periodically
    */
   @Transactional (propagation = Propagation.REQUIRED)
   @Scheduled (fixedRate = IServerConstants.PUBLISHING_POLLING_TIME)
   public synchronized void timeout () {
      Station station = null;
      int comments = 0;
      List <Variable> variablesToPublish = new ArrayList <Variable> (VARIABLES_TO_PUBLISH.length);
      
      logger.info ("Executing task {}", getClass ().getSimpleName ());
      try {
         synchronized (IJobConstants.STATION_ACCESS) {
            station = stationPersistence.getOwnStation ();
            if (station == null) {
               throw new RuntimeException ("unable to obtain own station, please review the system configuration");
            }
            
            for (String acronym : VARIABLES_TO_PUBLISH) {
               variablesToPublish.add (variablePersistence.getByAcronym (acronym));
            }
            
            comments = generate (station, variablesToPublish, new Date ());

            logger.info ("{} comments published", comments);
         }
      } catch (Exception e) {
         logger.error ("Error publishing comments", e);
      }
   }

   @Transactional (propagation = Propagation.REQUIRED)
   private int generate (Station station, List <Variable> variablesToPublish, Date now) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
      List <Observation> observations = observationPersistence.getUnsent (station.getId (), variablePersistence.getIdList (variablesToPublish));
      Collection <Comment> comments = getComments (observations);

      for (Comment comment : comments) {
         publishComment (comment);
         logger.info ("Successfully updated the status to [" + comment.toString () + "]");
      }
      
      for (Observation observation : observations) {
         observation.setSent (now);
         observationPersistence.merge (observation);
      }

      return comments.size ();
   }
   
   private Collection <Comment> getComments (List <Observation> observations) {
      Comment comment = null;
      Map <String, Comment> result = new HashMap <String, Comment> ();
      
      for (Observation observation : observations) {
         comment = result.get (observation.getDerivedVariable ().getAcronym ());
         if (comment == null) {
            comment = new Comment ();
            comment.variable = observation.getDerivedVariable ().getName ();
         }
         switch (observation.getVariable ().getAcronym ()) {
            case IServerConstants.DAY_MINIMUM:
               comment.minimum = observation.getValue ();
               comment.date = getDayDate (observation.getRangeIni ());
               break;
            case IServerConstants.DAY_AVERAGE:
               comment.average = observation.getValue ();
               comment.date = getDayDate (observation.getRangeIni ());
               break;
            case IServerConstants.DAY_MAXIMUM:
               comment.maximum = observation.getValue ();
               comment.date = getDayDate (observation.getRangeIni ());
               break;
            case IServerConstants.MONTH_MINIMUM:
               comment.minimum = observation.getValue ();
               comment.date = getMonthDate (observation.getRangeIni ());
               break;
            case IServerConstants.MONTH_AVERAGE:
               comment.average = observation.getValue ();
               comment.date = getMonthDate (observation.getRangeIni ());
               break;
            case IServerConstants.MONTH_MAXIMUM:
               comment.maximum = observation.getValue ();
               comment.date = getMonthDate (observation.getRangeIni ());
               break;
         }

         result.put (observation.getDerivedVariable ().getAcronym (), comment);
      }
      
      return result.values ();
   }

   private String getDayDate (Date date) {
      return new SimpleDateFormat ("dd/MM/yy").format (date);
   }

   private String getMonthDate (Date date) {
      return new SimpleDateFormat ("MMM").format (date);
   }

   private void publishComment (Comment comment) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {
//      Map <String, String> configuredParameters = parameterPersistence.asMap (station.getParameters ());

//      Twitter twitter = new Twitter ( "dolorestula", "tula01");
//
//      twitter.setOAuthConsumer ("K9wUgKoXzqSBKZXPa4GV5wyWJ", "bwGGzxhNJ77Lgkr1pdFa70Aei5VyyCI4SN7qIcfHuuQGZ0VO1a");
//      AccessToken accessToken = new AccessToken ("471222984-nM0Yfg6xvTIALv6ORAnsKODKuRD71BWPhimwc33f", "4PMTzMtf8HPJ0usqeZTw4RRzVf2UVjMaOIIqRtnWtzO8v");
//
//      twitter.setOAuthAccessToken (accessToken);

      OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer ("K9wUgKoXzqSBKZXPa4GV5wyWJ", "bwGGzxhNJ77Lgkr1pdFa70Aei5VyyCI4SN7qIcfHuuQGZ0VO1a");
      oAuthConsumer.setTokenWithSecret ("471222984-nM0Yfg6xvTIALv6ORAnsKODKuRD71BWPhimwc33f", "4PMTzMtf8HPJ0usqeZTw4RRzVf2UVjMaOIIqRtnWtzO8v");

      HttpPost httpPost = new HttpPost ("https://api.twitter.com/1.1/statuses/update.json?status=" + URLEncoder.encode (comment.toString ()));

      oAuthConsumer.sign (httpPost);

      HttpClient httpClient = new DefaultHttpClient ();
      HttpResponse httpResponse = httpClient.execute (httpPost);

      int statusCode = httpResponse.getStatusLine ().getStatusCode ();
      logger.info (statusCode + ':' + httpResponse.getStatusLine ().getReasonPhrase ());
      logger.info (IOUtils.toString (httpResponse.getEntity ().getContent ()));
//      httpClient.

   }
   
//   private Api connectTwitter (Station station) {
//      Map <String, String> configuredParameters = parameterPersistence.asMap (station.getParameters ());
//      return Api.builder ()
//                .scheme (Url.Scheme.HTTPS)
//                .username (configuredParameters.get (IServerConstants.TWITTER_USERNAME_PARAM))
//                .password (configuredParameters.get (IServerConstants.TWITTER_PASSWORD_PARAM))
//                .build ();
//   }

   private class Comment {
      private String variable = null;
      private String minimum = null;
      private String average = null;
      private String maximum = null;
      private String date = null;
      
      @Override
      public String toString () {
         return String.format (COMMENT_TEMPLATE, average, variable, date, minimum, maximum);
      }
   }
}