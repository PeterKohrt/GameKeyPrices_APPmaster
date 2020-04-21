package com.example.gamekeyprices_app.ui.category.action;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is action fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
