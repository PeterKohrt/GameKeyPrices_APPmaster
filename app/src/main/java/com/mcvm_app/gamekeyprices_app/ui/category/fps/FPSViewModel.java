package com.mcvm_app.gamekeyprices_app.ui.category.fps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FPSViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FPSViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is FPS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
