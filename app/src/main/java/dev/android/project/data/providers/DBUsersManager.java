package dev.android.project.data.providers;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

import dev.android.project.data.models.User;
import dev.android.project.data.providers.Firebase.FBFirestore;

public class DBUsersManager
{
    public static final String COLLECTION_NAME = "users";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_EMAIL = "email";

    private static final CollectionReference _collectionRef = FBFirestore.getInstance().collection(COLLECTION_NAME);

    public static void addUser(User user)
    {
        _collectionRef.document(user.getId()).set(new HashMap<>()
        {{
            put(FIELD_NAME, user.getName());
            put(FIELD_EMAIL, user.getEmail());
        }});
    }

    public static Task<User> getUser(String id)
    {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();

        _collectionRef.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                DocumentSnapshot document = task.getResult();
                taskCompletionSource.setResult(
                        new User(id,
                                 document.get(FIELD_NAME).toString(),
                                 document.get(FIELD_EMAIL).toString()));
            }
            else
                taskCompletionSource.setException(task.getException());
        });

        return taskCompletionSource.getTask();
    }
}
