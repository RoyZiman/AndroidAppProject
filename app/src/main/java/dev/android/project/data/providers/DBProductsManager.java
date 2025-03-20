package dev.android.project.data.providers;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

import dev.android.project.data.model.Product;
import dev.android.project.data.providers.Firebase.FBFirestore;

public class DBProductsManager
{
    public static final String COLLECTION_NAME = "products";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PRICE = "price";

    private static final CollectionReference _collectionRef = FBFirestore.getInstance().collection(COLLECTION_NAME);

    public static Task<ArrayList<Product>> getAllProducts()
    {
        TaskCompletionSource<ArrayList<Product>> taskCompletionSource = new TaskCompletionSource<>();

        _collectionRef.orderBy(FIELD_PRICE, Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                ArrayList<Product> products = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments())
                {
                    products.add(new Product(document.getString(FIELD_TITLE),
                                             document.getString(FIELD_DESCRIPTION),
                                             document.getDouble(FIELD_PRICE))
                                         .setID(document.getId()));
                }
                taskCompletionSource.setResult(products);
            }
            else
                taskCompletionSource.setException(task.getException());
        });

        return taskCompletionSource.getTask();
    }

    public static void addProduct(Product product)
    {
        _collectionRef
                .add(new HashMap<String, Object>()
                {{
                    put(FIELD_TITLE, product.getTitle());
                    put(FIELD_DESCRIPTION, product.getDescription());
                    put(FIELD_PRICE, product.getPrice());
                }})
                .addOnSuccessListener(ref -> product.setID(ref.getId()));
    }

    public static void setProduct(Product product)
    {
        _collectionRef.document(product.getID()).set(new HashMap<>()
        {{
            put(FIELD_TITLE, product.getTitle());
            put(FIELD_DESCRIPTION, product.getDescription());
            put(FIELD_PRICE, product.getPrice());
        }});
    }


}
