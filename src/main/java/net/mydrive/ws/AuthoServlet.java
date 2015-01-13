 
/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mydrive.ws;

/**
 *
 * @author guillaumerebmann
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AuthoServlet extends MyBaseServlet {
	 /**
            *
            * @param request
            * @param response
            * @throws ServletException
            * @throws IOException
            */
           @Override
           public void doGet(HttpServletRequest request,
                             HttpServletResponse response)
               throws ServletException, IOException {
              // System.out.println("1 "+request.getRequestURI());

               

              

               System.out.println("Autho-Servlet: "+request.getRequestURI());


               if(request.getRequestURI().equals("/oauth2/callback")){
                  handleCallbackIfRequired(request, response);
               }else if(request.getRequestURI().equals("/oauth2/login")){
                  loginIfRequired(request, response);
               }
           }
}