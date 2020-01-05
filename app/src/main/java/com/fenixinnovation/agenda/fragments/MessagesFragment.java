package com.fenixinnovation.agenda.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fenixinnovation.agenda.R;
import com.fenixinnovation.agenda.interfaces.ItemCLickListener;
import com.fenixinnovation.agenda.myadapter.AllConversationAdapter;
import com.fenixinnovation.agenda.objects.SMS;
import com.fenixinnovation.agenda.ui.NewSMSActivity;
import com.fenixinnovation.agenda.ui.SettingsActivity;
import com.fenixinnovation.agenda.ui.SmsDetailedView;
import com.fenixinnovation.agenda.utils.Constants;
import com.fenixinnovation.agenda.utils.SmsContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class MessagesFragment extends Fragment implements View.OnClickListener,
        ItemCLickListener, LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {


    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private AllConversationAdapter allConversationAdapter;
    private String TAG = MessagesFragment.class.getSimpleName();
    private String mCurFilter;
    private List<SMS> data;
    private LinearLayoutManager linearLayoutManager;
    private BroadcastReceiver mReceiver;
    private ProgressBar progressBar;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);


        recyclerView = rootView.findViewById(R.id.recyclerview);
        fab = rootView.findViewById(R.id.fab_new);
        progressBar = rootView.findViewById(R.id.progressBar);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        fab.setOnClickListener(this);

        if (checkDefaultSettings())
            checkPermissions();

        return rootView;
    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_SMS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_SMS},
                        Constants.MY_PERMISSIONS_REQUEST_READ_SMS);
            }

        }


        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS);
        if (permissionCheck == 0)
            getActivity().getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.READ_CONTACTS)) {
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        }

                    } else


                        getActivity().getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);


                } else {
                    Toast.makeText(getActivity(),
                            "Can't access messages.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case Constants.MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getActivity().getSupportLoaderManager().initLoader(Constants.ALL_SMS_LOADER, null, this);

                } else {
                    Toast.makeText(getActivity(),
                            "Can't access messages.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }


    private boolean checkDefaultSettings() {

        boolean isDefault = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            if (!Telephony.Sms.getDefaultSmsPackage(getActivity()).equals(getActivity().getPackageName())) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("This app is not set as your default messaging app. Do you want to set it as default?")
                        .setCancelable(false)
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                            checkPermissions();
                        })
                        .setPositiveButton("Yes", (dialog, id) -> {
                            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getActivity().getPackageName());
                            startActivity(intent);
                            checkPermissions();
                        });
                builder.show();

                isDefault = false;
            } else
                isDefault = true;
        }
        return isDefault;
    }


    private void setRecyclerView(List<SMS> data) {
        allConversationAdapter = new AllConversationAdapter(data);
        allConversationAdapter.setItemClickListener(this);
        recyclerView.setAdapter(allConversationAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fab_new:
                Intent intent = NewSMSActivity.getStartIntent(getContext());
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.home_menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.ic_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_settings) {
            startActivity(new Intent(getContext(), SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        mCurFilter = !TextUtils.isEmpty(query) ? query : null;
        getActivity().getSupportLoaderManager().restartLoader(Constants.ALL_SMS_LOADER, null, this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getActivity().getSupportLoaderManager().restartLoader(Constants.ALL_SMS_LOADER, null, this);
        return true;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = null;
        String[] selectionArgs = null;

        if (mCurFilter != null) {
            selection = SmsContract.SMS_SELECTION_SEARCH;
            selectionArgs = new String[]{"%" + mCurFilter + "%", "%" + mCurFilter + "%"};
        }

        return new CursorLoader(getActivity(),
                SmsContract.ALL_SMS_URI,
                null,
                selection,
                selectionArgs,
                SmsContract.SORT_DESC);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        progressBar.setVisibility(View.GONE);

        if (cursor != null && cursor.getCount() > 0) {

            //allConversationAdapter.swapCursor(cursor);
            getAllSmsToFile(cursor);

        } else {
            //no sms
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        data = null;
        if (allConversationAdapter != null)
            allConversationAdapter.notifyDataSetChanged();
        //allConversationAdapter.swapCursor(null);
    }

    @Override
    public void itemClicked(int color, String contact, long id, String read) {
        Intent intent = SmsDetailedView.getStartIntent(getContext());
        startActivity(intent);
        intent.putExtra(Constants.CONTACT_NAME, contact);
        intent.putExtra(Constants.COLOR, color);
        intent.putExtra(Constants.SMS_ID, id);
        intent.putExtra(Constants.READ, read);
        startActivity(intent);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.mReceiver);
        getActivity().getSupportLoaderManager().destroyLoader(Constants.ALL_SMS_LOADER);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                boolean new_sms = intent.getBooleanExtra("new_sms", false);

                if (new_sms)
                    getActivity().getSupportLoaderManager().restartLoader(Constants.ALL_SMS_LOADER, null, MessagesFragment.this);

            }
        };

        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    public void getAllSmsToFile(Cursor c) {

        List<SMS> lstSms = new ArrayList<>();
        SMS objSMS = null;
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                try {
                    objSMS = new SMS();
                    objSMS.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
                    String num = c.getString(c.getColumnIndexOrThrow("address"));
                    objSMS.setAddress(num);
                    objSMS.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    objSMS.setReadState(c.getString(c.getColumnIndex("read")));
                    objSMS.setTime(c.getLong(c.getColumnIndexOrThrow("date")));
                    if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        objSMS.setFolderName("inbox");
                    } else {
                        objSMS.setFolderName("sent");
                    }

                } catch (Exception e) {

                } finally {

                    lstSms.add(objSMS);
                    c.moveToNext();
                }
            }
        }
        c.close();

        data = lstSms;

        //Log.d(TAG,"Size before "+data.size());
        sortAndSetToRecycler(lstSms);

    }

    private void sortAndSetToRecycler(List<SMS> lstSms) {

        Set<SMS> s = new LinkedHashSet<>(lstSms);
        data = new ArrayList<>(s);
        setRecyclerView(data);

        convertToJson(lstSms);
    }

    private void convertToJson(List<SMS> lstSms) {

        Type listType = new TypeToken<List<SMS>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(lstSms, listType);

        SharedPreferences sp = getActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SMS_JSON, json);
        editor.apply();
        //List<String> target2 = gson.fromJson(json, listType);
        //Log.d(TAG, json);

    }

}
