package dev.android.project.data.providers.Firebase;

import com.google.firebase.storage.FirebaseStorage;

public class FBStorage
{
    private static boolean _isInstantiated = false;

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
