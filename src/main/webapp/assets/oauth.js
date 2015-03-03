      var CLIENT_ID = '1059398804638-76ard6efika93a7a9dol03kv02ubl6os.apps.googleusercontent.com';
      var SCOPES = 'https://www.googleapis.com/auth/drive';

      /**
       * Called when the client library is loaded to start the auth flow.
       */
      function handleClientLoad() {
        window.setTimeout(checkAuth, 1);
      }

      /**
       * Check if the current user has authorized the application.
       */
      function checkAuth() {
        gapi.auth.authorize(
            {'client_id': CLIENT_ID, 'scope': SCOPES, 'immediate': true},
            handleAuthResult);
      }

      /**
       * Called when authorization server replies.
       *
       * @param {Object} authResult Authorization result.
       */
      function handleAuthResult(authResult) {
        console.log(authResult);
        var authButton = document.getElementById('authorizeButton');
        var filePicker = document.getElementById('filePicker');
        authButton.style.display = 'none';
        filePicker.style.display = 'none';
        if (authResult && !authResult.error) {
          // Access token has been successfully retrieved, requests can be sent to the API.
          filePicker.style.display = 'block';
          filePicker.onchange = uploadFile;
        } else {
          // No access token could be retrieved, show the button to start the authorization flow.
          authButton.style.display = 'block';
          authButton.onclick = function() {
              gapi.auth.authorize(
                  {'client_id': CLIENT_ID, 'scope': SCOPES, 'immediate': false},
                  handleAuthResult);
          };
        }
      }
