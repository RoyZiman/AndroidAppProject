package dev.android.project.data.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.android.project.data.providers.Firebase.FBAuth;

public class User
{


    private static final FirebaseAuth AUTH = FBAuth.getInstance();

    private final String _id;
    private final String _name;
    private final String _email;


    public User(FirebaseUser user)
    {
        this(user.getUid(), user.getDisplayName(), user.getEmail());
    }

    public User(String id, String name, String email)
    {
        _id = id;
        _name = name;
        _email = email;
    }

    public static User getCurrentUser()
    {
        FirebaseUser user = AUTH.getCurrentUser();
        return new User(user);
    }

    public static Boolean isLoggedIn()
    {
        FirebaseUser user = AUTH.getCurrentUser();
        return user != null;
    }

    public static void signOut()
    {
        AUTH.signOut();
    }

    public String getId()
    {
        return _id;
    }

    public String getName()
    {
        return _name;
    }

    public String getEmail()
    {
        return _email;
    }
}