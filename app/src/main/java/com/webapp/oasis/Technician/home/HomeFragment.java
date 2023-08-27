package com.webapp.oasis.Technician.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.webapp.oasis.databinding.FragmentHomeBinding;

import java.util.Objects;

/* renamed from: com.webapp.oasis.Technician.ui.home.HomeFragment */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding inflate = FragmentHomeBinding.inflate(inflater, container, false);
        this.binding = inflate;
        View root = inflate.getRoot();
        TextView textView = this.binding.textHome;
        LiveData<String> text = ((HomeViewModel) new ViewModelProvider(this).get(HomeViewModel.class)).getText();
        LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
        Objects.requireNonNull(textView);
//        text.observe(viewLifecycleOwner, new Observer(textView) {
//            private final /* synthetic */ TextView f$0;
//
//            {
//                this.f$0 = r1;
//            }
//
//            public final void onChanged(Object obj) {
//                this.f$0.setText((String) obj);
//            }
//        });
        return root;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
