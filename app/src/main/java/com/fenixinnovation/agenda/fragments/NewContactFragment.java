package com.fenixinnovation.agenda.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fenixinnovation.agenda.R;
import com.fenixinnovation.agenda.objects.Contact;
import com.fenixinnovation.agenda.utils.Database;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import static com.fenixinnovation.agenda.utils.UtilsApp.getDateNow;


public class NewContactFragment extends Fragment {

    EditText nameEditText;
    EditText surnameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    Button saveButton;

    Database database;

    public NewContactFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_contact, container, false);

        nameEditText = rootView.findViewById(R.id.et_name);
        surnameEditText = rootView.findViewById(R.id.et_surname);
        phoneEditText = rootView.findViewById(R.id.et_phone);
        emailEditText = rootView.findViewById(R.id.et_email);
        saveButton = rootView.findViewById(R.id.btn_save);

        //Initialize database
        database = new Database(getActivity());


        saveButton.setOnClickListener(view -> {
            Contact contact = new Contact();

            contact.setName(nameEditText.getText().toString().trim());
            contact.setSurname(surnameEditText.getText().toString().trim());
            contact.setPhone(phoneEditText.getText().toString().trim());
            contact.setEmail(emailEditText.getText().toString().trim());
            contact.setCreated(getDateNow());

            if (database.isExists(contact)){
                Snackbar.make(view,getString(R.string.is_exists),Snackbar.LENGTH_SHORT).show();
                clearInputs();
            }else{
                database.addContact(contact);
                Snackbar.make(view,getString(R.string.success_message),Snackbar.LENGTH_SHORT).show();
                clearInputs();
            }
   });


        return rootView;
    }


    private void clearInputs(){
        nameEditText.getText().clear();
        surnameEditText.getText().clear();
        phoneEditText.getText().clear();
        emailEditText.getText().clear();
    }

}
