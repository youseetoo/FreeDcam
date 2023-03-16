package uc2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.troop.freedcam.R;
import com.troop.freedcam.databinding.Uc2FragmentControlBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Uc2UiFragment extends Fragment {

    Uc2FragmentControlBinding binding;
    Uc2ViewModel uc2ViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        uc2ViewModel = new ViewModelProvider(getActivity()).get(Uc2ViewModel.class);
        binding =  DataBindingUtil.inflate(inflater, R.layout.uc2_fragment_control, container, false);
        binding.setLed(uc2ViewModel.getLedModel());
        binding.setMotor(uc2ViewModel.getMotorModel());
        binding.setConnectionModel(uc2ViewModel.getConnectionModel());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}
