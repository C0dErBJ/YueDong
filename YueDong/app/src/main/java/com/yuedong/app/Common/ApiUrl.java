package com.yuedong.app.Common;

import com.yuedong.app.utils.ApplicationConfigration;

/**
 * Created by C0dEr on 16/1/11.
 */
public class ApiUrl {

    public static String getLoginUrl() {
        return ApplicationConfigration.getRemoteUrl() + LoginUrl;
    }

    public static String getSignUpUrl() {
        return ApplicationConfigration.getRemoteUrl() + SignUpUrl;
    }

    private final static String LoginUrl = "api/User/Login";

    private final static String SignUpUrl = "api/User/Regist";

    private final static String InsertFreeTime = "api/User/InsertFreeTime";

    private final static String RemoveFreeTime = "api/User/RemoveFreeTime";

    private final static String UpdateFreeTime = "api/User/UpdateFreeTime";

    private final static String FreeTime = "api/User/FreeTime/";

    private final static String UpdateUserInfo = "api/User/UpdateUserDetail";

    public static String getUsers() {
        return ApplicationConfigration.getRemoteUrl() + Users;
    }

    private final static String Users = "api/User";

    public static String getGetMessage() {
        return ApplicationConfigration.getRemoteUrl() + GetMessage;
    }

    private final static String GetMessage = "api/Message/GetMessage/";

    private final static String Replay = "api/Message/Replay";

    private final static String NewMessage = "api/Message/NewMessage";

    public static String getReplay() {
        return ApplicationConfigration.getRemoteUrl() + Replay;
    }

    public static String getNewMessage() {
        return ApplicationConfigration.getRemoteUrl() + NewMessage;
    }

    public static String getGetTargetMessage() {
        return ApplicationConfigration.getRemoteUrl() + GetTargetMessage;
    }

    private final static String GetTargetMessage = "api/Message/GetTargetMessage/";

    public static String getGetLatestMessage() {
        return ApplicationConfigration.getRemoteUrl() + GetLatestMessage;
    }

    private final static String GetLatestMessage = "api/Message/GetLatestMessage/";

    public static String getUpdateUserInfo() {
        return ApplicationConfigration.getRemoteUrl() + UpdateUserInfo;
    }

    public static String getInsertSportUrl() {
        return ApplicationConfigration.getRemoteUrl() + InsertSportUrl;
    }

    private final static String InsertSportUrl = "api/User/Sport";

    public static String getFreeTime() {
        return ApplicationConfigration.getRemoteUrl() + FreeTime;
    }

    public static String getUpdateFreeTime() {
        return ApplicationConfigration.getRemoteUrl() + UpdateFreeTime;
    }

    public static String getInsertFreeTime() {
        return ApplicationConfigration.getRemoteUrl() + InsertFreeTime;
    }

    public static String getRemoveFreeTime() {
        return ApplicationConfigration.getRemoteUrl() + RemoveFreeTime;
    }

    public static String getPictureUrl() {
        return ApplicationConfigration.getRemoteUrl() + PictureUrl;
    }

    private final static String PictureUrl = "api/Picture";

    public static String getSportUrl() {
        return ApplicationConfigration.getRemoteUrl() + SportUrl;
    }

    private final static String SportUrl = "api/User/Sport/";

    public static String getNearByStadium() {
        return ApplicationConfigration.getRemoteUrl() + NearByStadium;
    }

    private final static String NearByStadium = "api/Stadium";

    private final static String WeathUrl = "http://apis.baidu.com/heweather/weather/free";
    private final static String ApiWeather = "9a41c60bd56f9dd0c82fb5f69aaf52a0";

    public static String getWeathUrl() {
        return WeathUrl;
    }

    public static String getApiWeather() {
        return ApiWeather;
    }
}
