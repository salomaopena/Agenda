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
import com.fenixinnovation.agenda.myadapter.MailAdapter;
import com.fenixinnovation.agenda.objects.Contact;
import com.fenixinnovation.agenda.utils.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmailsFragment extends Fragment {

    private Handler mHandler;
    private MailAdapter mAdapter;
    private List<Contact> contacts;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mailView;

    public EmailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_emails, container, false);

        mailView = rootView.findViewById(R.id.mail_rv);

        mailView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mailView.setLayoutManager(mLayoutManager);
        mailView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(),
                        mLayoutManager.getOrientation());
        mailView.addItemDecoration(itemDecoration);

        contacts = new ArrayList<>();


        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

        mHandler = new Handler(Looper.getMainLooper());

        showMails();

        return rootView;
    }

    private void showMails(){
        mHandler.post(()->{
            contacts.clear();
            contacts = new Database(getActivity()).getContats();
            mAdapter = new MailAdapter(contacts);
            mailView.setAdapter(mAdapter);
        });
    }
}
