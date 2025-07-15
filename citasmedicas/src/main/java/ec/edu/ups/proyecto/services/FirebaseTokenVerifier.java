package ec.edu.ups.proyecto.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import java.io.IOException;
import java.io.InputStream;

public class FirebaseTokenVerifier {

	 private static boolean initialized = false;

	    public FirebaseTokenVerifier() throws IOException {
	        initializeFirebase();
	    }

	    private synchronized void initializeFirebase() throws IOException {
	        if (!initialized) {
	            InputStream serviceAccount = getClass().getClassLoader()
	                .getResourceAsStream("clave-firebase.json");

	            FirebaseOptions options = FirebaseOptions.builder()
	                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
	                .build();

	            FirebaseApp.initializeApp(options);
	            initialized = true;
	        }
	    }

	    public FirebaseToken verificarToken(String idToken) throws Exception {
	        return FirebaseAuth.getInstance().verifyIdToken(idToken);
	    }
}
