package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.example.contacts.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.OnContactListener {
    private List<contact> contactList;
    private ContactsAdapter contactsAdapter;
    private ActivityMainBinding binding;
    private ContactDAO contactDAO;
    private AppDatabase appDatabase;

    // private MyViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        //  model=new ViewModelProvider(this).get(MyViewModel.class);
        contactsAdapter = new ContactsAdapter(contactList, this);
        binding.rvContacts.setAdapter(contactsAdapter);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase = AppDatabase.getInstance(getApplicationContext());
                contactDAO = appDatabase.contactDAO();
                List<contact> newList = new ArrayList<>();
                newList = contactDAO.getAll();
                if (newList != null) {
                    for (contact i : newList) {
                        contact newContact = (new contact(i.getName(), i.getEmail(), i.getMobile(),i.getAvt()));
                        newContact.setId(i.getId());
                        contactList.add(newContact);
                    }
                }
            }
        });
//        contactList.add(new contact("Nguyen thanh than","thanhthan2k1@gmail.com","0901948483"));
//        contactList.add(new contact("Nguyen thanh tin","thanhtin@gmail.com","09"));
//        contactList.add(new contact("Nguyen thanh trung","thanhthan@gmail.com","0901948483"));
        //  contactsAdapter.notifyDataSetChanged();

//        model.getList().observe(this, new Observer<ArrayList<contact>>() {
//            @Override
//            public void onChanged(ArrayList<contact> contacts) {
//                if(contacts!=null){
//                    contactList.clear();
//                    for(contact i: contacts){
//                        contactList.add(i);
//                    }
//                    contactsAdapter.notifyDataSetChanged();
//                }
//            }
//        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Create_Contact.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contactsAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String email = data.getStringExtra("email");
            String phone = data.getStringExtra("phone");
            String avt=data.getStringExtra("avt");
            int id=data.getIntExtra("id",-1);
            contact contacts = new contact(name, email, phone,avt);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase = AppDatabase.getInstance(getApplicationContext());
                    contactDAO = appDatabase.contactDAO();
                    if(id==-1)
                    contactDAO.insert(contacts);
                    else
                        contactDAO.update(id,name,email,phone,avt);
                    List<contact> newList = new ArrayList<>();
                    newList = contactDAO.getAll();
                    contactList.clear();
                    if (newList != null) {
                        for (contact i : newList) {
                            contact newContact = (new contact(i.getName(), i.getEmail(), i.getMobile(),i.getAvt()));
                            newContact.setId(i.getId());
                            contactList.add(newContact);
                        }
                    }
                }
            });

//            model.ChangeList(contacts).observe(this, new Observer<ArrayList<contact>>() {
//                @Override
//                public void onChanged(ArrayList<contact> contacts) {
//                    if(contacts!=null) {
//                        contactList.clear();
//                        for (contact i : contacts) {
//                            contactList.add(i);
//                        }
//                        contactsAdapter.notifyDataSetChanged();
//                    }
//                }
//            });
            contactsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContactClick(int position) {
        contact i = contactList.get(position);
        Intent intent = new Intent(MainActivity.this, Create_Contact.class);
        intent.putExtra("id",i.getId());
        intent.putExtra("name", i.getName().toString());
        intent.putExtra("email", i.getEmail().toString());
        intent.putExtra("phone", i.getMobile().toString());
        intent.putExtra("avt",i.getAvt().toString());
        startActivityForResult(intent, 1);
    }
}