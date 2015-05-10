<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" contentType="text/html; charset=iso-8859-1" pageEncoding="utf-8"%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%
  javax.servlet.http.HttpSession tempSession = request.getSession (false);
  if (tempSession != null)
     tempSession.invalidate ();
%>
    
<html>
<head>
<title>meteo-social</title>
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="css/login.css" />
</head>
<body onload="document.f.j_username.focus();">
   <div class="container">
      <header>
         <h1>meteo <span>social</span></h1>
      </header>
      <section>
         <div id="container_demo">
            <div id="wrapper">
               <div id="login" class="form">
                  <form name="f" action="<c:url value='j_spring_security_check'/>" autocomplete="on" method="post">
                     <h1>Autenticación</h1>
                     <c:if test="${not empty param.login_error}">
				        <div class="error">
				           Usuario y/o contraseña no válidos
				        </div>
				     </c:if>
                     <p>
								<label for="username" class="uname"> Usuario </label>
                        <input id="username" name="j_username" required="required" type="text" placeholder="introduzca su usuario" />
                     </p>
                     <p>
                        <label for="password" class="youpasswd"> Contraseña </label>
                        <input id="password" name="j_password" required="required" type="password" placeholder="p.e.: X0Bz23a1" />
                     </p>
                     <p class="keeplogin"></p>
                     <p class="login button">
                        <input type="submit" value="Entrar" />
                     </p>
                  </form>
               </div>
            </div>
         </div>
      </section>
   </div>
</body>
</html>