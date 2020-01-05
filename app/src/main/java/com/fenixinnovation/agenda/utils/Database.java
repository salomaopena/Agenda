package com.fenixinnovation.agenda.utils;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.fenixinnovation.agenda.objects.Contact;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "agenda.db";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Add new contact from your contact's list
    public void addContact(Contact contact) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO contact(name,surname,phone,email,created) VALUES ('%s','%s','%s','%s','%s');",
                contact.getName(),contact.getSurname(),contact.getPhone(),contact.getEmail(),contact.getCreated());
        db.execSQL(query);
    }

    //To verify if exist before add new
    public boolean isExists(Contact contact) {

        boolean flag = false;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String query = String.format("SELECT * FROM contact WHERE phone ='%s' AND email='%s'", contact.getPhone(), contact.getEmail());
        cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            flag = true;
        } else {
            flag = false;
        }
        cursor.close();
        return flag;
    }


    //For list all contacts
    public List<Contact> getContats() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"id", "name", "surname", "phone", "email", "created"};
        String sqlTable = "contact";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, "name");

        final List<Contact> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Contact(
                        c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("name")),
                        c.getString(c.getColumnIndex("surname")),
                        c.getString(c.getColumnIndex("phone")),
                        c.getString(c.getColumnIndex("email")),
                        c.getString(c.getColumnIndex("created"))
                ));
            } while (c.moveToNext());
        }
        return result;

    }


    //For removing an especific contact
    public void removeContact(Contact contact) {
        SQLiteDatabase database = getReadableDatabase();
        String consulta = String.format("DELETE FROM contact WHERE id ='%s';",contact.getKey());
        database.execSQL(consulta);
    }

    //To update an especific contact
    public void updateContact(Contact contact) {
        SQLiteDatabase database = getReadableDatabase();
        String consulta = String.format("UPDATE contact SET name='%s', surname='%s', phone='%s', email='%s' WHERE id = '%s';",
                contact.getName(), contact.getSurname(), contact.getPhone(),contact.getEmail(),contact.getKey());
        database.execSQL(consulta);
    }



}
