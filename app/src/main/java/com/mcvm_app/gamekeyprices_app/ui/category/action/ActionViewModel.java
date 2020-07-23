package com.mcvm_app.gamekeyprices_app.ui.category.action;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Action fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
