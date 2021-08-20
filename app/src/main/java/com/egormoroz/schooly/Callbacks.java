package com.egormoroz.schooly;

public class Callbacks {
    public interface UniqueNick{
        void uniqueNicknameCallback(boolean isUnique);
    }
    public interface hasGoogleUser{
        void hasGoogleUserCallback(boolean hasThisUser);
    }
}
