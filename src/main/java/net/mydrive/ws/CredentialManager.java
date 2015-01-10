package net.mydrive.ws;

/**
 * @author nguyenquanganh
 */

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import net.mydrive.service.MyCredentialStore;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class CredentialManager {
	private GoogleClientSecrets clientSecret;
	private HttpTransport transport;
	private JsonFactory jsonFactory;

	public static final List<String> SCOPES = Arrays.asList(
			"https://www.googleapis.com/auth/drive.file",
			"https://www.googleapis.com/auth/userinfo.email",
			"https://www.googleapis.com/auth/userinfo.profile");

	public CredentialManager(GoogleClientSecrets clientSecret,
			HttpTransport transport, JsonFactory jFactory) {
		this.clientSecret = clientSecret;
		this.transport = transport;
		this.jsonFactory = jFactory;
	}

	public Credential buildEmpty() {
		Credential c = new GoogleCredential.Builder()
				.setClientSecrets(clientSecret).setTransport(transport)
				.setJsonFactory(jsonFactory).build();

		return c;
	}

	public Credential get(String userId) {
		Credential c = buildEmpty();
		if (MyCredentialStore.getInstante().getUserCredential(userId, c)) {
			return c;
		}
		return null;
	}

	public void save(String userId, Credential c) {
		MyCredentialStore.getInstante().saveUserCredential(userId, c);
	}

	public void dekete(String userId) {
		MyCredentialStore.getInstante().deleteUserCredential(userId);
	}

	public String getAuthorizationUrl() {
		GoogleAuthorizationCodeRequestUrl url = new GoogleAuthorizationCodeRequestUrl(
				clientSecret.getWeb().getClientId(), clientSecret.getWeb()
						.getRedirectUris().get(0), SCOPES).setAccessType(
				"offline").setApprovalPrompt("force");
		return url.build();
	}

	public Credential getNewAccessToken(String code) {
		try {
			GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
					transport, jsonFactory, clientSecret.getWeb().getClientId(),
					clientSecret.getWeb().getClientSecret(), code, clientSecret
							.getWeb().getRedirectUris().get(0)).execute();
			
			return buildEmpty().setAccessToken(response.getAccessToken());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}