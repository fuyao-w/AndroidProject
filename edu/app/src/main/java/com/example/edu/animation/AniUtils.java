package com.example.edu.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;

/**
 * Created by 扶摇 on 2017/7/14.
 */

public class AniUtils {
    public static AnimationSet cAnimation() {
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(1000);

        set.addAnimation(alpha);

        return set;
    }

    public static AnimationSet xAnimation() {

        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(1000);

        set.addAnimation(alpha);

        return set;
    }
}
