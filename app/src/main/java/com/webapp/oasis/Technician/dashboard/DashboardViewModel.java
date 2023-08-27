package com.webapp.oasis.Technician.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/* renamed from: com.webapp.oasis.Technician.ui.dashboard.DashboardViewModel */
public class DashboardViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public DashboardViewModel() {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        this.mText = mutableLiveData;
        mutableLiveData.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return this.mText;
    }
}
