package com.example.rooms;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout parent;
    private TextView button;
    private float dX, dY;
    private ArrayList<Room> rooms = new ArrayList<>();
    private Room room;
    private Room activeRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent = findViewById(R.id.parent);
        //parent.setOnTouchListener(this);



        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton delete = findViewById(R.id.delete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                button = new TextView(MainActivity.this);
//                button.setText("OK");
//                button.setBackgroundColor(Color.LTGRAY);
//                button.setWidth(400);
//                button.setHeight(400);
//                button.setOnTouchListener(buttonListener);
//                tvs.add(button);
                room = new Room(MainActivity.this);
                room.setName("Jiga");
                //room.setOnTouchListener(buttonListener);
                parent.addView(room);
                rooms.add(room);
                activeRoom = room;
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Room room : rooms) {
                    if (room.getCurrent()) {
                        activeRoom = room;
                    }
                }

                rooms.remove(activeRoom);
                parent.removeView(activeRoom);
            }

        });
    }

    public ArrayList getAllRooms(){
        return rooms;
    }

    boolean isCenter = false;

    private View.OnTouchListener buttonListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(isPointInsideView(event.getRawX(), event.getRawY(), view)){
                        isCenter = true;
                    } else {
                        isCenter = false;
                    }

                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(isCenter){
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                    } else {
//                        view.set
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isCenter = false;
//                    view.animate().cancel();
//                    view.animate().scaleX(1f).setDuration(1000).start();
//                    view.animate().scaleY(1f).setDuration(1000).start();
                    break;
                default:
                    return false;
            }
            return true;
        }
    };

    private boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        // point is inside view bounds
        return ((x > (viewX + 50) && x < ((viewX + view.getWidth()) - 50)) &&
                (y > (viewY + 50) && y < ((viewY + view.getHeight()) - 50)));
    }

    private void print(String printThis){
        Log.d("VZ", printThis);
    }

}
