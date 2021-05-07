package dev.hikari.paintview.sample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dev.hikari.paintview.sample.databinding.ActivityMainBinding;
import kotlin.Unit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnConfirm.setOnClickListener(v -> binding.paintView.saveBitmap(bitmap -> {
            //You can save bitmap here

            return Unit.INSTANCE;
        }));
        binding.btnUndo.setOnClickListener(v -> binding.paintView.undo());
    }
}
