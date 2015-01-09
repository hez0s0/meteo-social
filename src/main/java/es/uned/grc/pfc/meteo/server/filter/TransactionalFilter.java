package es.uned.grc.pfc.meteo.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * A filter that creates a transactional context for the whole request
 */
@WebFilter (urlPatterns = {"/*"})
public class TransactionalFilter implements Filter {

   private static final String EXCLUDE_PATTERS_PARAM = "excludePatterns";

   protected static Logger logger = LoggerFactory.getLogger (TransactionalFilter.class);
   
   private String [] excludePatterns = null;
   
   public void init (FilterConfig filterConfig) throws ServletException {
      this.excludePatterns = StringUtils.split (filterConfig.getInitParameter (EXCLUDE_PATTERS_PARAM), ",");
      if (this.excludePatterns == null) {
         this.excludePatterns = new String [0];
      }
   }
   
   public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      String path = ((HttpServletRequest) request).getServletPath ();
      
      if (matchExcludePatterns(path)) {
         chain.doFilter (request, response);
      } else {
         invokeTransactionally (this, request, response, chain);
      }
   }

   @Transactional
   private void invokeTransactionally (TransactionalFilter transactionalFilter, ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      chain.doFilter (request, response);
   }

   private boolean matchExcludePatterns (String path) {
      for (String excludePattern : excludePatterns) {
         if (path.startsWith (excludePattern)) {
            return true;
         }
      }
      
      return false;
   }

   public void destroy () {
      //do nothing
   }
   
   public void continueChain (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      chain.doFilter (request, response);
   }
}