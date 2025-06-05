package dev.android.project.data.providers;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

import dev.android.project.data.models.Product;
import dev.android.project.data.providers.Firebase.FBFirestore;

/**
 * Manages product-related operations in Firestore.
 * <p>
 * Provides methods to retrieve, add, and update products in the Firestore database.
 */
public class DBProductsManager
{
    public static final String COLLECTION_NAME = "products";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_STORE_ID = "storeID";

    private static final CollectionReference _collectionRef = FBFirestore.getInstance().collection(COLLECTION_NAME);

    /**
     * Retrieves all products from the Firestore collection.
     *
     * @return A {@link Task} containing a list of all {@link Product} objects.
     */
    public static Task<ArrayList<Product>> getAllProducts()
    {
        return getProducts(null);
    }

    /**
     * Retrieves products from the Firestore collection, optionally filtered.
     *
     * @param filter An optional {@link Filter} to apply to the query. If null, retrieves all products.
     *
     * @return A {@link Task} containing a list of {@link Product} objects.
     */
    private static Task<ArrayList<Product>> getProducts(@Nullable Filter filter)
    {
        TaskCompletionSource<ArrayList<Product>> taskCompletionSource = new TaskCompletionSource<>();
        Query query = _collectionRef;
        if (filter != null)
            query = query.where(filter);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                ArrayList<Product> products = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments())
                    products.add(documentToProduct(document));

                taskCompletionSource.setResult(products);
            }
            else
                taskCompletionSource.setException(task.getException());
        });

        return taskCompletionSource.getTask();
    }

    /**
     * Converts a Firestore {@link DocumentSnapshot} to a {@link Product} object.
     *
     * @param document The Firestore document snapshot.
     *
     * @return The corresponding {@link Product} object.
     */
    private static Product documentToProduct(DocumentSnapshot document)
    {
        return new Product(document.getString(FIELD_TITLE),
                           document.getString(FIELD_DESCRIPTION),
                           document.getDouble(FIELD_PRICE),
                           document.getString(FIELD_STORE_ID))
                .setId(document.getId());
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id The ID of the product to retrieve.
     *
     * @return A {@link Task} containing the {@link Product} object.
     */
    public static Task<Product> getProduct(String id)
    {
        TaskCompletionSource<Product> taskCompletionSource = new TaskCompletionSource<>();

        _collectionRef.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                DocumentSnapshot document = task.getResult();
                taskCompletionSource.setResult(documentToProduct(document));
            }
            else
                taskCompletionSource.setException(task.getException());
        });

        return taskCompletionSource.getTask();
    }

    /**
     * Adds a new product to the Firestore collection.
     *
     * @param product The {@link Product} to add.
     *
     * @return A {@link Task} containing the added {@link Product} with its Firestore ID set.
     */
    public static Task<Product> addProduct(Product product)
    {
        TaskCompletionSource<Product> taskCompletionSource = new TaskCompletionSource<>();

        _collectionRef
                .add(new HashMap<String, Object>()
                {{
                    put(FIELD_TITLE, product.getTitle());
                    put(FIELD_DESCRIPTION, product.getDescription());
                    put(FIELD_PRICE, product.getPrice());
                    put(FIELD_STORE_ID, product.getStoreID());
                }})
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        product.setId(task.getResult().getId());
                        taskCompletionSource.setResult(product);
                    }
                    else
                        taskCompletionSource.setException(task.getException());
                });

        return taskCompletionSource.getTask();
    }

    /**
     * Updates an existing product in the Firestore collection.
     *
     * @param product The {@link Product} to update.
     */
    public static void setProduct(Product product)
    {
        _collectionRef.document(product.getId()).set(new HashMap<>()
        {{
            put(FIELD_TITLE, product.getTitle());
            put(FIELD_DESCRIPTION, product.getDescription());
            put(FIELD_PRICE, product.getPrice());
            put(FIELD_STORE_ID, product.getStoreID());
        }});
    }

    /**
     * Retrieves all products for a specific user (store) by user ID.
     *
     * @param userId The ID of the user (store) whose products to retrieve.
     *
     * @return A {@link Task} containing a list of {@link Product} objects.
     */
    public static Task<ArrayList<Product>> getAllProductsByUser(String userId)
    {
        return getProducts(Filter.equalTo(FIELD_STORE_ID, userId));
    }
}
