package net.mydrive.ws;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class CredentialManager {
	private GoogleClientSecrets clientSecret;
	private HttpTransport transport;
	private JsonFactory jsonFactory;

	public CredentialManager(GoogleClientSecrets clientSecret,
			HttpTransport transport, JsonFactory jFactory) {
		this.clientSecret = clientSecret;
		this.transport = transport;
		this.jsonFactory = jFactory;
	}
	
	
}