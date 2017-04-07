package costy.krzysiekz.com.costy.utils;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import costy.krzysiekz.com.costy.IntegrationTestCostyApplication;

public class CustomAndroidJUnitRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, IntegrationTestCostyApplication.class.getName(), context);
    }
}
