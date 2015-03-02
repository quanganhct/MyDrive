package net.mydrive.ws;

/**
 * 
 * @author nguyenquanganh
 * 
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public abstract class MyBaseServlet extends HttpServlet {
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	protected static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final String KEY_SESSION_USERID = "user_id";
	private static final String DEFAULT_MIMETYPE = "/test/plain";
	public static final String CLIENT_SECRETS_FILE_PATH = "/WEB-INF/client_secrets.json";

	protected CredentialManager credentialManager = null;
	protected CredentialManager2 credentialManager2 = null;

	@Override
	public void init() throws ServletException {
		super.init();
		credentialManager = new CredentialManager(getClientSecret(), TRANSPORT,
				JSON_FACTORY);

		credentialManager2 = new CredentialManager2(getClientSecret(),
				TRANSPORT, JSON_FACTORY);
	}

	private GoogleClientSecrets getClientSecret() {
		// Reader reader = new InputStreamReader(getServletContext()
		// .getResourceAsStream(CLIENT_SECRETS_FILE_PATH));
		Reader reader = new InputStreamReader(getClass().getClassLoader()
				.getResourceAsStream(CLIENT_SECRETS_FILE_PATH));
		try {
			return GoogleClientSecrets.load(JSON_FACTORY, reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("No client secret");
		}
	}

	protected void sendJson(HttpServletResponse resp, int code, Object o) {
		resp.setContentType("application/json");
		try {
			resp.getWriter().print(new Gson().toJson(o).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	protected void sendJson(HttpServletResponse resp, Object o) {
		sendJson(resp, 200, o);
	}

	protected void loginIfRequired(HttpServletRequest req,
			HttpServletResponse resp) {
		Credential c = getCredential(req, resp);
		if (c == null) {
			try {
				resp.sendRedirect(credentialManager.getAuthorizationUrl());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Cant get redirect to autho page");
			}
		}
	}

	protected Credential getCredential(HttpServletRequest req,
			HttpServletResponse resp) {
		String userId = (String) req.getSession().getAttribute(
				KEY_SESSION_USERID);
		if (userId != null)
			return credentialManager.get(userId);
		return null;
	}

	protected Oauth2 getOauth2Service(Credential credential) {
		return new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential).build();
	}

	protected void handleCallbackIfRequired(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String code = req.getParameter("code");
		if (code != null) {
			Credential c = credentialManager.retrieve(code);
			Oauth2 service = getOauth2Service(c);

			try {
				Userinfoplus inf = service.userinfo().get().execute();
				String id = inf.getId();
				credentialManager.save(id, c);
				req.getSession().setAttribute(KEY_SESSION_USERID, id);
				resp.sendRedirect("/index");
			} catch (IOException e) {
				// TODO Auto-generated catch block

				throw new RuntimeException("Cant handle Oauth2 call back");
			}
		} else {
			resp.sendRedirect("/login");
		}
	}

	protected Drive getDriveService(Credential credential) {
		return new Drive.Builder(TRANSPORT, JSON_FACTORY, credential).build();
	}
}
