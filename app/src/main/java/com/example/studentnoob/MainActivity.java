package com.example.studentnoob;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv1;
    private StudentSubAdapter adapter;
    private ArrayList<StudentSub> liststd = new ArrayList<>();
    private ArrayList<StudentSub> oldlist = new ArrayList<>();
    private String name;
    private String mssv;
    private int selectedItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv1 = findViewById(R.id.lv1);
        selectedItemPosition = getIntent().getIntExtra("position", -1);
        name = getIntent().getStringExtra("name");
        mssv = getIntent().getStringExtra("mssv");
        getData();

        if (selectedItemPosition != -1) {
            liststd.get(selectedItemPosition).setName(name);
            liststd.get(selectedItemPosition).setMssv(mssv);
            adapter.notifyDataSetChanged();
        }

        // Register for context menu
        registerForContextMenu(lv1);

        // SetOnItemLongClickListener using a lambda expression
        lv1.setOnItemLongClickListener((parent, view, position, id) -> {
            selectedItemPosition = position; // Store the selected item position
            return false; // Return false to allow context menu to show
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            gotoEditActivity();
            return true;
        } else if (id == R.id.delete) {
            gotoDeleteActivity();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void gotoEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("position", selectedItemPosition);
        startActivity(intent);
    }

    private void gotoDeleteActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có thực sự muốn xóa không?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            int position = selectedItemPosition;

            // Remove the selected student
            List<StudentSub> updatedStudents = new ArrayList<>();
            for (int i = 0; i < liststd.size(); i++) {
                if (i != position) {
                    updatedStudents.add(liststd.get(i));
                }
            }

            adapter = new StudentSubAdapter(MainActivity.this, R.layout.studentitem, updatedStudents);
            lv1.setAdapter(adapter);
            oldlist = liststd;
            liststd = new ArrayList<>(updatedStudents);

            Snackbar.make(
                            findViewById(android.R.id.content),
                            "Student removed",
                            Snackbar.LENGTH_LONG
                    )
                    .setAction("Undo", v -> {
                        adapter = new StudentSubAdapter(MainActivity.this, R.layout.studentitem, oldlist);
                        lv1.setAdapter(adapter);
                        liststd = new ArrayList<>(oldlist);
                    })
                    .show();
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuitem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_new) {
            connectToAddActivity();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void connectToAddActivity() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    private void getData() {
        liststd.add(new StudentSub("Nguyễn Văn An", "SV001"));
        liststd.add(new StudentSub("Trần Thị Bảo", "SV002"));
        liststd.add(new StudentSub("Lê Hoàng Cường", "SV003"));
        liststd.add(new StudentSub("Phạm Thị Dung", "SV004"));
        liststd.add(new StudentSub("Đỗ Minh Đức", "SV005"));
        liststd.add(new StudentSub("Vũ Thị Hoa", "SV006"));
        liststd.add(new StudentSub("Hoàng Văn Hải", "SV007"));
        liststd.add(new StudentSub("Bùi Thị Hạnh", "SV008"));
        liststd.add(new StudentSub("Đinh Văn Hùng", "SV009"));
        liststd.add(new StudentSub("Nguyễn Thị Linh", "SV010"));
        if (name != null && !name.isEmpty() && mssv != null && !mssv.isEmpty()) {
            liststd.add(new StudentSub(name, mssv));
            Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        }
        adapter = new StudentSubAdapter(this, R.layout.studentitem, liststd);
        lv1.setAdapter(adapter);
    }
}
