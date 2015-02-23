package net.mydrive.ws;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;

public class MyCredentialRefreshListener implements CredentialRefreshListener {

	@Override
	public void onTokenResponse(Credential credential,
			TokenResponse tokenResponse) throws IOException {
		// TODO Auto-generated method stub
		credential.setAccessToken(tokenResponse.getAccessToken());
		credential.setExpirationTimeMilliseconds(tokenResponse
				.getExpiresInSeconds() * 1000);
	}

	@Override
	public void onTokenErrorResponse(Credential credential,
			TokenErrorResponse tokenErrorResponse) throws IOException {
		// TODO Auto-generated method stub
		System.err.println(tokenErrorResponse);
	}

}
