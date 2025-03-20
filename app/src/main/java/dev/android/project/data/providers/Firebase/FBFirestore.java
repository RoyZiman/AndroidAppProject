package dev.android.project.data.providers.Firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public class FBFirestore
{
    private static boolean _isInstantiated = false;

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
