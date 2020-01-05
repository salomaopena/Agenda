package com.fenixinnovation.agenda.myadapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fenixinnovation.agenda.R;
import com.fenixinnovation.agenda.objects.Contact;

import java.util.List;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.ViewHolderMail> {

    private Context mContext;
    private List<Contact> contacts;

    public MailAdapter(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderMail onCreateViewHolder(@NonNull ViewGroup convertView, int viewType) {
        mContext = convertView.getContext();
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_mail, convertView, false);
        return new ViewHolderMail(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMail view, int position) {
        Contact contact = contacts.get(position);
        view.nameView.setText(contact.getName() + " " + contact.getSurname());
        view.emailView.setText(contact.getEmail());

        view.itemMail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",contact.getEmail(), null));
            intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name));
            mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.email_send)));
        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolderMail extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView emailView;
        private LinearLayout itemMail;


        public ViewHolderMail(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tv_name);
            emailView = itemView.findViewById(R.id.tv_email);
            itemMail = itemView.findViewById(R.id.item_mail);
        }

    }

}
