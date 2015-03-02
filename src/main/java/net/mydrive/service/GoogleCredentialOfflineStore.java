package net.mydrive.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;

public class GoogleCredentialOfflineStore {

	class CredentialDataOffline {
		String accessToken = null;
		String refreshToken = null;
		long expireTime;

		CredentialDataOffline() {

		}
	}

	private static GoogleCredentialOfflineStore myStore = null;
	private Map<String, Map<String, CredentialDataOffline>> credentialMap = new HashMap<String, Map<String, CredentialDataOffline>>();

	public static GoogleCredentialOfflineStore getGoogleCredentialOffline(
			HttpServletRequest request) {
		if (request.getSession().getAttribute("GoogleCredentialOfflineStore") == null) {
			myStore = new GoogleCredentialOfflineStore();
			request.getSession().setAttribute("GoogleCredentialOfflineStore",
					myStore);
		}

		return (GoogleCredentialOfflineStore) request.getSession()
				.getAttribute("GoogleCredentialOfflineStore");
	}

	public boolean getUserCredential(HttpServletRequest request, String userId,
			String googleAcc, Credential c) {
		if (getGoogleCredentialOffline(request).credentialMap
				.containsKey(userId)) {
			CredentialDataOffline data = getGoogleCredentialOffline(request).credentialMap
					.get(userId).get(googleAcc);
			c.setRefreshToken(data.refreshToken);
			return true;
		}
		return false;
	}

	public String getRefreshToken(HttpServletRequest request, String userId,
			String googleAcc) {
		return getGoogleCredentialOffline(request).credentialMap.get(userId)
				.get(googleAcc).refreshToken;
	}

	public void deleteUserCredential(HttpServletRequest request, String userId) {
		getGoogleCredentialOffline(request).credentialMap.remove(userId);
	}

	public void saveUserCredential(HttpServletRequest request, String userId,
			String googleAcc, Credential c) {
		CredentialDataOffline data = new CredentialDataOffline();
		data.accessToken = c.getAccessToken();
		data.refreshToken = c.getRefreshToken();
		data.expireTime = c.getExpirationTimeMilliseconds();
		if (!getGoogleCredentialOffline(request).credentialMap
				.containsKey(userId))
			getGoogleCredentialOffline(request).credentialMap.put(userId,
					new HashMap<String, CredentialDataOffline>());
		getGoogleCredentialOffline(request).credentialMap.get(userId).put(
				googleAcc, data);
	}

	public void addUserDataToStore(HttpServletRequest request, String userId,
			String googleAcc, String refreshToken) {
		CredentialDataOffline data = new CredentialDataOffline();
		data.refreshToken = refreshToken;
		if (!getGoogleCredentialOffline(request).credentialMap
				.containsKey(userId))
			getGoogleCredentialOffline(request).credentialMap.put(userId,
					new HashMap<String, CredentialDataOffline>());
		getGoogleCredentialOffline(request).credentialMap.get(userId).put(
				googleAcc, data);
	}
}
