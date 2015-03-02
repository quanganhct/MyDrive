package net.mydrive.ws;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.mydrive.service.GoogleCredentialOfflineStore;
import net.mydrive.service.MyCredentialStore;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class CredentialManager2 {
	private GoogleClientSecrets clientSecret;
	private HttpTransport transport;
	private JsonFactory jsonFactory;

	public static final List<String> SCOPES = Arrays.asList(
			"https://www.googleapis.com/auth/drive.file",
			"https://www.googleapis.com/auth/userinfo.email",
			"https://www.googleapis.com/auth/userinfo.profile");

	public CredentialManager2(GoogleClientSecrets clientSecret,
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

	public Credential get(HttpServletRequest request, String userId,
			String googleAcc) {
		Credential c = buildEmpty();
		if (GoogleCredentialOfflineStore.getGoogleCredentialOffline(request)
				.getUserCredential(request, userId, googleAcc, c)) {
			return c;
		}
		return null;
	}

	public void save(HttpServletRequest request, String userId,
			String googleAcc, Credential c) {
		GoogleCredentialOfflineStore.getGoogleCredentialOffline(request)
				.saveUserCredential(request, userId, googleAcc, c);
	}

	public void save(HttpServletRequest request, String userId,
			String googleAcc, String refreshToken) {
		GoogleCredentialOfflineStore.getGoogleCredentialOffline(request)
				.addUserDataToStore(request, userId, googleAcc, refreshToken);
	}

	public void delete(HttpServletRequest request, String userId) {
		GoogleCredentialOfflineStore.getGoogleCredentialOffline(request)
				.deleteUserCredential(request, userId);
	}

	public String getAuthorizationUrl() {
		GoogleAuthorizationCodeRequestUrl url = new GoogleAuthorizationCodeRequestUrl(
				clientSecret.getWeb().getClientId(), clientSecret.getWeb()
						.getRedirectUris().get(0), SCOPES).setAccessType(
				"offline").setApprovalPrompt("force");
		return url.build();
	}

	public Credential getCredentialWithRefreshToken(HttpServletRequest request,
			String userId, String googleAcc) throws Exception {
		String refreshToken = GoogleCredentialOfflineStore
				.getGoogleCredentialOffline(request).getRefreshToken(request,
						userId, googleAcc);

		if (refreshToken == null)
			throw new Exception("Refresh token null");

		GoogleCredential.Builder builder = new GoogleCredential.Builder()
				.setTransport(transport).setJsonFactory(jsonFactory)
				.setClientSecrets(clientSecret);
		builder.addRefreshListener(new MyCredentialRefreshListener());
		Credential c = builder.build();
		c.setRefreshToken(refreshToken);
		try {
			c.refreshToken();
			return c;
		} catch (IOException e) {
			System.err.println("Cant get access token " + e);
			return null;
		}
	}
}
