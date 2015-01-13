package net.mydrive.ws;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.management.RuntimeErrorException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

@SuppressWarnings("serial")
public abstract class MyBaseServlet extends HttpServlet {
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final String KEY_SESSION_USERID = "user_id";
	private static final String DEFAULT_MIMETYPE = "/test/plain";
	public static final String CLIENT_SECRETS_FILE_PATH = "/WEB-INF/client_secrets.json";

	private CredentialManager credentialManager = null;

	@Override
	public void init() throws ServletException {
		super.init();
		credentialManager = new CredentialManager(getClientSecret(), TRANSPORT,
				JSON_FACTORY);
	}

	private GoogleClientSecrets getClientSecret() {
		Reader reader = new InputStreamReader(getServletContext()
				.getResourceAsStream(CLIENT_SECRETS_FILE_PATH));
		try {
			return GoogleClientSecrets.load(JSON_FACTORY, reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeErrorException(new Error("No client secret"),
					"No client secret");
		}
	}
	
	protected void sendJson(HttpServletResponse resp, int code, Object o){
		resp.setContentType("application/json");
		//resp.getWriter().print();
	}
}
