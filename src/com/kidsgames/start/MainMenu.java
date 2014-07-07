package com.kidsgames.start;

import java.util.ArrayList;

import org.json.JSONObject;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.bugsense.trace.BugSenseHandler;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.vikinc.coloring.R;

public class MainMenu extends Activity implements OnClickListener{
	
	private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAglVFlL6qyAYVOGjT9Rnf+TdoraLGXfq8SMPWrVAEbPG0X/1kRsJnVnJZFspsmugwqdNRUy+BwVcD01Pmez/yBvhSdQmNJlrN5F/X+BuWMRyeOzttb8SegTeLbKVB8+OeRXoJtC8Wocgki2/axUhASYpNJNGgAXA2NcACkNOh5jHNfbb0sleWQ7ta6zpZWs3wOhB1eXuHcFTW81qUfftaL9hUzhc8Vzib7Px5Xy+BpUJVoHNxvTvpwU3qQ9hHJYUR4Y23d8Kkqn2qEm6+di+y07LAgGcWb9toeiEALNtHMyDCAbH8/2+5hEMEPb0vIILYJLDS9CXecicZElwzWxK7DQIDAQAB";

	
	private final String ITEM_SKU = "com.pack2";
//	static final String ITEM_SKU ="buy.pack2";
//	static final String ITEM_SKU = "android.test.purchased";
//	static final String ITEM_SKU = "pack2";
	
	private TextView lock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		 bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"),
			                mServiceConn, Context.BIND_AUTO_CREATE);
		 
		findViewById(R.id.pack1_button).setOnClickListener(this);
		findViewById(R.id.pack2_button).setOnClickListener(this);
		
		lock = (TextView) findViewById(R.id.lock_view);
		
        Tracker tracker = GoogleAnalytics.getInstance(this).getTracker("UA-51610813-3");
        tracker.send(MapBuilder.createAppView().set(Fields.SCREEN_NAME, "Home Screen").build());
        
        BugSenseHandler.initAndStartSession(MainMenu.this, "718d6664");
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
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
			else
			{
		       Toast.makeText(this, R.string.google_acc, Toast.LENGTH_SHORT).show();
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
	       
	       try{
				Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
				int response = ownedItems.getInt("RESPONSE_CODE");
				if (response == 0) {
	
				   ArrayList<String> purchaseDataList = 
				      ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");

				   for (int i = 0; i < purchaseDataList.size(); ++i) {
				      String purchaseData = purchaseDataList.get(i);
				      lock.setVisibility(View.GONE);
				   } 

				   // if continuationToken != null, call getPurchases again 
				   // and pass in the token to retrieve more items
				}
		
			}
	       catch(Exception e)
	       {
	    	   Log.d("Likers" , "E : " + e);
	       }
	       
	   }
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if (requestCode == 1001 && data != null) {           
		      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
		      if (resultCode == Activity.RESULT_OK) {
		         try {
		            JSONObject jo = new JSONObject(purchaseData);
		            String sku = jo.getString("productId");
		            Log.d("Likers","You have bought the " + sku + ". Excellent choice, adventurer!");
		        
					Intent intent = new Intent(this, DrawerSliderActivity.class);
					intent.putExtra("pack", "pack2");
					startActivity(intent);
					
			        Tracker tracker = GoogleAnalytics.getInstance(this).getTracker("UA-51610813-3");
			        tracker.send(MapBuilder
			        	    .createEvent("Buy", "pack", "item_bouth", null)
			        	    .build());
			        
			        lock.setVisibility(View.GONE);
			        
		          }
		          catch (Exception e) {
		             Log.d("Likers","Failed to parse purchase data.");
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

			Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),ITEM_SKU, "inapp","bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
			int s = buyIntentBundle.getInt("RESPONSE_CODE");
			switch (s) {
			case 0:
				PendingIntent pendingIntent = buyIntentBundle
						.getParcelable("BUY_INTENT");
				startIntentSenderForResult(pendingIntent.getIntentSender(),
						1001, new Intent(), Integer.valueOf(0),
						Integer.valueOf(0), Integer.valueOf(0));
				break;
			case 7:
				Intent intent = new Intent(this, DrawerSliderActivity.class);
				intent.putExtra("pack", "pack2");
				startActivity(intent);
				break;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Likers", "Exp : " + e);
			e.printStackTrace();
		}

	}
}
