package com.egormoroz.schooly;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RecentMethods {
    public static void isNickCorrect(String nickname, DatabaseReference reference, TextView errorTextnickname) {
        if(nickname.length() < 4){
            showErrorMessage(ErrorList.ERROR_NICK_IS_TO_SHORT, errorTextnickname);
            return;
        }
        isNickUniqueFun(nickname, reference, new FirebaseCallbacks() {
            @Override
            public void uniqueNicknameCallback(boolean isUnique) {
                if(!isUnique)
                    showErrorMessage(ErrorList.NICK_IS_USED, errorTextnickname);
                else
                    showErrorMessage(ErrorList.NOTHING, errorTextnickname);
            }
        });
    }
    public static void showErrorMessage(ErrorList tag, TextView errorText) {
        switch (tag) {
            case NOT_ENOUGH_SYMBOL_ERROR:
                errorText.setText("The minimum number of symbols is 8");
                break;
            case ONLY_ENGLISH_SYMBOLS_ERROR:
                errorText.setText("Оnly English symbols in the nickname");
                break;
            case INVALID_SYMBOLS_ERROR:
                errorText.setText("Invalid symbols");
                break;
            case NOT_ONLY_NUMBERS_ERROR:
                errorText.setText("The password must contain more than just numbers");
                break;
            case NOT_ONLY_LETTERS:
                errorText.setText("The password must contain more than just letters");
                break;
            case NOTHING:
                errorText.setText(" ");
                break;
            case NICK_IS_USED:
                errorText.setText("Nickname is already being used");
                break;
            case PHONE_ERROR:
                errorText.setText("Phone is not correct");
                break;
            case ERROR_EMPTY_PHONE:
                errorText.setText("You didn't enter your phone");
                break;
        }
    }
    public static void setCurrentFragment(Fragment fragment, Activity activity) {
        FragmentTransaction ft = ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }
    public static void isNickUniqueFun(String nickname, DatabaseReference ref, final FirebaseCallbacks callback){
        Query query = ref.orderByChild("nick").equalTo(nickname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.uniqueNicknameCallback(!snapshot.exists());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public static String makeEmail(String phone) {
        String email = "schooly";
        for (int i = 1; i < phone.length(); i++)
            email += phone.toCharArray()[i];
        email += "@gmail.com";
        return email;
    }
    public static String getPhone(String email) {
        String res = email;
        res = res.replace("schooly", "");
        res = res.replace("@gmail.com", "");
        return "+" + res;
    }
}