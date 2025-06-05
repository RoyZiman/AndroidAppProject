package dev.android.project.data.providers.Firebase;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Provides a singleton instance of FirebaseAuth, configured to use the emulator on first instantiation.
 */
public class FBAuth
{
    private static boolean _isInstantiated = false;

    /**
     * Gets the singleton instance of FirebaseAuth.
     * <p>
     * On the first call, configures FirebaseAuth to use the local emulator.
     *
     * @return The FirebaseAuth instance.
     */
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
