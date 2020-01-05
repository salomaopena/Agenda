package com.fenixinnovation.agenda.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsApp {

    public static String getDateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
