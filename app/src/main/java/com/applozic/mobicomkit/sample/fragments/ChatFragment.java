package com.applozic.mobicomkit.sample.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.attachment.FileClientService;
import com.applozic.mobicomkit.api.conversation.MobiComMessageService;
import com.applozic.mobicomkit.broadcast.ConnectivityReceiver;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.contact.BaseContactService;
import com.applozic.mobicomkit.feed.TopicDetail;
import com.applozic.mobicomkit.sample.R;
import com.applozic.mobicomkit.uiwidgets.AlCustomizationSettings;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.MobiComKitBroadcastReceiver;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.ConversationFragment;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.MobiComQuickConversationFragment;
import com.applozic.mobicomkit.uiwidgets.instruction.ApplozicPermissions;
import com.applozic.mobicomkit.uiwidgets.people.fragment.ProfileFragment;
import com.applozic.mobicommons.people.SearchListFragment;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.channel.Conversation;
import com.applozic.mobicommons.people.contact.Contact;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import java.io.File;
import java.util.Calendar;


/**
 * Created by kaddafi on 28/02/2017.
 */

public class ChatFragment extends Fragment {

    public static final int LOCATION_SERVICE_ENABLE = 1001;
    public static final String TAKE_ORDER = "takeOrder";
    public static final String CONTACT = "contact";
    public static final String CHANNEL = "channel";
    public static final String CONVERSATION_ID = "conversationId";
    public static final String GOOGLE_API_KEY_META_DATA = "com.google.android.geo.API_KEY";
    public static final String ACTIVITY_TO_OPEN_ONCLICK_OF_CALL_BUTTON_META_DATA = "activity.open.on.call.button.click";
    protected static final long UPDATE_INTERVAL = 500;
    protected static final long FASTEST_INTERVAL = 1;
    private static final String LOAD_FILE = "loadFile";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final String API_KYE_STRING = "YOUR_GEO_API_KEY";
    private static final String CAPTURED_IMAGE_URI = "capturedImageUri";
    private static final String CAPTURED_VIDEO_URI = "capturedVideoUri";
    private static final String SHARE_TEXT = "share_text";
    private static Uri capturedImageUri;
    private static String inviteMessage;
    protected ConversationFragment conversation;
    protected MobiComQuickConversationFragment quickConversationFragment;
    protected MobiComKitBroadcastReceiver mobiComKitBroadcastReceiver;
    protected ActionBar mActionBar;
    protected GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    public Contact contact;
    private Channel channel;
    private static int retry;
    public LinearLayout layout;
    String geoApiKey;
    String activityToOpenOnClickOfCallButton;
    int resourceId;
    RelativeLayout childFragmentLayout;
    public boolean isTakePhoto;
    public boolean isAttachment;
    private BaseContactService baseContactService;
    private ApplozicPermissions applozicPermission;
    public Integer currentConversationId;
    private Uri videoFileUri;
    private Uri imageUri;
    ProfileFragment profilefragment;
    MobiComMessageService mobiComMessageService;
    private ConversationUIService conversationUIService;
    private SearchView searchView;
    private String searchTerm;
    private SearchListFragment searchListFragment;
    AlCustomizationSettings alCustomizationSettings;
    ConnectivityReceiver connectivityReceiver;
    private Calendar calendar;
    File mediaFile;
    File profilePhotoFile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }
}
