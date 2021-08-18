package com.egormoroz.schooly;

public interface FirebaseCallbacks {
    default void uniqueNicknameCallback(boolean isUnique){
        return;
    };
    default void hasGoogleUserCallback(boolean hasGoogleUser){
        return;
    }
}