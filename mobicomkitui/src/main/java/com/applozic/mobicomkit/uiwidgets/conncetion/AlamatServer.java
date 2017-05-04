package com.applozic.mobicomkit.uiwidgets.conncetion;

/**
 * Created by Septiawan Aji Pradan on 3/8/2017.
 */

public class AlamatServer {
    private final static String ALAMAT_SERVER = "http://wmms-project.com/meetup/";
    private final static String REGISTER_SELF ="register.php";
    private final static String REGISTER_GROUP ="register-group.php";
    private final static String CREATE_MEETUP = "create-meetup.php";
    private final static String LEAVE_GROUP = "left-group.php";
    private final static String CHEK_MEETUP = "check-meetup.php";
    private final static String ADD_USER = "add-user-group.php";
    private final static String ADD_VOTING = "add-vote.php";
    private final static String CHECK_VOTING ="check-voting.php";
    private final static String CHECK_USER ="check-user.php";
    private final static String ADD_USER_VOTE = "add-user-vote.php";
    private final static String UPDATE_LOCATION = "share-location.php";
    private final static String GET_USER_LOCATION = "get-user-location.php";
    private final static String FINISH_VOTE = "finish-vote.php";
    private final static String FINISH_MEETUP = "finish-meetup.php";
    private final static String LIST_MEETUP = "list-meetup.php";


    private final static String RESUME = "resume.php";

    public static String getAlamatServer() {
        return ALAMAT_SERVER;
    }

    public static String getRegisterSelf() {
        return REGISTER_SELF;
    }

    public static String getRegisterGroup() {
        return REGISTER_GROUP;
    }

    public static String getCreateMeetup() {
        return CREATE_MEETUP;
    }

    public static String getLeaveGroup() {
        return LEAVE_GROUP;
    }

    public static String getChekMeetup() {
        return CHEK_MEETUP;
    }

    public static String getAddUser() {
        return ADD_USER;
    }

    public static String getAddVoting() {
        return ADD_VOTING;
    }

    public static String getCheckVoting() {
        return CHECK_VOTING;
    }

    public static String getCheckUser() {
        return CHECK_USER;
    }

    public static String getAddUserVote() {
        return ADD_USER_VOTE;
    }

    public static String getUpdateLocation() {
        return UPDATE_LOCATION;
    }

    public static String getGetUserLocation() {
        return GET_USER_LOCATION;
    }

    public static String getFinishVote() {
        return FINISH_VOTE;
    }

    public static String getFinishMeetup() {
        return FINISH_MEETUP;
    }

    public static String getListMeetUp() {
        return getListMeetup();
    }

    public static String getListMeetup() {
        return LIST_MEETUP;
    }

    public static String getRESUME() {
        return RESUME;
    }
}
