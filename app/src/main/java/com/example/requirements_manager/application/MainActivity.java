package com.example.requirements_manager.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.requirements_manager.R;
import com.example.requirements_manager.dao.DaoFactory;
import com.example.requirements_manager.dao.UserDAO;
import com.example.requirements_manager.entities.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createProjectBtn;

        createProjectBtn = findViewById(R.id.createProjectBtn);

        UserDAO userDAO = DaoFactory.createUserDao();
        List<User> list = userDAO.findAll();
        list.forEach(System.out::println);

        createProjectBtn.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MainActivity.this, ProjectActivity.class);
                            startActivity(i);
                        }
                    }
            );

        }

}
