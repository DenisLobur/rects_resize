package com.example.rooms;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Room extends ConstraintLayout {

    private ImageView topLeftPin;
    private ImageView topRightPin;
    private ImageView bottomLeftPin;
    private ImageView bottomRightPin;
    private TextView mainView;
    private float dX, dY;
    private Context context;


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

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_room, this, false);

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

    boolean isCenter = false;
    boolean topLeftPinTouched;
    boolean topRightPinTouched;
    boolean bottomLeftPinTouched;
    boolean bottomRightPinTouched;
    private final int PIN_SIZE_DP = 16;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
