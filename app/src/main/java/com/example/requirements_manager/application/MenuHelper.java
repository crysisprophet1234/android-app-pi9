package com.example.requirements_manager.application;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.requirements_manager.R;

public class MenuHelper {

    private final AppCompatActivity activity;

    public MenuHelper(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.homepage:
                navigateToActivity(MainActivity.class);
                return true;

            case R.id.project_activity:
                navigateToActivity(ProjectActivity.class);
                return true;

            case R.id.requirement_register:
                navigateToActivity(RequirementsActivity.class);
                return true;

            case R.id.log_off:
                MainActivity.loggedIn = false;
                navigateToActivity(MainActivity.class);
                return true;

                /*

            case R.id.deleteAll:
                RequirementService reqService = new RequirementService(activity);
                ProjectService projectService = new ProjectService(activity);
                reqService.deleteAll();
                projectService.deleteAll();
                activity.recreate();
                return true;

                 */

            default:
                return activity.onOptionsItemSelected(item);
        }
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(activity, activityClass);
        if (activity.getClass().getName().equals(activityClass.getName())) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        activity.startActivity(intent);
    }
}

