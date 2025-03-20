package dev.android.project.data.providers.Firebase;

import com.google.firebase.auth.FirebaseAuth;

public class FBAuth
{
    private static boolean _isInstantiated = false;

    public static FirebaseAuth getInstance()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (!_isInstantiated)
        {
            _isInstantiated = true;
            auth.useEmulator("10.0.2.2", 9099);
        }
        return auth;
    }
}
