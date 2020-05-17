package com.example.artlet_v1;

import android.view.View;
import androidx.viewpager.widget.ViewPager;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float min_scale_factor = 0.85f;
    private static final float min_alpha_factor = 0.5f;

    public void transformPage(View v, float pos) {
        int width = v.getWidth();
        int height = v.getHeight();

        if (pos < -1) {
            v.setAlpha(0f);
        } else if (pos <= 1) {
            float abs_pos = (pos<0)?-pos:pos;
            float scale = (min_scale_factor  > (1-abs_pos))?min_scale_factor:1-abs_pos;
            float v_mar = height * (1 - scale) / 2;
            float h_mar = width * (1 - scale) / 2;
            if (pos < 0) {
                v.setTranslationX(h_mar - v_mar / 2);
            } else {
                v.setTranslationX(-h_mar + v_mar / 2);
            }

            v.setScaleX(scale);
            v.setScaleY(scale);

            v.setAlpha(min_alpha_factor + (scale - min_scale_factor)/(1 - min_scale_factor) * (1 - min_alpha_factor));

        } else {
            v.setAlpha(0f);
        }
    }
}
