package com.demo.apputil;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Pankaj on 6/26/2015.
 */
public class DownLoadStatusReceiver extends ResultReceiver{
    private Receiver mReceiver;

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    public DownLoadStatusReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

}
