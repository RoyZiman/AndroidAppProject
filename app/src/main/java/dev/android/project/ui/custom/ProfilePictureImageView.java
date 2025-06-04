package dev.android.project.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.google.android.material.imageview.ShapeableImageView;

public class ProfilePictureImageView extends CardView
{
    private static final int FOREGROUND_ATTR = android.R.attr.selectableItemBackground;
    private ShapeableImageView _imageView;

    public ProfilePictureImageView(Context context)
    {
        super(context);
        init(context);
    }

    private void init(Context context)
    {
        int size = isInEditMode() ? 200 : getResources().getDimensionPixelSize(
                context.getResources().getIdentifier("profile_picture_size", "dimen", context.getPackageName())
        );
        setRadius((float)size / 2);
        LayoutParams params = new LayoutParams(size, size);
        setCardElevation(5);

        _imageView = new ShapeableImageView(context);
        _imageView.setLayoutParams(params);
        _imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        _imageView.setShapeAppearanceModel(
                _imageView.getShapeAppearanceModel()
                          .toBuilder()
                          .build()
        );
        addView(_imageView);
        // Set selectable foreground
        int[] attrs = new int[] {FOREGROUND_ATTR};
        android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs);
        int foregroundResId = ta.getResourceId(0, 0);
        ta.recycle();
        setForeground(foregroundResId != 0 ? context.getDrawable(foregroundResId) : null);

        _imageView.setImageResource(
                context.getResources().getIdentifier("ic_nav_profile", "drawable", context.getPackageName())
        );
    }

    public ProfilePictureImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public ProfilePictureImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setImageResource(int resId)
    {
        _imageView.setImageResource(resId);
    }

    public void setImageBitmap(android.graphics.Bitmap bitmap)
    {
        _imageView.setImageBitmap(bitmap);
    }

    public void setImageDrawable(android.graphics.drawable.Drawable drawable)
    {
        _imageView.setImageDrawable(drawable);
    }

    public ShapeableImageView getImageView()
    {
        return _imageView;
    }

}
