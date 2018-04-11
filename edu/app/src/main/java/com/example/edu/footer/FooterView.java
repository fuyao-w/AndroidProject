package com.example.edu.footer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.example.edu.R;


/**
 * Created by 扶摇 on 2017/7/13.
 */

public class FooterView extends LinearLayout implements SwipeLoadMoreTrigger,SwipeTrigger {
   LinearLayout linearLayoutping,linearLayoutup,linearLayoutdown;
    public FooterView(Context context) {
        super(context);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        linearLayoutup = (LinearLayout) findViewById(R.id.dup);
        linearLayoutping = (LinearLayout) findViewById(R.id.dping);
        linearLayoutdown = (LinearLayout) findViewById(R.id.ddown);

    }
    @Override
    public void onLoadMore() {
        linearLayoutdown.setVisibility(GONE);
        linearLayoutping.setVisibility(VISIBLE);
        linearLayoutup.setVisibility(GONE);
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onSwipe(int i, boolean b) {

    }

    @Override
    public void onRelease() {
        linearLayoutdown.setVisibility(GONE);
        linearLayoutping.setVisibility(VISIBLE);
        linearLayoutup.setVisibility(GONE);
    }

    @Override
    public void complete() {
        linearLayoutdown.setVisibility(VISIBLE);
        linearLayoutping.setVisibility(GONE);
        linearLayoutup.setVisibility(GONE);
    }

    @Override
    public void onReset() {
        linearLayoutdown.setVisibility(GONE);
        linearLayoutup.setVisibility(VISIBLE);
        linearLayoutping.setVisibility(GONE);
    }
}
