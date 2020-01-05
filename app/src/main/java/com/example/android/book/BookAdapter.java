package com.example.android.book;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends ArrayAdapter<BookList> {


    public BookAdapter(Activity context, List<BookList> bookList)
    {
        super(context, 0, bookList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.booklist, parent, false);

        }

        BookList books = getItem(position);
        TextView name = (TextView) listItemView.findViewById(R.id.bookName);
        name.setText(books.getName());

        return listItemView;
    }
}
