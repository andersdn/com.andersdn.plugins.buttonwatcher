package com.andersdn.plugins;

import org.apache.cordova.Plugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.media.AudioManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

@SuppressWarnings("deprecation")
public class HeadsetWatcher extends Plugin {

  private String callback;
  public HeadsetBroadcastReceiver headsetReceiver;
  @Override
  public PluginResult execute(String action, JSONArray data, String callbackId) {
    this.callback = callbackId;
    headsetReceiver = new HeadsetBroadcastReceiver(this);
    PluginResult result = new PluginResult(Status.NO_RESULT);
    this.cordova.getActivity().registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_MEDIA_BUTTON));
    result.setKeepCallback(true);
    return result;
  }
  
  public void changed(int state) {

    JSONObject status = new JSONObject();
    try {
      status.put(state);
    } catch (Exception ex) {
      Log.e("Headset", "JSON error " + ex.toString());
      return;
    }
    PluginResult result = new PluginResult(PluginResult.Status.OK, status);
    result.setKeepCallback(true);
    this.success(result, this.callback);
  }

  public class HeadsetBroadcastReceiver extends BroadcastReceiver
    {     
        protected HeadsetWatcher watcher; 
        

        public HeadsetBroadcastReceiver(HeadsetWatcher watcher) {
          super();
          this.watcher = watcher; 
        }
        
        mediaButtonReceiver = new ComponentName(getPackageName(), MediaButtonReceiver.class.getName());
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.registerMediaButtonEventReceiver(mediaButtonReceiver);
        //mAudioManager.unregisterMediaButtonEventReceiver(mediaButtonReceiver);
        IntentFilter mediaButtonFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

        @Override        
        BroadcastReceiver brMediaButton = new BroadcastReceiver() {
          public void onReceive(Context context, Intent intent) {
              Log.d("Event", "Media button!");
              this.abortBroadcast();

              KeyEvent key = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
              if(key.getAction() == KeyEvent.ACTION_UP) {
                  int keycode = key.getKeyCode();
                  if(keycode == KeyEvent.KEYCODE_MEDIA_NEXT) {
                      Log.d("TestApp", "Next Pressed");
                      changed(3);
                  } else if(keycode == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                      Log.d("TestApp", "Previous pressed");
                      changed(2);
                  } else if(keycode == KeyEvent.KEYCODE_HEADSETHOOK) {
                      Log.d("TestApp", "Head Set Hook pressed");
                      changed(1);
                  }
              }

          }
        };

        registerReceiver(brMediaButton, mediaButtonFilter);

    }
}