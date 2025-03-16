package dev.android.project.data.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.android.project.data.providers.FBAuth;

public class User
{


    private static final FirebaseAuth AUTH = FBAuth.getInstance();

    private String _id;
    private String _name;
    private String _email;
    private FirebaseUser user;


    protected User(String id, String name, String email)
    {
        _id = id;
        _name = name;
        _email = email;
    }
    public User(FirebaseUser user)
    {
        this(user.getUid(), user.getDisplayName(), user.getEmail());
    }

    public static User getCurrentUser()
    {
        FirebaseUser user = AUTH.getCurrentUser();
//        if (user == null)
//            return new Guest();

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

    public String getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getEmail() {
        return _email;
    }
//
//    public static class Guest extends User
//    {
//        private Guest()
//        {
//            super("-1", "Guest", "");
//        }
//    }
}