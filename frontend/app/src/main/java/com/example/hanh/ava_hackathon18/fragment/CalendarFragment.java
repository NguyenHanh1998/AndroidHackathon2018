package com.example.hanh.ava_hackathon18.fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hanh.ava_hackathon18.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarFragment extends BottomSheetDialogFragment {

    public static CalendarFragment newInstance() {
            CalendarFragment fragment = new CalendarFragment();
            return  fragment;
    }
    public interface OnViewAddPlaceListener {
        void open();
    }

    private OnViewAddPlaceListener onViewAddPlaceListener;

    public void setOnViewAddPlaceListener(OnViewAddPlaceListener onViewAddPlaceListener) {
        this.onViewAddPlaceListener = onViewAddPlaceListener;
    }

    @BindView(R.id.layoutAddPlace)
    View layoutAddPlace;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_set_calendar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.layoutAddPlace)
    public void showReturnPolicy() {
        if (onViewAddPlaceListener != null) {
            onViewAddPlaceListener.open();
        }
    }
}
