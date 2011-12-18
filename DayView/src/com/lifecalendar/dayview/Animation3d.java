package com.lifecalendar.dayview;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Animation3d extends Animation {
    private final float mFromDegree;

    private final float mToDegree;

    private final float mCenterX;

    private final float mCenterY;

    private Camera mCamera;

    public Animation3d(float fromDegree, float toDegree, float centerX, float centerY) {
        mFromDegree = fromDegree;
        mToDegree = toDegree;
        mCenterX = centerX;
        mCenterY = centerY;
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float FromDegree = mFromDegree;
        float degrees = FromDegree + (mToDegree - mFromDegree) * interpolatedTime;
        final float centerX = mCenterX;
        final float centerY = mCenterY;
        Matrix matrix = t.getMatrix();
        if (degrees <= -76F) {
            degrees = -90F;
            mCamera.save();
            mCamera.rotateY(degrees);
            mCamera.getMatrix(matrix);
            mCamera.restore();
        } else if (degrees >= 76F) {
            degrees = 90F;
            mCamera.save();
            mCamera.rotateY(degrees);
            mCamera.getMatrix(matrix);
            mCamera.restore();
        } else {
            mCamera.save();
            mCamera.translate(0.0F, 0.0F, centerX);
            mCamera.rotateY(degrees);
            mCamera.translate(0.0F, 0.0F, -centerX);
            mCamera.getMatrix(matrix);
            mCamera.restore();
        }
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
