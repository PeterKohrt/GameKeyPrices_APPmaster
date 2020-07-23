package com.mcvm_app.gamekeyprices_app.ui.category.racing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RacingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RacingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Racing fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}