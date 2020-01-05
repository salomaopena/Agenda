package com.fenixinnovation.agenda.tasks;


import android.content.Context;
import android.util.Log;

import com.fenixinnovation.agenda.ui.SettingsActivity;
import com.fenixinnovation.agenda.utils.Helpers;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;

import java.io.IOException;
import java.io.OutputStream;

public class EditContentsAsyncTask extends ApiClientAsyncTask<DriveFile, Void, Boolean> {

    private Context context;
    private String TAG = EditContentsAsyncTask.class.getSimpleName();

    public EditContentsAsyncTask(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected Boolean doInBackgroundConnected(DriveFile... args) {
        DriveFile file = args[0];
        try {
            DriveApi.DriveContentsResult driveContentsResult = file.open(
                    getGoogleApiClient(), DriveFile.MODE_WRITE_ONLY, null).await();
            if (!driveContentsResult.getStatus().isSuccess()) {
                return false;
            }
            DriveContents driveContents = driveContentsResult.getDriveContents();
            OutputStream outputStream = driveContents.getOutputStream();
            outputStream.write(Helpers.getSMSJson(context).getBytes());
            com.google.android.gms.common.api.Status status =
                    driveContents.commit(getGoogleApiClient(), null).await();
            return status.getStatus().isSuccess();
        } catch (IOException e) {
            Log.e(TAG, "Excepcao", e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (!result) {
            ((SettingsActivity)context).showMessage("Erro ao criar backup.");
            return;
        }
        ((SettingsActivity)context).showMessage("Backup criado com sucesso");

    }
}


