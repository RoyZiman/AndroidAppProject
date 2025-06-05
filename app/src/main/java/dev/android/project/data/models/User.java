package dev.android.project.data.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.android.project.data.providers.Firebase.FBAuth;

/**
 * Represents a user in the application.
 */
public class User
{
    private static final FirebaseAuth AUTH = FBAuth.getInstance();
    private final String _id;
    private final String _name;
    private final String _email;

    /**
     * Constructs a User from a FirebaseUser object.
     *
     * @param user The FirebaseUser instance.
     */
    public User(FirebaseUser user)
    {
        this(user.getUid(), user.getDisplayName(), user.getEmail());
    }

    /**
     * Constructs a User with the specified ID, name, and email.
     *
     * @param id The user's unique ID.
     * @param name The user's display name.
     * @param email The user's email address.
     */
    public User(String id, String name, String email)
    {
        _id = id;
        _name = name;
        _email = email;
    }

    /**
     * Gets the currently authenticated user.
     * <p>
     * Please note: If no user is logged in, this method may cause a NullPointerException. Always check isLoggedIn()
     * before calling.
     *
     * @return The current User instance.
     */
    public static User getCurrentUser()
    {
        FirebaseUser user = AUTH.getCurrentUser();
        return new User(user);
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return True if a user is logged in, false otherwise.
     */
    public static Boolean isLoggedIn()
    {
        FirebaseUser user = AUTH.getCurrentUser();
        return user != null;
    }

    /**
     * Signs out the currently authenticated user.
     */
    public static void signOut()
    {
        AUTH.signOut();
    }

    /**
     * Gets the user's unique ID as stored in the database.
     *
     * @return The user's ID.
     */
    public String getId()
    {
        return _id;
    }

    /**
     * Gets the user's display name.
     *
     * @return The user's name.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Gets the user's email address.
     *
     * @return The user's email.
     */
    public String getEmail()
    {
        return _email;
    }
}