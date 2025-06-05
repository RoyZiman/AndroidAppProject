package dev.android.project.data.models;

import androidx.annotation.NonNull;


/**
 * A generic class that holds a result of an operation, either a success with data or an error with an exception.
 *
 * @param <T> The type of data held in case of success.
 */
public class Result<T>
{
    // Hide the private constructor to limit subclass types (Success, Error)
    private Result() {}

    /**
     * Returns a string representation of the result.
     *
     * @return A string describing the result, either success or error.
     */
    @NonNull
    @Override
    public String toString()
    {
        if (this instanceof Result.Success)
        {
            Result.Success success = (Result.Success)this;
            return "Success[data=" + success.getData().toString() + "]";
        }
        else if (this instanceof Result.Error)
        {
            Result.Error error = (Result.Error)this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    /**
     * Represents a successful result containing data.
     *
     * @param <T> The type of data held.
     */
    public final static class Success<T> extends Result
    {
        private final T _data;

        /**
         * Constructs a Success result with the given data.
         *
         * @param data The data of the successful result.
         */
        public Success(T data)
        {
            _data = data;
        }

        /**
         * Gets the data of the successful result.
         *
         * @return The data.
         */
        public T getData()
        {
            return _data;
        }
    }

    /**
     * Represents an error result containing an exception.
     */
    public final static class Error extends Result
    {
        private final Exception _error;

        /**
         * Constructs an Error result with the given exception.
         *
         * @param error The exception representing the error.
         */
        public Error(Exception error)
        {
            _error = error;
        }

        /**
         * Gets the exception of the error result.
         *
         * @return The exception.
         */
        public Exception getError()
        {
            return _error;
        }
    }
}