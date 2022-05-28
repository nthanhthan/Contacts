package com.example.contacts;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements Filterable {
    private List<contact> contactList;
    private OnContactListener onContactListener;
    private List<contact> listContactSearch;

    public ContactsAdapter(List<contact> contactList,OnContactListener onContactListener) {
        this.contactList = contactList;
        this.onContactListener=onContactListener;
        listContactSearch=contactList;
    }
    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row_item, parent, false);

        return new ViewHolder(view,onContactListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
       holder.tvName.setText(listContactSearch.get(position).getName());
        //if(listContactSearch.get(position).getAvt().equals("None")==false)
          //holder.tvName.setText(listContactSearch.get(position).getAvt());
       //   holder.iv_avt.setImageURI(Uri.parse(listContactSearch.get(position).getAvt()));
       //Picasso.get().load(listContactSearch.get(position).getAvt()).into(holder.iv_avt);
    }

    @Override
    public int getItemCount() {
        return listContactSearch.size();
    }

    @Override
    public Filter getFilter() {
        return contactsFilter;
    }
    private Filter contactsFilter=new Filter() {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence charSequence) {

            if(charSequence.toString().isEmpty()){
               listContactSearch=contactList;
            }else{
                List<contact> filteredList=new ArrayList<>();
                String filterPattern=charSequence.toString().toLowerCase().trim();
                for(contact item: contactList){
                    if(item.getName().toLowerCase().contains(filterPattern)||item.getMobile().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                listContactSearch=filteredList;
            }
            FilterResults resultFilter=new FilterResults();
            resultFilter.values=listContactSearch;
            return resultFilter;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listContactSearch=(ArrayList<contact>)filterResults.values;
            notifyDataSetChanged();
        }
    };
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvName;
        public ImageView iv_avt;
        OnContactListener onContactListener;
        public ViewHolder(View view,OnContactListener onContactListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            tvName= view.findViewById(R.id.tv_name);
            iv_avt=view.findViewById(R.id.imv_avt);
            this.onContactListener=onContactListener;
            view.setOnClickListener(this);
           // textView = (TextView) view.findViewById(R.id.textView);
        }
        @Override
        public void onClick(View view) {
            onContactListener.onContactClick(getAdapterPosition());
        }

//        public TextView getTextView() {
//            return textView;
//        }
    }
    public interface OnContactListener{
        void onContactClick(int position);
    }
}
