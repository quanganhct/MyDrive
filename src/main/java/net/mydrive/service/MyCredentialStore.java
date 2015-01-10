package net.mydrive.service;

/**
 * @author nguyenquanganh
 */
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.auth.oauth2.Credential;

public class MyCredentialStore {

	class CredentialData {
		String accessToken;
		String refreshToken;
		long expireTime;

		public CredentialData(String access, String refresh, long expireTime) {
			this.accessToken = access;
			this.refreshToken = refresh;
			this.expireTime = expireTime;
		}
	}

	private static MyCredentialStore myStore = null;
	private Map<String, CredentialData> credentialMap;

	private MyCredentialStore() {
		credentialMap = new HashMap<String, CredentialData>();
	}

	public static MyCredentialStore getInstante() {
		if (myStore == null)
			myStore = new MyCredentialStore();
		return myStore;
	}

	public boolean getUserCredential(String userId, Credential c) {
		if (myStore.credentialMap.containsKey(userId)) {
			CredentialData data = myStore.credentialMap.get(userId);
			c.setAccessToken(data.accessToken);
			c.setRefreshToken(data.refreshToken);
			c.setExpirationTimeMilliseconds(data.expireTime);
			return true;
		}
		return false;
	}

	public void deleteUserCredential(String userId) {
		myStore.credentialMap.remove(userId);
	}

	public void saveUserCredential(String userId, Credential c) {
		CredentialData data = new CredentialData(c.getAccessToken(),
				c.getRefreshToken(), c.getExpirationTimeMilliseconds());
		myStore.credentialMap.put(userId, data);
	}
}
