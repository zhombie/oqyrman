package com.example.zhomart.oqyrman.Fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhomart.oqyrman.Models.Book;
import com.example.zhomart.oqyrman.R;
import com.example.zhomart.oqyrman.DetailsActivity;

import java.util.List;

class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.MyViewHolder>{
    private final FragmentActivity fragmentActivity;
    private final String url;
    private final String language;
    private List<Book> data;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView thumbnail;
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.authorTextView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    DiscoverAdapter(FragmentActivity activity, List<Book> movieList, String url, String language) {
        this.data = movieList;
        this.fragmentActivity = activity;
        this.url = url;
        this.language = language;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Book book = data.get(position);
        if (language.equals("es")){
            holder.name.setText(book.getTitle_latin());
            holder.price.setText(book.getAuthor_latin());
            System.out.println("img:" + url + book.getImage());
            Glide.with(fragmentActivity)
                    .load(url + book.getImage_latin())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(holder.thumbnail);
        } else{
            holder.name.setText(book.getTitle());
            holder.price.setText(book.getAuthor());
            System.out.println("img:" + url + book.getImage());
            Glide.with(fragmentActivity)
                    .load(url + book.getImage())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(holder.thumbnail);
        }

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragmentActivity, DetailsActivity.class);
                intent.putExtra("position", book.getId());
                intent.putExtra("language", language);
                fragmentActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
