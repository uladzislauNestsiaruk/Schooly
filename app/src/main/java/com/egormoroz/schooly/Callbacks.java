package com.egormoroz.schooly;

import com.egormoroz.schooly.ui.main.UserInformation;

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
}
