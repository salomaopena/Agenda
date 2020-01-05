package com.fenixinnovation.agenda.fragments;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.fenixinnovation.agenda.R;
import com.fenixinnovation.agenda.myadapter.ContactAdapter;
import com.fenixinnovation.agenda.objects.Contact;
import com.fenixinnovation.agenda.utils.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private Handler mHandler;
    private ContactAdapter mAdapter;
    private List<Contact> contacts;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView listView;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        listView = rootView.findViewById(R.id.contact_rv);

        listView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(),
                        mLayoutManager.getOrientation());
        listView.addItemDecoration(itemDecoration);

        contacts = new ArrayList<>();


        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

        mHandler = new Handler(Looper.getMainLooper());

        showContacts();

        return rootView;
    }

    private void showContacts(){
        mHandler.post(()->{
            contacts.clear();
            contacts = new Database(getActivity()).getContats();
            mAdapter = new ContactAdapter(contacts);
            listView.setAdapter(mAdapter);
        });
    }


}
