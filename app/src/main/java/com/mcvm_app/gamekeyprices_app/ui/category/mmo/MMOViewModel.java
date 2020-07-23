package com.mcvm_app.gamekeyprices_app.ui.category.mmo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MMOViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MMOViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MMORPG fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
