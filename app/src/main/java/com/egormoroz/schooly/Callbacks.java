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
        void LoadData(ArrayList<UserInformation> data);
    }
    public interface PassLoadUserDataInterface{
        void PassData(ArrayList<UserInformation> data);
    }
    public interface GetUserNickByUid{
        void PassUserNick(String nick);
    }
    public interface GetMinerByMinerPrice{
        void PassMiner(String miner);
    }

    public interface GetMinerByUid{
        void SetMinerInMyMiners(String miner);
    }
    public interface HasUid{
        void HasUidCallback(boolean HasUid);
    }
}
