package com.example.gamekeyprices_app.ui.category.rpg;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RPGViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RPGViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is RPG fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}