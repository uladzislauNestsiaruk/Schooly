package com.egormoroz.schooly;

import com.egormoroz.schooly.ui.main.UserInformation;

import java.util.ArrayList;

public class Callbacks {
    public interface UniqueNick{
        void uniqueNicknameCallback(boolean isUnique);
    }
    public interface hasGoogleUser{
        void hasGoogleUserCallback(boolean hasThisUser);
    }
    public interface passUserDataBetweenFragments{
        void passUserData(UserInformation info);
    }
    public interface LoadUserDataInterface{
        ArrayList<UserInformation> LoadData(ArrayList<UserInformation> data);
    }
}
