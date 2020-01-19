package com.petstore.view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petstore.R;
import com.petstore.model.Pet;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.*;

public class PetAdaptor extends RecyclerView.Adapter<PetViewHolder> {

    /**
     * data structure for holding list of pets
     **/
    private List<Pet> petList;

    private final Context context;

    public PetAdaptor(final Context context) {
        this.context = context;
    }

    /**
     * sets list of pets received from the endpoint
     *
     * @param petList
     */
    public void setPetList(List<Pet> petList) {
        this.petList = petList;
        notifyDataSetChanged();
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pet_item_list_1, parent, false);
        return new PetViewHolder(v, context);
    }

    /**
     * inflate the layout and displays the
     * list of pets
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {
        Pet pet = petList.get(position);
        if (pet != null)
            holder.bind(pet);
    }

    /**
     * returns list of pets
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return petList == null ? 0 : petList.size();
    }

}
