package com.example.rooms;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Room extends RelativeLayout {

    private RelativeLayout rl;
    private ImageView topLeftPin;
    private ImageView topRightPin;
    private ImageView bottomLeftPin;
    private ImageView bottomRightPin;
    private TextView mainView;
    private float dX, dY;
    private Context context;
    private int currentWidth, currentHeight;
    private boolean isCurrent = false;

    public Room(Context context) {
        super(context);
        init(context);
    }

    public Room(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Room(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Room(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_room_rel, this, false);
        rl = rootView.findViewById(R.id.holder);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(300, 200);
        currentWidth = 300;
        currentHeight = 200;
        rl.setLayoutParams(lp);
        addView(rootView);

        topLeftPin = rootView.findViewById(R.id.topLeftPin);
        topRightPin = rootView.findViewById(R.id.topRightPin);
        bottomLeftPin = rootView.findViewById(R.id.bottomLeftPin);
        bottomRightPin = rootView.findViewById(R.id.bottomRightPin);
        mainView = rootView.findViewById(R.id.mainView);

//        ConstraintSet set = new ConstraintSet();
//        set.clone(this);
//        set.connect(rootView.getId(), ConstraintSet.TOP, this.getId(), ConstraintSet.TOP);
//        set.connect(rootView.getId(), ConstraintSet.START, this.getId(), ConstraintSet.START);
//        set.connect(rootView.getId(), ConstraintSet.END, this.getId(), ConstraintSet.END);
//        set.connect(rootView.getId(), ConstraintSet.BOTTOM, this.getId(), ConstraintSet.BOTTOM);

    }

    public void setName(String name){
        mainView.setText(name);
    }

    public synchronized void setCurrent(boolean isCurrent){
        this.isCurrent = isCurrent;
    }

    public synchronized boolean getCurrent(){
        return isCurrent;
    }

    boolean isCenter = false;
    boolean topLeftPinTouched;
    boolean topRightPinTouched;
    boolean bottomLeftPinTouched;
    boolean bottomRightPinTouched;
    private final int PIN_SIZE_DP = 16;
    private float startMoveX, startMoveY, endMoveX, endMoveY, deltaMoveX, deltaMoveY;
    private int dragX, dragY;
    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(currentHeight, currentHeight);
    private enum Direction{
        LEFT, UP, RIGHT, DOWN
    }

    private Direction currentDirection;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ArrayList<Room> list = ((MainActivity) context).getAllRooms();
        for(Room room: list){
            if(room.equals(this)){
                room.setCurrent(true);
                room.mainView.setBackgroundColor(Color.MAGENTA);
            } else {
                room.setCurrent(false);
                room.mainView.setBackgroundColor(Color.GREEN);
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isPointInsideView(event.getRawX(), event.getRawY(), this)){
                    isCenter = true;
                } else {
                    isCenter = false;
                    float screenX = event.getX();
                    float screenY = event.getY();
                    float viewX = screenX - this.getLeft();
                    float viewY = screenY - this.getTop();
//                    print("viewX:" + convertPixelsToDp(viewX, context));
//                    print("viewY:" + convertPixelsToDp(viewY, context));
                    float touchXCoordInView = convertPixelsToDp(viewX, context);
                    float touchYCoordInView = convertPixelsToDp(viewY, context);
                    float viewWidthDP = convertPixelsToDp(this.getWidth(), context);
                    float viewHeightDP = convertPixelsToDp(this.getHeight(), context);
                    if (touchXCoordInView < PIN_SIZE_DP && touchYCoordInView < PIN_SIZE_DP) {
                        topLeftPinTouched = true;
                        topRightPinTouched = false;
                        bottomLeftPinTouched = false;
                        bottomRightPinTouched = false;
                    } else if (touchXCoordInView > (viewWidthDP - PIN_SIZE_DP) && touchYCoordInView < PIN_SIZE_DP) {
                        topRightPinTouched = true;
                        topLeftPinTouched = false;
                        bottomLeftPinTouched = false;
                        bottomRightPinTouched = false;
                    } else if (touchXCoordInView < PIN_SIZE_DP && touchYCoordInView > (viewHeightDP - PIN_SIZE_DP)) {
                        bottomLeftPinTouched = true;
                        topLeftPinTouched = false;
                        topRightPinTouched = false;
                        bottomRightPinTouched = false;
                    } else if (touchXCoordInView > (viewWidthDP - PIN_SIZE_DP) && touchYCoordInView > (viewHeightDP - PIN_SIZE_DP)) {
                        bottomRightPinTouched = true;
                        topLeftPinTouched = false;
                        topRightPinTouched = false;
                        bottomLeftPinTouched = false;
                    } else {
                        topLeftPinTouched = false;
                        topRightPinTouched = false;
                        bottomLeftPinTouched = false;
                        bottomRightPinTouched = false;
                    }

                    print("topLeft: " + topLeftPinTouched);
                    print("topRight: " + topRightPinTouched);
                    print("bottomLeft: " + bottomLeftPinTouched);
                    print("bottomRight:" + bottomRightPinTouched);
                    startMoveX = event.getX();
                    startMoveY = event.getY();
                }

                dX = this.getX() - event.getRawX();
                dY = this.getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                if(isCenter){
                    this.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                } else {
                    endMoveX = event.getX();
                    endMoveY = event.getY();
                    deltaMoveX = endMoveX - startMoveX;
                    deltaMoveY = endMoveY - startMoveY;

                    if(Math.abs(deltaMoveX) > Math.abs(deltaMoveY)) {
                        if (deltaMoveX > 0) {
                            currentDirection = Direction.RIGHT;
                        } else {
                            currentDirection = Direction.LEFT;
                        }
                    } else {
                        if(deltaMoveY > 0){
                            currentDirection = Direction.DOWN;
                        } else {
                            currentDirection = Direction.UP;
                        }
                    }

                    if(topLeftPinTouched){
                        switch (currentDirection){
                            case LEFT:
                                dragX = -1;
                                break;
                            case UP:
                                dragY = -1;
                                break;
                            case RIGHT:
                                dragX = 1;
                                break;
                            case DOWN:
                                dragY = 1;
                                break;
                        }

                        lp.width = currentWidth + dragX;
                        lp.height = currentHeight + dragY;
                        rl.setLayoutParams(lp);
                        currentWidth = lp.width;
                        currentHeight = lp.height;
                    } else if(topRightPinTouched){
                        switch (currentDirection){
                            case LEFT:
                                dragX = -1;
                                break;
                            case UP:
                                dragY = -1;
                                break;
                            case RIGHT:
                                dragX = 1;
                                break;
                            case DOWN:
                                dragY = 1;
                                break;
                        }

                        lp.width = currentWidth + dragX;
                        lp.height = currentHeight + dragY;
                        rl.setLayoutParams(lp);
                        currentWidth = lp.width;
                        currentHeight = lp.height;
                    } else if(bottomLeftPinTouched) {
                        switch (currentDirection){
                            case LEFT:
                                dragX = -1;
                                break;
                            case UP:
                                dragY = -1;
                                break;
                            case RIGHT:
                                dragX = 1;
                                break;
                            case DOWN:
                                dragY = 1;
                                break;
                        }

                        lp.width = currentWidth + dragX;
                        lp.height = currentHeight + dragY;
                        rl.setLayoutParams(lp);
                        currentWidth = lp.width;
                        currentHeight = lp.height;
                    } else {
                        switch (currentDirection){
                            case LEFT:
                                dragX = -1;
                                break;
                            case UP:
                                dragY = -1;
                                break;
                            case RIGHT:
                                dragX = 1;
                                break;
                            case DOWN:
                                dragY = 1;
                                break;
                        }

                        lp.width = currentWidth + dragX;
                        lp.height = currentHeight + dragY;
                        rl.setLayoutParams(lp);
                        currentWidth = lp.width;
                        currentHeight = lp.height;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isCenter = false;
//                this.requestLayout();
//                    view.animate().cancel();
//                    view.animate().scaleX(1f).setDuration(1000).start();
//                    view.animate().scaleY(1f).setDuration(1000).start();
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
//        print("viewX: " + viewX);
//        print("viewY: " + viewY);

        // point is inside view bounds
        return ((x > (viewX + 20) && x < ((viewX + view.getWidth()) - 20)) &&
                (y > (viewY + 20) && y < ((viewY + view.getHeight()) - 20)));
    }

    private float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void print(String printThis){
        Log.d("QAZ", printThis);
    }
}
