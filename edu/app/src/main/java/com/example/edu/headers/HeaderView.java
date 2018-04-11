package com.example.edu.headers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.example.edu.R;

/**
 * Created by 扶摇 on 2017/7/13.
 */

public class HeaderView extends LinearLayout implements SwipeRefreshTrigger,SwipeTrigger {
   LinearLayout linearLayoutup,linearLayoutping,linearLayoutdown;
    private int mTriggerOffset;
    private int mFinalOffset;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTriggerOffset = 300;
//                context.getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height);
        mFinalOffset = 120;
//                context.getResources().getDimensionPixelOffset(R.dimen.load_more_final_offset);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
       linearLayoutup = (LinearLayout) findViewById(R.id.up);
       linearLayoutping = (LinearLayout) findViewById(R.id.ping);
       linearLayoutdown = (LinearLayout) findViewById(R.id.down);

    }




    @Override
    public void onRefresh() {
        linearLayoutdown.setVisibility(GONE);
        linearLayoutping.setVisibility(VISIBLE);
        linearLayoutup.setVisibility(GONE);
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onSwipe(int i, boolean b) {
        float alpha = 1 / (float) mTriggerOffset;
        linearLayoutdown.setAlpha(0.5f);
        if (!b) {
            linearLayoutdown.setAlpha(i / (float) mFinalOffset);
        }
    }

    @Override
    public void onRelease() {
        linearLayoutdown.setVisibility(GONE);
        linearLayoutping.setVisibility(VISIBLE);
       linearLayoutup.setVisibility(GONE);
    }

    @Override
    public void complete() {
        linearLayoutdown.setVisibility(GONE);
        linearLayoutping.setVisibility(GONE);
        linearLayoutup.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
           linearLayoutdown.setVisibility(VISIBLE);
        linearLayoutup.setVisibility(GONE);
        linearLayoutping.setVisibility(GONE);
    }
}
