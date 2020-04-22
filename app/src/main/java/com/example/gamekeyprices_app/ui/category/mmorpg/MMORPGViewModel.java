package com.example.gamekeyprices_app.ui.category.mmorpg;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MMORPGViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MMORPGViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MMORPG fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
