package com.kidsgames.menu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.vending.billing.IInAppBillingService;
import com.bugsense.trace.BugSenseHandler;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.vikinc.coloring.R;

public class MainMenu extends Activity implements OnClickListener {


    private final String ITEM_SKU = "com.pack2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        findViewById(R.id.pack1_button).setOnClickListener(this);
        findViewById(R.id.pack2_button).setOnClickListener(this);

        Tracker tracker = GoogleAnalytics.getInstance(this).getTracker("UA-51610813-3");
        tracker.send(MapBuilder.createAppView().set(Fields.SCREEN_NAME, "Home Screen").build());

        BugSenseHandler.initAndStartSession(MainMenu.this, "718d6664");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pack1_button:
                Intent intent = new Intent(this, DrawerSliderActivity.class);
                intent.putExtra("pack", "pack1");
                startActivity(intent);
                break;
            case R.id.pack2_button:

                Tracker tracker = GoogleAnalytics.getInstance(this).getTracker("UA-51610813-3");
                tracker.send(MapBuilder
                        .createEvent("Buy", "pack", "try_to_buy", null)
                        .build());

                if (deviceHasGoogleAccount())
                    buyClick(v);
                else {
                    tracker.send(MapBuilder
                            .createEvent("Buy", "pack", "no_account", null)
                            .build());
                }

                break;
        }
    }

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Intent intent = new Intent(this, DrawerSliderActivity.class);
                    intent.putExtra("pack", "pack2");
                    startActivity(intent);

                    Tracker tracker = GoogleAnalytics.getInstance(this).getTracker("UA-51610813-3");
                    tracker.send(MapBuilder
                            .createEvent("Buy", "pack", "item_bouth", null)
                            .build());
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceConn != null) {
            unbindService(mServiceConn);
        }

    }


    private boolean deviceHasGoogleAccount() {
        AccountManager accMan = AccountManager.get(this);
        Account[] accArray = accMan.getAccountsByType("com.google");
        return accArray.length >= 1 ? true : false;
    }

    public void buyClick(View view) {

        try {

            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(), ITEM_SKU, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            int s = buyIntentBundle.getInt("RESPONSE_CODE");
            switch (s) {
                case 0:
                    PendingIntent pendingIntent = buyIntentBundle
                            .getParcelable("BUY_INTENT");
                    startIntentSenderForResult(pendingIntent.getIntentSender(),
                            1001, new Intent(), 0,
                            0, 0);
                    break;
                case 7:
                    Intent intent = new Intent(this, DrawerSliderActivity.class);
                    intent.putExtra("pack", "pack2");
                    startActivity(intent);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
