package dev.android.project.data.providers.Firebase;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Provides a singleton instance of FirebaseFirestore, configured to use the emulator on first instantiation.
 */
public class FBFirestore
{
    private static boolean _isInstantiated = false;

    /**
     * Gets the singleton instance of FirebaseFirestore.
     * <p>
     * On the first call, configures FirebaseFirestore to use the local emulator.
     *
     * @return The FirebaseFirestore instance.
     */
    public static FirebaseFirestore getInstance()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (!_isInstantiated)
        {
            _isInstantiated = true;
            db.useEmulator("10.0.2.2", 8080);
        }
        return db;
    }
}
