package dev.android.project.data.model;

import java.time.LocalDateTime;

public class Product
{
    private final int _stock;
    private final LocalDateTime _dateAdded;
    private final String _storeID;
    private final String _description;
    private final double _price;
    private final String _title;
    private String _id;

    public Product(String title, String description, double price)
    {
        _title = title;
        _description = description;
        _price = price;
        _stock = 0;
        _dateAdded = LocalDateTime.now();
        _storeID = "";
    }

    public String getID()
    {
        return _id;
    }

    public Product setID(String id)
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

    public int getStock()
    {
        return _stock;
    }

    public LocalDateTime getDateAdded()
    {
        return _dateAdded;
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
