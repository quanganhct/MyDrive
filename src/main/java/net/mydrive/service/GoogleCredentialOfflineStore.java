package net.mydrive.service;

import java.util.HashMap;
import java.util.Map;

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

	public static GoogleCredentialOfflineStore getGoogleCredentialOffline() {
		if (myStore == null)
			myStore = new GoogleCredentialOfflineStore();
		return myStore;
	}

	public boolean getUserCredential(String userId, String googleAcc,
			Credential c) {
		if (myStore.credentialMap.containsKey(userId)) {
			CredentialDataOffline data = myStore.credentialMap.get(userId).get(
					googleAcc);
			c.setRefreshToken(data.refreshToken);
			return true;
		}
		return false;
	}
	
	public String getRefreshToken(String userId, String googleAcc) {
		return myStore.credentialMap.get(userId).get(googleAcc).refreshToken;
	}

	public void deleteUserCredential(String userId) {
		myStore.credentialMap.remove(userId);
	}

	public void saveUserCredential(String userId, String googleAcc, Credential c) {
		CredentialDataOffline data = new CredentialDataOffline();
		data.accessToken = c.getAccessToken();
		data.refreshToken = c.getRefreshToken();
		data.expireTime = c.getExpirationTimeMilliseconds();
		if (!myStore.credentialMap.containsKey(userId))
			myStore.credentialMap.put(userId,
					new HashMap<String, CredentialDataOffline>());
		myStore.credentialMap.get(userId).put(googleAcc, data);
	}
	
	
}
