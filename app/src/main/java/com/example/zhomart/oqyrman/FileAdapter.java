package com.example.zhomart.oqyrman;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.folioreader.FolioReader;

import java.util.List;

import nl.siegmann.epublib.epub.EpubReader;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.MyViewHolder> {
    private final Context context;
    private final List<FileBean> data;
//    int res;
//    ArrayList<FileBean> list;
//    private Book book;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtView, txtView2;
        ImageView imageViewTest;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtView = (TextView) itemView.findViewById(R.id.title);
            txtView2 = (TextView) itemView.findViewById(R.id.authorTextView);
            imageViewTest = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    public FileAdapter(Context cxt, List<FileBean> objects){
        this.context = cxt;
        this.data = objects;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FileBean book = data.get(position);
        holder.txtView.setText(book.getFileName());
        holder.txtView2.setText(book.getFilePath());
        holder.imageViewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TEST", book.getFilePath());
                Intent intent = new Intent(context, EpubActivity.class);
                intent.putExtra("path", book.getFileName());
                context.startActivity(intent);
//                FolioReader folioReader = FolioReader.getInstance(context);
//                folioReader.openBook(book.getFilePath());
            }
        });
        holder.imageViewTest.setImageResource(R.drawable.cover);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

//    public FileAdapter(Context context, int resource, ArrayList<FileBean> objects) {
//        super(context, resource, objects);
//        cxt = context;
//        res = resource;
//        list = objects;
//    }
}