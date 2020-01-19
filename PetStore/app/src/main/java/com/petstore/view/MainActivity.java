package com.petstore.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.petstore.R;
import com.petstore.model.Config;
import com.petstore.model.Pet;
import com.petstore.model.repository.PetRepository;
import com.petstore.view.adaptor.PetAdaptor;
import com.petstore.view.util.BitmapCache;
import com.petstore.view.util.DownloadImageTask;
import com.petstore.view.util.Utils;
import com.petstore.viewmodel.ConfigurationViewModel;
import com.petstore.viewmodel.PetListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    /** chat button **/
    private Button chatButton;
    /** call button **/
    private Button callButton;
    /** working hours text view **/
    private TextView mWorkingHoursView;
    /** repository for endpoint **/
    private PetRepository petRepository;
    /** pet adaptor **/
    private PetAdaptor petAdaptor;

    /** total image **/
    private int total = 0;
    /** current downloading image **/
    private int current = 0;
    /** pet list **/
    private final List<Pet> images = new ArrayList<>();
    /** live data object **/
    private final MutableLiveData<List<Pet>> imageLiveData = new MutableLiveData<>();
    /** working hours **/
    private String workingHours;

    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callButton = findViewById(R.id.button3);
        callButton.setTextColor(Color.WHITE);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.validateOfficeHours(workingHours))
                    Toast.makeText(MainActivity.this.getApplicationContext(), R.string.in_office, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this.getApplicationContext(), R.string.out_office, Toast.LENGTH_LONG).show();
            }
        });

        chatButton = findViewById(R.id.button2);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.validateOfficeHours(workingHours))
                    Toast.makeText(MainActivity.this.getApplicationContext(), R.string.in_office, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this.getApplicationContext(), R.string.out_office, Toast.LENGTH_LONG).show();
            }
        });

        chatButton.setTextColor(Color.WHITE);
        mWorkingHoursView = findViewById(R.id.textView);

        petAdaptor = new PetAdaptor(MainActivity.this.getApplicationContext());

        RecyclerView petList =  findViewById(R.id.messageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        petList.setLayoutManager(linearLayoutManager);
        petList.setAdapter(petAdaptor);

        petRepository = new PetRepository(getApplicationContext(), 0);
        ConfigurationViewModel configurationViewModel = new ConfigurationViewModel(petRepository, MainActivity.this.getApplication());
        observeConfigViewModel(configurationViewModel);
    }

    /**
     * an observer for configuration view model.This method will get
     * the configuration data from the endpoint and will display it
     *
     * @param viewModel
     */
    private void observeConfigViewModel(final ConfigurationViewModel viewModel) {
        viewModel.getConfigObservable().observe(this, new Observer<Config>() {
            @Override
            public void onChanged(Config config) {
                if (!config.isCallEnabled()) callButton.setVisibility(View.INVISIBLE);
                if (!config.isCallEnabled()) chatButton.setVisibility(View.INVISIBLE);
                workingHours = config.getWorkingHours();
                mWorkingHoursView.setText("Office Hours: " + workingHours);
                // process pet data
                registerPetObserver();
            }
        });
    }

    /**
     * registers pet observer
     */
    private void registerPetObserver() {
        /** android model for pets **/
        PetListViewModel petListViewModel = new PetListViewModel(petRepository, MainActivity.this.getApplication());
        observePetViewModel(petListViewModel);
    }

    /**
     * @param viewModel
     */
    private void observePetViewModel(PetListViewModel viewModel) {
        viewModel.getPetListObservable().observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> ps) {
                total = ps.size();
                // set it to pet adaptor
                for (Pet p : ps) {
                    downloadImage(p);
                }
                registerImageObserver();
            }
        });
    }

    /**
     *
     */
    private void registerImageObserver() {
        imageLiveData.observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(List<Pet> pets) {
                petAdaptor.setPetList(pets);
            }
        });
    }

    /**
     * download image from endpoint and sets image to pet
     * object
     *
     * @param p
     */
    private void downloadImage(final Pet p) {
        current += 1;
        try {
            Bitmap bitmap = BitmapCache.getInstance().getBitmap(p.getImageUrl());
            if (bitmap != null) {
                p.setImage(bitmap);
            }

            DownloadImageTask task = new DownloadImageTask();
            task.setHttpListener(new DownloadImageTask.HttpCallback() {
                @Override
                public void onPostExecute(final Bitmap bitmap) {
                    p.setImage(Bitmap.createScaledBitmap(bitmap, 140, 140, false));
                    images.add(p);

                    if (current >= total) {
                        imageLiveData.setValue(images);
                    }
                }

                @Override
                public void onPreExecute() {
                }
            });

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, p.getImageUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
