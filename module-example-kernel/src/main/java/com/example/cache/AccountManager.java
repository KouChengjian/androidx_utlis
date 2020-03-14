package com.example.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.utlis.PreferencesUtils;
import com.yiciyuan.kernel.app.BaseApp;


/**
 * Create by AItsuki on 2018/7/18.
 */
public class AccountManager {

    private final String PREFERENCE_NAME = "basic_cache";

    // 用户
    private final String USER_FIRST_IN = "user_first_in";
    private final String USER_ID = "user_id";
    private final String SESSION = "user_session";
    // 公共
    private final String XML_KEY_URL_POSITION = "xml_key_url_position";

    private static AccountManager manager;

    private SharedPreferences preference;

    public AccountManager(Context context) {
        preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static AccountManager getInstance() {
        if (manager == null) {
            manager = new AccountManager(BaseApp.app());
        }
        return manager;
    }

    public Context getContext() {
        return BaseApp.app();
    }

//    public void login(UserEntity userEntity) {
//        UserDao.INSTANCE.deleteAll();
//        UserDao.INSTANCE.save(userEntity);
//        // TODO: 需要缓存的
//        setUserId(userEntity.getUserId());
//
//    }

    public void logout() {
//        UserDao.INSTANCE.deleteAll();
//        preference.edit().clear().apply();
//        setUserId(0);
//        setSession("");
//        EventBusConfig.postUserEventBus(new UserEntity());
    }

//    public void updata(UserEntity userEntity) {
//        // TODO: 更新用户信息的时候
//        UserDao.INSTANCE.save(userEntity);
//        // TODO: 需要缓存的
//        setUserId(userEntity.getUserId());
//    }

//    public UserEntity getCurrentUser() {
//        UserEntity userEntity = UserDao.INSTANCE.getCurrentUser();
//        if (userEntity == null) {
//            userEntity = new UserEntity();
//        }
//        return userEntity;
//    }

    /**
     * -----------------------------------------------------USER------------------------------------------------------------
     */

    public boolean isLogin() {
        if (getUserId() != 0 && !TextUtils.isEmpty(getSession())) {
            return true;
        }
        return false;
    }

    public void setUserId(int uid) {
        PreferencesUtils.putInt(getContext(), USER_ID, uid);
    }

    public int getUserId() {
        return PreferencesUtils.getInt(getContext(), USER_ID, 0);
    }

    public void setSession(String session) {
        PreferencesUtils.putString(getContext(), SESSION, session);
    }

    public String getSession() {
        return PreferencesUtils.getString(getContext(), SESSION, "");
    }

    public void setUserFirstIn(boolean b) {
        PreferencesUtils.putBoolean(getContext(), USER_FIRST_IN, b);
    }

    public boolean isUserFirstIn() {
        return PreferencesUtils.getBoolean(getContext(), USER_FIRST_IN, true);
    }


    /**
     * -----------------------------------------------------Comm------------------------------------------------------------
     */
    public int getServerUrl(Context context) {
        int position = PreferencesUtils.getInt(context, XML_KEY_URL_POSITION, 0);
        return position;
    }

    public void setServerUrl(Context context, int position) {
        PreferencesUtils.putInt(context, XML_KEY_URL_POSITION, position);
    }
}
