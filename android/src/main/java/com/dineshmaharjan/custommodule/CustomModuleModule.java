package com.dineshmaharjan.custommodule;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.Callback;

import android.provider.Settings;
import android.database.ContentObserver;
import android.net.Uri;
import com.facebook.react.modules.core.DeviceEventManagerModule;


public class CustomModuleModule extends ReactContextBaseJavaModule {

  private ReactApplicationContext appContext;
  private String SYSTEM_ROTATE_PROPERTY = Settings.System.ACCELEROMETER_ROTATION;

  public CustomModuleModule(ReactApplicationContext reactContext) {
    super(reactContext);
    appContext = reactContext;
  }

  @Override
  public String getName() {
    return "CustomModule";
  }

  private ContentObserver _autoRotationSettingObserver = new AutoRotationSettingObserver();

  private class AutoRotationSettingObserver extends ContentObserver {
    public AutoRotationSettingObserver() {
      super(null);
    }
    @Override
    public void onChange(boolean selfChange) {
      this.onChange(selfChange, null);
    }
    @Override
    public void onChange(boolean selfChange, Uri uri) {
      boolean autoRotationSetting = getAutoRotationSetting();
      appContext
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit("autoRotationSettingChanged", autoRotationSetting);
    }
  }

  public boolean getAutoRotationSetting() {
    try {
      String ROTATE_PROP = Settings.System.ACCELEROMETER_ROTATION;
      boolean autoRotateSettingValue = Settings.System.getInt(appContext.getContentResolver(), ROTATE_PROP) != 0;
      return autoRotateSettingValue;
    } catch (Exception e) {
      return false;
    }
  }


  @ReactMethod
  public void init() {
    try {
      appContext.getContentResolver().registerContentObserver(
        Settings.System.getUriFor(SYSTEM_ROTATE_PROPERTY),
        false,
        _autoRotationSettingObserver
      );
    } catch(Exception e) {}
  }

  @ReactMethod
  public void remove() {
    try {
      appContext.getContentResolver().unregisterContentObserver(_autoRotationSettingObserver);
    } catch(Exception e) {}
  }

  @ReactMethod
  public void getAutoRotationValue(Callback callback) {
    boolean autoRotateSettingValue = getAutoRotationSetting();
    callback.invoke(autoRotateSettingValue);
  }


}
