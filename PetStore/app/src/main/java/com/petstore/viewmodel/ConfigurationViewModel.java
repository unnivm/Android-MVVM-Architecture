package com.petstore.viewmodel;

import android.app.Application;

import com.petstore.model.Config;
import com.petstore.model.repository.PetRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ConfigurationViewModel extends AndroidViewModel {

    /** live data object for config **/
    private final LiveData<Config> configObservable;

    public ConfigurationViewModel(PetRepository petRepository, Application application) {
        super(application);

        petRepository.getConfigurationData();
        configObservable = petRepository.getConfig();
    }

    /**
     * returns live data for configuration
     * @return
     */
    public LiveData<Config> getConfigObservable() {
        return configObservable;
    }

}
