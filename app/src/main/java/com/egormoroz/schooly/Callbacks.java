package com.egormoroz.schooly;

public class Callbacks {
    interface UniqueNick{
        void uniqueNicknameCallback(boolean isUnique);
    }
    interface hasGoogleUser{
        void hasGoogleUserCallback(boolean hasThisUser);
    }
}
