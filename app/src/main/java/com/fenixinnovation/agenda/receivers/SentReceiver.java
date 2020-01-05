package com.fenixinnovation.agenda.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(context, "SMS enviada", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(context, "SMS Generica falhada",
                        Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(context, "Sem servico",
                        Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Toast.makeText(context, "PDU nula", Toast.LENGTH_SHORT)
                        .show();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(context, "Sem conexao",
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
