package com.example.percentlinearlayout.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wk on 2016/9/29.
 */

public class UIC_TextView extends TextView {
    public UIC_TextView(Context context) {
        super(context);
        init();
    }

    public UIC_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        UIC_TextView.this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        UIC_TextView.this.setSingleLine();
        UIC_TextView.this.setFocusable(true);
        UIC_TextView.this.setFocusableInTouchMode(true);
        UIC_TextView.this.setMarqueeRepeatLimit(1);
    }
}
