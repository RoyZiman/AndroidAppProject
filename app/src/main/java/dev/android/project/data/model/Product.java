package dev.android.project.data.model;

public class Product
{
    private final String _storeID;
    private final String _description;
    private final double _price;
    private final String _title;
    private String _id;

    public Product(String title, String description, double price, String storeID)
    {
        _title = title;
        _description = description;
        _price = price;
        _storeID = storeID;
    }

    public String getId()
    {
        return _id;
    }

    public Product setId(String id)
    {
        _id = id;
        return this;
    }

    public String getTitle()
    {
        return _title;
    }

    public String getDescription()
    {
        return _description;
    }

    public double getPrice()
    {
        return _price;
    }

    public String getPriceAsString()
    {
        return "$" + _price;
    }

    public String getStoreID()
    {
        return _storeID;
    }

    @Override
    public String toString()
    {
        return "Product { " +
               "name = '" + _title + '\'' +
               ", description = '" + _description + '\'' +
               ", price = $" + _price +
               " }";
    }
}
