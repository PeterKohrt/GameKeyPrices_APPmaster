package com.example.gamekeyprices_app.ui.category.adventure;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdventureViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdventureViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Adventure fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}