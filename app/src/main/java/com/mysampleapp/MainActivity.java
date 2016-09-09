//
// Copyright 2016 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.7
//
package com.mysampleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentDownloadPolicy;
import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentListHandler;
import com.amazonaws.mobile.content.ContentManager;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.mysampleapp.Waffle.WaffleShowFragment;
import com.mysampleapp.demo.DemoConfiguration;
import com.mysampleapp.demo.HomeDemoFragment;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import com.mysampleapp.demo.nosql.DemoNoSQLOperation;
import com.mysampleapp.demo.nosql.DemoNoSQLResult;
import com.mysampleapp.demo.nosql.DemoNoSQLTableWaffles;
import com.mysampleapp.demo.nosql.DemoNoSQLWafflesResult;
import com.mysampleapp.demo.nosql.DynamoDBUtils;
import com.mysampleapp.demo.nosql.WafflesDO;
import com.mysampleapp.navigation.NavigationDrawer;
import com.mysampleapp.demo.UserSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    /** Class name for log messages. */
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    /** Bundle key for saving/restoring the toolbar title. */
    private final static String BUNDLE_KEY_TOOLBAR_TITLE = "title";

    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;

    private ContentManager contentManager;

    /** Our navigation drawer class for handling navigation drawer logic. */
    private NavigationDrawer navigationDrawer;

    /** The helper class used to toggle the left navigation drawer open and closed. */
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();

        AWSMobileClient.defaultMobileClient().
                createDefaultContentManager(new ContentManager.BuilderResultHandler() {
                    @Override
                    public void onComplete(final ContentManager contentManager) {

                        MainActivity.this.contentManager = contentManager;
                    }
                });

        setContentView(R.layout.activity_waffle_main);

//        setupToolbar(savedInstanceState);

