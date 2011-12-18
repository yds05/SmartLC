
package com.lifecalendar.dayview;

import android.view.View;

public class Rotate3d {
    private Rotate3d() {
    }

    public static void leftRotate(View layoutFrom, View layoutTo, int centerX, int centerY,
            int duration, android.view.animation.Animation.AnimationListener animationListener) {
        final Animation3d fromAnimation = new Animation3d(0.0F, -90F, centerX, centerY);
        final Animation3d toAnimation = new Animation3d(90F, 0.0F, centerX, centerY);
        fromAnimation.setDuration(duration);
        toAnimation.setDuration(duration);
        toAnimation.setAnimationListener(animationListener);
        layoutFrom.startAnimation(fromAnimation);
        layoutTo.startAnimation(toAnimation);
        layoutTo.setVisibility(0);
        layoutFrom.setVisibility(8);
    }

    public static void rightRotate(View layoutFrom, View layoutTo, int centerX, int centerY,
            int duration, android.view.animation.Animation.AnimationListener animationListener) {
        final Animation3d fromAnimation = new Animation3d(0.0F, 90F, centerX, centerY);
        final Animation3d toAnimation = new Animation3d(-90F, 0.0F, centerX, centerY);
        fromAnimation.setDuration(duration);
        toAnimation.setDuration(duration);
        toAnimation.setAnimationListener(animationListener);
        layoutFrom.startAnimation(fromAnimation);
        layoutTo.startAnimation(toAnimation);
        layoutTo.setVisibility(0);
        layoutFrom.setVisibility(8);
    }
}
