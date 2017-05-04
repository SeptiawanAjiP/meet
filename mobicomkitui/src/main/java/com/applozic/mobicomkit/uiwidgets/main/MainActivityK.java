package com.applozic.mobicomkit.uiwidgets.main;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.attachment.FileClientService;
import com.applozic.mobicomkit.api.conversation.ApplozicMqttIntentService;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.broadcast.BroadcastService;
import com.applozic.mobicomkit.uiwidgets.AlCustomizationSettings;

import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.MessageCommunicator;
import com.applozic.mobicomkit.uiwidgets.conversation.MobiComKitBroadcastReceiver;
import com.applozic.mobicomkit.uiwidgets.conversation.UIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ChannelCreateActivity;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.MobiComKitActivityInterface;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.ConversationFragment;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.MobiComQuickConversationFragment;
import com.applozic.mobicomkit.uiwidgets.instruction.ApplozicPermissions;
import com.applozic.mobicomkit.uiwidgets.meetup.MeetUpFragment;
import com.applozic.mobicomkit.uiwidgets.people.channel.ChannelFragment;
import com.applozic.mobicomkit.uiwidgets.people.contact.AppContactFragment;
import com.applozic.mobicomkit.uiwidgets.people.fragment.ProfileFragment;
import com.applozic.mobicomkit.uiwidgets.people.fragment.UserProfileFragment;
import com.applozic.mobicommons.commons.core.utils.PermissionsUtils;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.file.FileUtils;
import com.applozic.mobicommons.json.GsonUtils;
import com.applozic.mobicommons.people.OnContactsInteractionListener;
import com.applozic.mobicommons.people.SearchListFragment;
import com.applozic.mobicommons.people.contact.Contact;
import com.applozic.mobicommons.people.contact.ContactUtils;