//        setupNavigationMenu(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.new_button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButton1Pressed();
            }
        });


        findViewById(R.id.new_button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButton2Pressed();
            }
        });

        cycleWaffle();

        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();
        // register settings changed receiver.
        LocalBroadcastManager.getInstance(this).registerReceiver(settingsChangedReceiver,
            new IntentFilter(UserSettings.ACTION_SETTINGS_CHANGED));
        updateColor();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here excluding the home button.

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onClick(final View view) {
    }
    
    private final BroadcastReceiver settingsChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "Received settings changed local broadcast. Update theme colors.");
            updateColor();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(settingsChangedReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void updateColor() {
        final UserSettings userSettings = UserSettings.getInstance(getApplicationContext());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {
                userSettings.loadFromDataset();
                return null;
            }

            @Override
            protected void onPostExecute(final Void aVoid) {
                final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
                if (fragment != null) {
                    final View fragmentView = fragment.getView();
                    if (fragmentView != null) {
                        fragmentView.setBackgroundColor(userSettings.getBackgroudColor());
                    }
                }
            }
        }.execute();
    }
    
    
    
    ///////////////////////////////////////////////////////////////////////////
    //              FRAGMENT STUFF
    ///////////////////////////////////////////////////////////////////////////

    private WafflesDO currentWaffleObject;
    private WafflesDO oldWaffleObject;

    private ContentProgressListener progressListener1 = new ContentProgressListener() {
        @Override
        public void onSuccess(ContentItem contentItem) {
            showImage(contentItem, 1);
        }

        @Override
        public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {

        }

        @Override
        public void onError(String filePath, Exception ex) {

        }
    };

    private ContentProgressListener progressListener2 = new ContentProgressListener() {
        @Override
        public void onSuccess(ContentItem contentItem) {
            showImage(contentItem, 2);
        }

        @Override
        public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {

        }

        @Override
        public void onError(String filePath, Exception ex) {

        }
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaffleShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaffleShowFragment newInstance(String param1, String param2) {
        WaffleShowFragment fragment = new WaffleShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButton1Pressed() {
        currentWaffleObject.setVote1(currentWaffleObject.getVote1() + 1);
        updateWaffle(currentWaffleObject);
        cycleWaffle();
    }

    public void onButton2Pressed() {
        currentWaffleObject.setVote2(currentWaffleObject.getVote2() + 1);
        updateWaffle(currentWaffleObject);
        cycleWaffle();
    }

    private void updateWaffle(WafflesDO waffle){

        final HashMap<String, AttributeValue> item = new HashMap<String, AttributeValue>();

        AttributeValue tmpVal = new AttributeValue();
        tmpVal.setN(String.valueOf(waffle.getQId()));
        item.put("qId", tmpVal);

        tmpVal = new AttributeValue();
        tmpVal.setS(waffle.get_caption());
        item.put("caption", tmpVal);

        tmpVal = new AttributeValue();
        tmpVal.setS(waffle.getOpt1());
        item.put("opt1", tmpVal);

        tmpVal = new AttributeValue();
        tmpVal.setS(waffle.getOpt2());
        item.put("opt2", tmpVal);

        tmpVal = new AttributeValue();
        tmpVal.setN(String.valueOf(waffle.getVote1()));
        item.put("vote1", tmpVal);

        tmpVal = new AttributeValue();
        tmpVal.setN(String.valueOf(waffle.getVote2()));
        item.put("vote2", tmpVal);

        tmpVal = new AttributeValue();
        tmpVal.setN(String.valueOf(waffle.getTerm()));
        item.put("term", tmpVal);

        tmpVal = new AttributeValue();
        tmpVal.setS(waffle.getOwnerId());
        item.put("ownerId", tmpVal);

        Runnable putItem = new Runnable(){
            @Override
            public void run() {
                AWSMobileClient.defaultMobileClient().getDynamoDBClient().putItem(WafflesDO.getTableName(), item);
            }
        };
        new Thread(putItem).start();
    }

    private void cycleWaffle(){

        oldWaffleObject = currentWaffleObject;

        ((ImageButton)findViewById(R.id.old_button_1)).setImageDrawable(((ImageButton)findViewById(R.id.new_button_1)).getDrawable());
        ((ImageButton)findViewById(R.id.old_button_2)).setImageDrawable(((ImageButton)findViewById(R.id.new_button_2)).getDrawable());
        ((ImageButton)findViewById(R.id.new_button_1)).setImageDrawable(null);
        ((ImageButton)findViewById(R.id.new_button_2)).setImageDrawable(null);
        if(oldWaffleObject != null) {
            ((TextView) findViewById(R.id.votes_1)).setText(String.valueOf(oldWaffleObject.getVote1()));
            ((TextView) findViewById(R.id.votes_2)).setText(String.valueOf(oldWaffleObject.getVote2()));
            ((TextView) findViewById(R.id.votes_1)).setTextColor(Color.rgb(255, 255, 255));
            ((TextView) findViewById(R.id.votes_2)).setTextColor(Color.rgb(255, 255, 255));
        }

        getRandomWaffle(getApplicationContext());
    }

    public void getRandomWaffle(final Context context){

        final DemoNoSQLTableWaffles.WaffleTestOperation operation = new DemoNoSQLTableWaffles.WaffleTestOperation(context);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean foundResults = false;
                try {
                    foundResults = operation.executeOperation();
                } catch (final AmazonClientException ex) {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(LOG_TAG, String.format("Failed executing selected DynamoDB table (%s) operation (%s) : %s",
                                    operation.getTitle(), ex.getMessage()), ex);
                        }
                    });
                    return;
                }

                if (!foundResults) {
                    return;//handleNoResultsFound();
                } else {
                    currentWaffleObject = ((DemoNoSQLWafflesResult)operation.getNextResultGroup().get(0)).getResult();
                    System.out.println(currentWaffleObject.toString());
                    retrieveContent();
                }
            }
        }).start();
    }

    private void retrieveContent(){


        this.findViewById(R.id.new_button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButton1Pressed();
            }
        });


        this.findViewById(R.id.new_button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButton2Pressed();
            }
        });

        contentManager.listAvailableContent(currentWaffleObject.getOpt1(), new ContentListHandler() {
            @Override
            public boolean onContentReceived(int startIndex, List<ContentItem> partialResults, boolean hasMoreResults) {
                if(partialResults != null && partialResults.size() != 0){
                    try{
                        for(ContentItem item: partialResults) {
                            contentManager.getContent(item.getFilePath(), item.getSize(), ContentDownloadPolicy.DOWNLOAD_ALWAYS, false, progressListener1);
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        return false;
                    }
                }
                return false;
            }
            @Override
            public void onError(Exception ex) {

            }
        });

        contentManager.listAvailableContent(currentWaffleObject.getOpt2(), new ContentListHandler() {
            @Override
            public boolean onContentReceived(int startIndex, List<ContentItem> partialResults, boolean hasMoreResults) {
                if (partialResults != null && partialResults.size() != 0) {
                    try {
                        for (ContentItem item : partialResults) {
                            contentManager.getContent(item.getFilePath(), item.getSize(), ContentDownloadPolicy.DOWNLOAD_ALWAYS, false, progressListener2);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return false;
            }

            @Override
            public void onError(Exception ex) {

            }
        });



    }

    private void showImage(ContentItem content, int imageNum){
        ImageButton button;
        if(imageNum == 1) {

            button = (ImageButton) findViewById(R.id.new_button_1);
        }else {
            button = (ImageButton) findViewById(R.id.new_button_2);
        } try {

            Drawable d = getAssetImage(getApplicationContext(), content.getFile());

            button.setImageDrawable(d);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static Drawable getAssetImage(Context context, File file) throws IOException {
        InputStream buffer = new FileInputStream(file);
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

}
