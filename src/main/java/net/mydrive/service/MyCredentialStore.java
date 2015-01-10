package net.mydrive.service;

/**
 * @author NGUYEN Quang Anh
 */
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.auth.oauth2.Credential;

public class MyCredentialStore {
	private static MyCredentialStore myStore = null;
	private Map<String, Credential> credentialMap;

	private MyCredentialStore() {
		credentialMap = new HashMap<String, Credential>();
	}

	public static MyCredentialStore getInstante() {
		if (myStore == null)
			myStore = new MyCredentialStore();
		return myStore;
	}

	public Credential getUserCredential(String userId) {
		Credential c = null;
		if (myStore.credentialMap.containsKey(userId))
			c = myStore.credentialMap.get(userId);
		return c;
	}

	public void deleteUserCredential(String userId) {
		myStore.credentialMap.remove(userId);
	}

	public void saveUserCredential(String userId, Credential c) {
		myStore.credentialMap.put(userId, c);
	}
}
