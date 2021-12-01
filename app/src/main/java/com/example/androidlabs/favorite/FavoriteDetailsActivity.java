package com.example.androidlabs.favorite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.androidlabs.R;
import com.example.androidlabs.article.ArticleModel;
import com.example.androidlabs.data.local.db.GuardianDatabase;
import com.example.androidlabs.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FavoriteDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DATA_ARTICLE = "EXTRA_DATA_FAVORITE_ARTICLE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.label_favorite_detail);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ArticleModel articleModel = getIntent().getParcelableExtra(EXTRA_DATA_ARTICLE);

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvUrl = findViewById(R.id.tv_url);
        TextView tvSelectionName = findViewById(R.id.tv_selection_name);

        tvTitle.setText(articleModel.getTitle());
        tvUrl.setText(articleModel.getUrl());
        tvSelectionName.setText(articleModel.getSectionName());

        tvUrl.setOnClickListener(v -> Utils.openBrowser(v, articleModel.getUrl()));

        AppCompatButton btnOpenInBrowser = findViewById(R.id.btn_open_in_browser);

        btnOpenInBrowser.setOnClickListener(v -> Utils.openBrowser(v, articleModel.getUrl()));

        FloatingActionButton fabSave = findViewById(R.id.fab_save);

        // in this block there are some changes and string extraction
        fabSave.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.dialog_title_delete_favorite);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                if (GuardianDatabase.getInstance(getBaseContext()).delete(articleModel.getId())) {
                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.success_delete_favorite), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra(FavoriteActivity.KEY_IS_DELETE, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(v.getContext(), v.getContext().getString(R.string.fail_delete_favorite), Toast.LENGTH_LONG).show();
                }
                dialog.cancel();
            });
            builder.setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    /**
     * add create menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * add action menu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_help) {
            Utils.showDialogHelp(this, String.format("%s\n\n%s", getString(R.string.help_delete_message), getString(R.string.open_in_browser)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
