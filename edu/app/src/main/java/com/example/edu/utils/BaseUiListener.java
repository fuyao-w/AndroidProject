package com.example.edu.utils;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by 扶摇 on 2017/7/2.
 */

public class BaseUiListener implements IUiListener {
    @Override
    public void onComplete(Object o) {
        doComplete(o);
    }
    protected void doComplete(Object values) {
    }
    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
