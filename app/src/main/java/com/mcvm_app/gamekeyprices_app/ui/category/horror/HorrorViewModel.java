package com.mcvm_app.gamekeyprices_app.ui.category.horror;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HorrorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HorrorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Horror fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}