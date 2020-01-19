package com.petstore.viewmodel;

import android.app.Application;

import com.petstore.model.Pet;
import com.petstore.model.repository.PetRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PetListViewModel extends AndroidViewModel {
    /** android log **/
    private final String TAG = PetListViewModel.class.getName();
    /** live data for pet list **/
    private LiveData<List<Pet>> petListObservable;

    public PetListViewModel(PetRepository petRepository, Application application) {
        super(application);
        petRepository.getPetData();
        petListObservable = petRepository.getPetList();
    }

    /**
     * return pet list live data
     * @return
     */
    public LiveData<List<Pet>> getPetListObservable() {
        return  petListObservable;
    }
}
