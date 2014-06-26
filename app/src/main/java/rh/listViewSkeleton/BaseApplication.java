package rh.listViewSkeleton;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BaseApplication extends Application {

    private static Bus mEventBus;
    private static BaseApplication instance;


    public static Bus getEventBus() {
        return mEventBus;
    }

    public BaseApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus = new Bus(ThreadEnforcer.ANY);
    }
}
