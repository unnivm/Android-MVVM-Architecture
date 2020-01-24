package com.petstore.viewmodel;

import android.app.Application;

import com.petstore.model.Pet;
import com.petstore.model.repository.PetRepository;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PetImageViewModel extends AndroidViewModel {

    /** observable for pet image **/
    protected  LiveData<List<Pet>> petImageObservable;

    /** list of pets **/
    private List<Pet>images;

    /** Pet repository **/
    private PetRepository petRepository;

    public PetImageViewModel(PetRepository petRepository, @NonNull Application application) {
        super(application);
        this.petRepository = petRepository;
        petRepository.downloadImages();
        petImageObservable = petRepository.getPetImageList();
    }

    /**
     *
     * @return
     */
    public LiveData<List<Pet>> getPetImageObservable() {
        return petImageObservable;
    }
}
