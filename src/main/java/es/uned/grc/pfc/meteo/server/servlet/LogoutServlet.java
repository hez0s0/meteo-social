package es.uned.grc.pfc.meteo.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet (name = "logoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

   @Override
   public void service (ServletRequest req, ServletResponse res) throws IOException, ServletException {
      System.out.println ("PLEASE LOG OUT !");
   }
}
