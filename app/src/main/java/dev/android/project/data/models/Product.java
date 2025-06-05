package dev.android.project.data.models;

/**
 * Represents a listed product.
 */
public class Product
{
    private final String _storeID;
    private final String _description;
    private final double _price;
    private final String _title;
    private String _id;

    /**
     * Constructs a new Product.
     *
     * @param title The title of the product.
     * @param description The description of the product.
     * @param price The price of the product.
     * @param storeID The ID of the store selling the product.
     */
    public Product(String title, String description, double price, String storeID)
    {
        _title = title;
        _description = description;
        _price = price;
        _storeID = storeID;
    }

    /**
     * Gets the product's unique ID as stored in the database.
     *
     * @return The product ID.
     */
    public String getId()
    {
        return _id;
    }

    /**
     * Sets the product's unique ID as stored in the database.
     *
     * @param id The product ID.
     *
     * @return This Product instance.
     */
    public Product setId(String id)
    {
        _id = id;
        return this;
    }

    /**
     * Gets the title of the product.
     *
     * @return The title.
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Gets the description of the product.
     *
     * @return The description.
     */
    public String getDescription()
    {
        return _description;
    }

    /**
     * Gets the price of the product.
     *
     * @return The price.
     */
    public double getPrice()
    {
        return _price;
    }

    /**
     * Gets the price of the product as a formatted string.
     *
     * @return The price as a string.
     */
    public String getPriceAsString()
    {
        return "$" + _price;
    }

    /**
     * Gets the ID of the user listing the product.
     *
     * @return The store ID.
     */
    public String getStoreID()
    {
        return _storeID;
    }
}
