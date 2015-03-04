/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mydrive.ws;

/**
 *
 * @author guillaumerebmann, nguyenquanganh
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class WebServlet extends HttpServlet {

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// System.out.println("1 "+request.getRequestURI());

		// handleCallbackIfRequired(request, response);

		// loginIfRequired(request, response);

		// System.out.println("2" + request.getServletPath() + "2");

		System.out.println("2" + request.getServletPath() + "2");

		if (request.getSession().getAttribute("user_id") == null) {
			// response.sendRedirect("/login");
			request.getRequestDispatcher("login.html").forward(request,
					response);
		} else {
			if (request.getServletPath().equals("/index")) {
				request.getRequestDispatcher("index.html").forward(request,
						response);
			} else {
				request.getRequestDispatcher("index.html").forward(request,
						response);
			}

		}
	}
}