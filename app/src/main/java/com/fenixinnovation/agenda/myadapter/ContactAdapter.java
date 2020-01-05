package com.fenixinnovation.agenda.myadapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fenixinnovation.agenda.R;
import com.fenixinnovation.agenda.objects.Contact;
import com.fenixinnovation.agenda.ui.NewSMSActivity;
import com.fenixinnovation.agenda.utils.Database;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolderContact> {

    private Context mContext;
    private List<Contact> contacts;
    private Dialog dialog;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderContact onCreateViewHolder(@NonNull ViewGroup convertView, int viewType) {
        mContext = convertView.getContext();
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_contact, convertView, false);
        return new ContactAdapter.ViewHolderContact(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContact view, int position) {
        Contact contact = contacts.get(position);
        view.nameView.setText(contact.getName() + " " + contact.getSurname());
        view.phoneView.setText(contact.getPhone());

        view.itemContact.setOnClickListener(v -> {
            callDialog(contact);
        });

        view.more.setOnClickListener(more -> {
            showMenu(more, contact);
        });

    }

    //menu Options
    private void showMenu(View more, Contact contact) {
        PopupMenu menu = new PopupMenu(mContext, more);
        menu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.update:
                    updateDialog(contact);
                    return true;
                case R.id.delete:
                    new Database(mContext).removeContact(contact);
                    Snackbar.make(more, "Excluido com sucesso!",
                            Snackbar.LENGTH_SHORT).show();
                    new Database(mContext).getContats();
                    return true;

            }
            return false;
        });
        MenuInflater inflater = menu.getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu.getMenu());
        menu.show();
    }

    private void updateDialog(Contact contact) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText idText = dialog.findViewById(R.id.et_id);
        EditText nameText = dialog.findViewById(R.id.et_name);
        EditText surnameText = dialog.findViewById(R.id.et_surname);
        EditText phoneText = dialog.findViewById(R.id.et_phone);
        EditText emailText = dialog.findViewById(R.id.et_email);

        Button saveButton = dialog.findViewById(R.id.btn_save);


        //set Contents
        idText.setText(String.valueOf(contact.getKey()));
        nameText.setText(contact.getName());
        surnameText.setText(contact.getSurname());
        phoneText.setText(contact.getPhone());
        emailText.setText(contact.getEmail());


        //Save in database
        saveButton.setOnClickListener(save -> {
            Database database = new Database(mContext);
            contact.setKey(Integer.parseInt(idText.getText().toString().trim()));
            contact.setName(nameText.getText().toString().trim());
            contact.setSurname(surnameText.getText().toString().trim());
            contact.setPhone(phoneText.getText().toString().trim());
            contact.setEmail(emailText.getText().toString().trim());


            if (database.isExists(contact)){
                Snackbar.make(save,mContext.getString(R.string.is_exists),
                        Snackbar.LENGTH_SHORT).show();
            }else{
                database.updateContact(contact);
                database.getContats();
                Snackbar.make(save, mContext.getString(R.string.success_message),
                        Snackbar.LENGTH_SHORT).show();

                idText.getText().clear();
                nameText.getText().clear();
                surnameText.getText().clear();
                phoneText.getText().clear();
                emailText.getText().clear();
                dialog.dismiss();
            }

        });

        dialog.show();
    }


    //Call dialog
    private void callDialog(Contact contact) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_contact);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView nameTextView = dialog.findViewById(R.id.tv_name);
        TextView phoneTextView = dialog.findViewById(R.id.tv_phone);
        TextView emailTextView = dialog.findViewById(R.id.tv_email);

        Button callButton = dialog.findViewById(R.id.btn_mobile_call);
        Button messageButton = dialog.findViewById(R.id.btn_send_message);

        //set Contents
        nameTextView.setText(contact.getName() + " " + contact.getSurname());
        phoneTextView.setText(contact.getPhone());
        emailTextView.setText(contact.getEmail());

        callButton.setOnClickListener(call -> {
            phoneCallIntent(contact.getPhone());
            dialog.dismiss();
        });
        messageButton.setOnClickListener(call -> {
            Intent i = NewSMSActivity.getStartIntent(mContext);
            mContext.startActivity(i);
            dialog.dismiss();
        });
        dialog.show();
    }


    private void phoneCallIntent(String number) {
        String phoneNumber = number.replace("(\\s|\\)|\\(|-)", "");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        mContext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolderContact extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView phoneView;
        private ImageView more;
        private LinearLayout itemContact;


        public ViewHolderContact(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tv_name);
            phoneView = itemView.findViewById(R.id.tv_phone);
            more = itemView.findViewById(R.id.iv_more_events);
            itemContact = itemView.findViewById(R.id.item_contact);
        }

    }

}
