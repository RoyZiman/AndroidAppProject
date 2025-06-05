package dev.android.project.data.providers.Firebase;

import com.google.firebase.storage.FirebaseStorage;

/**
 * Provides a singleton instance of FirebaseStorage, configured to use the emulator on first instantiation.
 */
public class FBStorage
{
    private static boolean _isInstantiated = false;

    /**
     * Gets the singleton instance of FirebaseStorage.
     * <p>
     * On the first call, configures FirebaseStorage to use the local emulator.
     *
     * @return The FirebaseStorage instance.
     */
    public static FirebaseStorage getInstance()
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if (!_isInstantiated)
        {
            _isInstantiated = true;
            storage.useEmulator("10.0.2.2", 9199);
        }
        return storage;
    }
}