import com.applozic.mobicommons.people.channel.Channel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivityK extends AppCompatActivity implements OnContactsInteractionListener,
        SearchView.OnQueryTextListener,TabLayout.OnTabSelectedListener,MessageCommunicator, MobiComKitActivityInterface {

    public static final String SHARED_TEXT = "SHARED_TEXT";
    public static final String FORWARD_MESSAGE = "forwardMessage";
    private static final String CONTACT_ID = "contactId";
    private static final String GROUP_ID = "groupId";
    private static final String GROUP_NAME = "groupName";
    private static final String USER_ID = "userId";
    public static final String USER_ID_ARRAY = "userIdArray";
    protected SearchView searchView;
    protected String searchTerm;
    private SearchListFragment searchListFragment;
    private boolean isSearchResultView = false;
    ViewPager viewPager;
    TabLayout tabLayout;
    ActionBar actionBar;
    String[] userIdArray;
    public static boolean isSearching = false;
    AppContactFragment appContactFragment;
    ChannelFragment channelFragment;
    MeetUpFragment meetUpFragment;
    ProfileFragment userProfile;
    ViewPagerAdapter adapter;
    AlCustomizationSettings alCustomizationSettings;
    ProfileFragment profilefragment;
    public LinearLayout layout;
    private Uri imageUri;
    File profilePhotoFile;
    private ApplozicPermissions applozicPermission;

    private static int retry;
    ConversationUIService conversationUIService;
    MobiComQuickConversationFragment mobiComQuickConversationFragment;
    MobiComKitBroadcastReceiver mobiComKitBroadcastReceiver;

    public Snackbar snackbar;

    private Integer[] iconTab = {
            R.drawable.selector_ic_chat,
            R.drawable.selector_ic_contact,
            R.drawable.selector_ic_group,
            R.drawable.selector_ic_meetup
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_k);
        String jsonString = FileUtils.loadSettingsJsonFile(getApplicationContext());
        if(!TextUtils.isEmpty(jsonString)){
            alCustomizationSettings = (AlCustomizationSettings) GsonUtils.getObjectFromJson(jsonString,AlCustomizationSettings.class);
        }else {
            alCustomizationSettings =  new AlCustomizationSettings();
        }
        appContactFragment = new AppContactFragment(userIdArray);
        appContactFragment.setAlCustomizationSettings(alCustomizationSettings);

        channelFragment = new ChannelFragment();
        meetUpFragment = new MeetUpFragment();
//        userProfile = new ProfileFragment();
//        userProfile.setAlCustomizationSettings(alCustomizationSettings);

        mobiComQuickConversationFragment = new MobiComQuickConversationFragment();
        conversationUIService = new ConversationUIService(this, mobiComQuickConversationFragment);
        mobiComKitBroadcastReceiver = new MobiComKitBroadcastReceiver(this, mobiComQuickConversationFragment);
        new MobiComConversationService(this).processLastSeenAtStatus();
//        addFragment(this, mobiComQuickConversationFragment, ConversationUIService.QUICK_CONVERSATION_FRAGMENT); //here we are adding fragment

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        if (getIntent().getExtras() != null) {
            userIdArray = getIntent().getStringArrayExtra(USER_ID_ARRAY);
        }
//        setSearchListFragment(appContactFragment);
        if (alCustomizationSettings.isStartNewGroup()) {
            actionBar.setTitle(getString(R.string.app_name));
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            viewPager.setVisibility(View.VISIBLE);
            setupViewPager(viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setOnTabSelectedListener(this);
        } else {
            actionBar.setTitle(getString(R.string.app_name));
            Log.d("DEBUG","appcontact jalan");
            addFragment(this,appContactFragment , "AppContactFragment");
        }
      /*  mContactsListFragment = (AppContactFragment)
                getSupportFragmentManager().findFragmentById(R.id.contact_list);*/

        // This flag notes that the Activity is doing a search, and so the result will be
        // search results rather than all contacts. This prevents the Activity and Fragment
        // from trying to a search on search results.
        isSearchResultView = true;

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();

        String searchQuery = intent.getStringExtra(SearchManager.QUERY);
        // Set special title for search results
        String title = getString(R.string.contacts_list_search_results_title, searchQuery);
        setTitle(title);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(iconTab[i]);
        }

        if (intent.getBooleanExtra("isFirst",false)){
            tabLayout.getTabAt(1).select();
        } else {
            tabLayout.getTabAt(0).select();
        }

        layout = (LinearLayout) findViewById(R.id.footerAd);
        applozicPermission = new ApplozicPermissions(this, layout);
        if (Utils.hasMarshmallow()) {
            applozicPermission.checkRuntimePermissionForStorage();
        }
        profilefragment =  new ProfileFragment();
        profilefragment.setAlCustomizationSettings(alCustomizationSettings);

      /*  if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mContactsListFragment.onQueryTextChange(searchQuery);
        }*/
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        if (Utils.hasICS()) {
            searchItem.collapseActionView();
        }
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconified(true);

        return super.onCreateOptionsMenu(menu);
    }

    public static void addFragment(FragmentActivity fragmentActivity, Fragment fragmentToAdd, String fragmentTag) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = supportFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.layout_child_activity, fragmentToAdd,
                fragmentTag);

        if (supportFragmentManager.getBackStackEntryCount() > 1) {
            supportFragmentManager.popBackStack();
        }
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
    }

    /**
     * This interface callback lets the main contacts list fragment notify
     * this activity that a contact has been selected.
     *
     * @param contactUri The contact Uri to the selected contact.
     */
    @Override
    public void onContactSelected(Uri contactUri) {
        Long contactId = ContactUtils.getContactId(getContentResolver(), contactUri);
        Map<String, String> phoneNumbers = ContactUtils.getPhoneNumbers(getApplicationContext(), contactId);

        if (phoneNumbers.isEmpty()) {
            Toast toast = Toast.makeText(this.getApplicationContext(), R.string.phone_number_not_present, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

//        Intent intent = new Intent(this, ConversationActivity.class);
//        intent.putExtra(ConversationUIService.CONVERSATION_ID, conversationId);
//        intent.putExtra(ConversationUIService.SEARCH_STRING, searchString);
//        intent.putExtra(ConversationUIService.TAKE_ORDER, true);
//        if (phoneNumbers != null) {
//            intent.putExtra(ConversationUIService.USER_ID, contactId);
//            intent.putExtra(ConversationUIService.DISPLAY_NAME, contact.getDisplayName());
//            startActivity(intent);
//        }
//        else if (channel != null) {
//            intent.putExtra(ConversationUIService.GROUP_ID, channel.getKey());
//            intent.putExtra(ConversationUIService.GROUP_NAME, channel.getName());
//            startActivity(intent);
//
//        }

//        Intent intent = new Intent();
//        intent.putExtra(CONTACT_ID, contactId);
//        intent.setData(contactUri);
//        finishActivity(intent);
    }

    public void startNewConversation(String contactNumber) {
        Intent intent = new Intent();
        intent.putExtra(USER_ID, contactNumber);
        finishActivity(intent);
    }

    @Override
    public void onGroupSelected(Channel channel) {
        Intent intent = new Intent(this,ConversationActivity.class);
        intent.putExtra(GROUP_ID, channel.getKey());
        intent.putExtra(GROUP_NAME, channel.getName());
        startActivity(intent);
//        finishActivity(intent);
    }

    @Override
    public void onCustomContactSelected(Contact contact) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(USER_ID, contact.getUserId());
        intent.putExtra(ConversationUIService.DISPLAY_NAME, contact.getDisplayName());
        startActivity(intent);
//        finishActivity(intent);

//        Intent intent = new Intent(this, ConversationActivity.class);
//        intent.putExtra(ConversationUIService.CONVERSATION_ID, conversationId);
//        intent.putExtra(ConversationUIService.SEARCH_STRING, searchString);
//        intent.putExtra(ConversationUIService.TAKE_ORDER, true);
//        if (contact != null) {
//            intent.putExtra(ConversationUIService.USER_ID, contact.getUserId());
//            intent.putExtra(ConversationUIService.DISPLAY_NAME, contact.getDisplayName());
//            startActivity(intent);
//        } else if (channel != null) {
//            intent.putExtra(ConversationUIService.GROUP_ID, channel.getKey());
//            intent.putExtra(ConversationUIService.GROUP_NAME, channel.getName());
//            startActivity(intent);
//
//        }
    }

    public void finishActivity(Intent intent) {
        String forwardMessage = getIntent().getStringExtra(FORWARD_MESSAGE);
        if (!TextUtils.isEmpty(forwardMessage)) {
            intent.putExtra(FORWARD_MESSAGE, forwardMessage);
        }

        String sharedText = getIntent().getStringExtra(SHARED_TEXT);
        if (!TextUtils.isEmpty(sharedText)) {
            intent.putExtra(SHARED_TEXT, sharedText);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSelectionCleared() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
            // For platforms earlier than Android 3.0, triggers the search activity
        } else if(i == R.id.menu_profile){
            profilefragment.setApplozicPermissions(applozicPermission);
            addFragment(this,profilefragment,ProfileFragment.ProfileFragmentTag);
        } else if (i == R.id.menu_search) {// if (!Utils.hasHoneycomb()) {
            onSearchRequested();
            //}
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        // Don't allow another search if this activity instance is already showing
        // search results. Only used pre-HC.
        return !isSearchResultView && super.onSearchRequested();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (alCustomizationSettings.isCreateAnyContact()) {
            this.searchTerm = query;
            startNewConversation(query);
            isSearching = false;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        this.searchTerm = query;
        if (getSearchListFragment() != null) {
            getSearchListFragment().onQueryTextChange(query);
            isSearching = true;

            if (query.isEmpty()) {
                isSearching = false;
            }
        }
        return true;
    }

    public SearchListFragment getSearchListFragment() {
        return searchListFragment;
    }

    public void setSearchListFragment(SearchListFragment searchListFragment) {
        this.searchListFragment = searchListFragment;
    }



    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(mobiComQuickConversationFragment,"Chat");
        adapter.addFrag(appContactFragment, "Contact");
        adapter.addFrag(channelFragment, "Group");
        adapter.addFrag(meetUpFragment, "MeetUp");
//        adapter.addFrag(userProfile,"Profile");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition(), true);
        switch (tab.getPosition()) {
            case 0:
                setSearchListFragment((MobiComQuickConversationFragment)adapter.getItem(0));
                if(getSearchListFragment() != null){
                    getSearchListFragment().onQueryTextChange(null);
                }
                actionBar.setTitle("Conversation");
                break;
            case 1:
                setSearchListFragment((AppContactFragment)adapter.getItem(1));
//                if(getSearchListFragment() != null){
//                    getSearchListFragment().onQueryTextChange(null);
//                }
                actionBar.setTitle("Contact");
                break;
            case 2:
                setSearchListFragment((ChannelFragment)adapter.getItem(2));
//                if(getSearchListFragment() != null){
//                    getSearchListFragment().onQueryTextChange(null);
//                }
                actionBar.setTitle("Group");
                break;
            case 3:
                actionBar.setTitle("MeetUp");
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition(),true);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            fragmentList.add(fragment);
            titleList.add(title);
        }

    }

    @Override
    public void onQuickConversationFragmentItemClick(View view, Contact contact, Channel channel, Integer conversationId, String searchString) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(ConversationUIService.CONVERSATION_ID, conversationId);
        intent.putExtra(ConversationUIService.SEARCH_STRING, searchString);
        intent.putExtra(ConversationUIService.TAKE_ORDER, true);
        if (contact != null) {
            intent.putExtra(ConversationUIService.USER_ID, contact.getUserId());
            intent.putExtra(ConversationUIService.DISPLAY_NAME, contact.getDisplayName());
            startActivity(intent);
        } else if (channel != null) {
            intent.putExtra(ConversationUIService.GROUP_ID, channel.getKey());
            intent.putExtra(ConversationUIService.GROUP_NAME, channel.getName());
            startActivity(intent);

        }
    }

    @Override
    public void startContactActivityForResult() {
        conversationUIService.startContactActivityForResult();
    }

    @Override
    public void addFragment(ConversationFragment conversationFragment) {

    }

    @Override
    public void updateLatestMessage(Message message, String formattedContactNumber) {
        conversationUIService.updateLatestMessage(message, formattedContactNumber);
    }

    @Override
    public void removeConversation(Message message, String formattedContactNumber) {
        conversationUIService.removeConversation(message, formattedContactNumber);
    }

    @Override
    public void showErrorMessageView(String errorMessage) {

    }

    @Override
    public void retry() {
        retry++;
    }

    @Override
    public int getRetryCount() {
        return retry;
    }

    @Override
    protected void onStop() {
        super.onStop();
        final String deviceKeyString = MobiComUserPreference.getInstance(this).getDeviceKeyString();
        final String userKeyString = MobiComUserPreference.getInstance(this).getSuUserKeyString();
        Intent intent = new Intent(this, ApplozicMqttIntentService.class);
        intent.putExtra(ApplozicMqttIntentService.USER_KEY_STRING, userKeyString);
        intent.putExtra(ApplozicMqttIntentService.DEVICE_KEY_STRING, deviceKeyString);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mobiComKitBroadcastReceiver, BroadcastService.getIntentFilter());
        Intent subscribeIntent = new Intent(this, ApplozicMqttIntentService.class);
        subscribeIntent.putExtra(ApplozicMqttIntentService.SUBSCRIBE, true);
        startService(subscribeIntent);

        if (!Utils.isInternetAvailable(this)) {
            String errorMessage = getResources().getString(R.string.internet_connection_not_available);
            showErrorMessageView(errorMessage);
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mobiComKitBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            conversationUIService.onActivityResult(requestCode, resultCode, data);
            handleOnActivityResult(requestCode,data);
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    if(data == null){
                        return;
                    }
                    if(imageUri != null){
                        imageUri = result.getUri();
                        if (imageUri != null && profilefragment != null) {
                            profilefragment.handleProfileimageUpload(false,imageUri,profilePhotoFile);
                        }
                    }else {
                        imageUri = result.getUri();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + "_" + ".jpeg";
                        profilePhotoFile = FileClientService.getFilePath(imageFileName,this, "image/jpeg");
                        if (imageUri != null && profilefragment != null) {
                            profilefragment.handleProfileimageUpload(true,imageUri,profilePhotoFile);
                        }
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void handleOnActivityResult(int requestCode, Intent intent) {

        switch (requestCode) {

            case ProfileFragment.REQUEST_CODE_ATTACH_PHOTO:
                Uri selectedFileUri = (intent == null ? null : intent.getData());
                beginCrop(selectedFileUri);
                break;

            case ProfileFragment.REQUEST_CODE_TAKE_PHOTO:
                beginCrop(imageUri);
                break;

        }
    }

    void beginCrop(Uri imageUri){
        try{
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .setMultiTouchEnabled(true)
                    .start(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionsUtils.REQUEST_CAMERA_FOR_PROFILE_PHOTO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBar(R.string.phone_camera_permission_granted);
                if(profilefragment != null){
                    profilefragment.processPhotoOption();
                }
            } else {
                showSnackBar(R.string.phone_camera_permission_not_granted);
            }
        }else if (requestCode == PermissionsUtils.REQUEST_STORAGE_FOR_PROFILE_PHOTO) {
            if (PermissionsUtils.verifyPermissions(grantResults)) {
                showSnackBar(R.string.storage_permission_granted);
                if(profilefragment != null){
                    profilefragment.processPhotoOption();
                }
            } else {
                showSnackBar(R.string.storage_permission_not_granted);
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void showSnackBar(int resId) {
        snackbar = Snackbar.make(layout, resId,
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
