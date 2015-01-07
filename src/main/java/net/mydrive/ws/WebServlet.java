 
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

public class WebServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println(request);
        response.sendRedirect("index.html");
    }
}