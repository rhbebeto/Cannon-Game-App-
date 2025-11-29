package com.deitel.cannongame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class CannonView extends SurfaceView implements SurfaceHolder.Callback{
     private static final String TAG = "CannonView"; // for logging errors

        // constants for game play
         public static final int MISS_PENALTY = 2; // seconds deducted on a miss
 public static final int HIT_REWARD = 3; // seconds added on a hit

         // constants for the Cannon
         public static final double CANNON_BASE_RADIUS_PERCENT = 3.0 / 40;
 public static final double CANNON_BARREL_WIDTH_PERCENT = 3.0 / 40;
 public static final double CANNON_BARREL_LENGTH_PERCENT = 1.0 / 10;

        // constants for the Cannonball
         public static final double CANNONBALL_RADIUS_PERCENT = 3.0 / 80;
 public static final double CANNONBALL_SPEED_PERCENT = 3.0 / 2;

        // constants for the Targets
         public static final double TARGET_WIDTH_PERCENT = 1.0 / 40;
 public static final double TARGET_LENGTH_PERCENT = 3.0 / 20;
 public static final double TARGET_FIRST_X_PERCENT = 3.0 / 5;
 public static final double TARGET_SPACING_PERCENT = 1.0 / 60;
public static final double TARGET_PIECES = 9;
 public static final double TARGET_MIN_SPEED_PERCENT = 3.0 / 4;
 public static final double TARGET_MAX_SPEED_PERCENT = 6.0 / 4;

         // constants for the Blocker
        public static final double BLOCKER_WIDTH_PERCENT = 1.0 / 40;
 public static final double BLOCKER_LENGTH_PERCENT = 1.0 / 4;
 public static final double BLOCKER_X_PERCENT = 1.0 / 2;
 public static final double BLOCKER_SPEED_PERCENT = 1.0;

         // text size 1/18 of screen width
         public static final double TEXT_SIZE_PERCENT = 1.0 / 18;

         private CannonThread cannonThread; // controls the game loop
private Activity activity; // to display Game Over dialog in GUI thread
private boolean dialogIsDisplayed = false;

         // game objects
         private Cannon cannon;
    private Blocker blocker;
 private ArrayList<Target> targets;

         // dimension variables
         private int screenWidth;
 private int screenHeight;

         // variables for the game loop and tracking statistics
         private boolean gameOver; // is the game over?
 private double timeLeft; // time remaining in seconds
 private int shotsFired; // shots the user has fired
 private double totalElapsedTime; // elapsed seconds

        // constants and variables for managing sounds
         public static final int TARGET_SOUND_ID = 0;
 public static final int CANNON_SOUND_ID = 1;
 public static final int BLOCKER_SOUND_ID = 2;
    private SoundPool soundPool; // plays sound effects
    private SparseIntArray soundMap; // maps IDs to SoundPool
    // Paint variables used when drawing each item on the screen
 private Paint textPaint; // Paint used to draw text
 private Paint backgroundPaint; // Paint used to clear the drawing area

    public CannonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context; // store reference to MainActivity
        getHolder().addCallback(this);

        // configure audio attributes for game audio
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        // initialize SoundPool to play the app's three sound effects
        oundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(1);
        builder.setAudioAttributes(attrBuilder.build());
        soundPool = builder.build();

        // create Map of sounds and pre-load sounds
        soundMap = new SparseIntArray(3); // create new SparseIntArray
        soundMap.put(TARGET_SOUND_ID,
        soundPool.load(context, R.raw.target_hit, 1));

        soundMap.put(CANNON_SOUND_ID,
                soundPool.load(context, R.raw.cannon_fire, 1));
        soundMap.put(BLOCKER_SOUND_ID,
                 soundPool.load(context, R.raw.blocker_hit, 1));
        textPaint = new Paint();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
    }

    // called when the size of the SurfaceView changes,
    // such as when it's first added to the View hierarchy
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w; // store CannonView's width
        screenHeight = h; // store CannonView's height

        // configure text properties
        textPaint.setTextSize((int) (TEXT_SIZE_PERCENT * screenHeight));
        textPaint.setAntiAlias(true); // smoothes the text
    }

    // get width of the game screen
    public int getScreenWidth() {
         return screenWidth;
        }

    // get height of the game screen
    public int getScreenHeight() {
         return screenHeight;
    }

    // plays a sound with the given soundId in soundMap
    public void playSound(int soundId) {
        soundPool.play(soundMap.get(soundId), 1, 1, 1, 0, 1f);
    }

}
