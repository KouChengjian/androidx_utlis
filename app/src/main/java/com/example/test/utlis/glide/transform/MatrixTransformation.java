package com.example.test.utlis.glide.transform;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;

import androidx.annotation.Nullable;

public class MatrixTransformation extends ImageViewTarget<Bitmap> {

    public MatrixTransformation(ImageView view) {
        super(view);
    }

    @Override
    protected void setResource(@Nullable Bitmap resource) {

        if (null == resource) {
            return;
        }
        view.setImageBitmap(resource);
        int width = resource.getWidth();
        int height = resource.getHeight();
        int imageWidth = view.getWidth();

        if (imageWidth == width) {
            return;
        }
        float sy = (float) (imageWidth * 0.1) / (float) (width * 0.1);
        Matrix matrix = new Matrix();
        matrix.postScale(sy, sy);
        Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, width, height, matrix, true);
        view.setImageBitmap(bitmap);
        if (bitmap.getHeight() > 2040) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }


}
