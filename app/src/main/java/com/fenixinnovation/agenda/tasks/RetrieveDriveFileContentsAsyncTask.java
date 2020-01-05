package com.fenixinnovation.agenda.tasks;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fenixinnovation.agenda.ui.SettingsActivity;
import com.fenixinnovation.agenda.utils.Constants;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RetrieveDriveFileContentsAsyncTask
        extends ApiClientAsyncTask<DriveId, Boolean, String> {

    private String TAG = RetrieveDriveFileContentsAsyncTask.class.getSimpleName();
    private Context context;

    public RetrieveDriveFileContentsAsyncTask(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected String doInBackgroundConnected(DriveId... params) {
        String contents = null;
        DriveFile file = params[0].asDriveFile();
        DriveApi.DriveContentsResult driveContentsResult =
                file.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, null).await();
        if (!driveContentsResult.getStatus().isSuccess()) {
            return null;
        }
        DriveContents driveContents = driveContentsResult.getDriveContents();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(driveContents.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            contents = builder.toString();
        } catch (IOException e) {
            Log.e(TAG, "IOException while reading from the stream", e);
        }

        driveContents.discard(getGoogleApiClient());
        return contents;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result == null) {
            ((SettingsActivity)context).showMessage("Erro ao criar ficheiro");
            return;
        }
        ((SettingsActivity)context).showMessage("Messagens restauradas. ");
        restoreJSON(result);
    }

    private void restoreJSON(String json) {

        SharedPreferences sp = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SMS_JSON, json);
        editor.apply();

    }
}

