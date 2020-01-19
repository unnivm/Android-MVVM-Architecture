package com.petstore.view.adaptor;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.petstore.R;
import com.petstore.model.Pet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.OnClickListener;

public class PetViewHolder extends RecyclerView.ViewHolder {

    /**
     * image view
     **/
    private ImageView imageView;

    /**
     * text view
     **/
    private TextView textView;

    /**
     * android app context
     **/
    private Context context;

    /**
     * @param itemView
     */
    public PetViewHolder(@NonNull View itemView, final Context context) {
        super(itemView);

        this.context = context;
        imageView = itemView.findViewById(R.id.grid_image_1);
        textView = itemView.findViewById(R.id.textImage_1);
    }

    /**
     * bind image
     *
     * @param pet
     */
    public void bind(final Pet pet) {
        imageView.setImageBitmap(pet.getImage());
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView webView = new WebView(context);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.loadUrl(pet.getImageUrl());
            }
        });
        textView.setText(pet.getTitle());
    }
}
