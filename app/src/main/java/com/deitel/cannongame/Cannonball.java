package com.deitel.cannongame;

import android.graphics.Canvas;
import android.graphics.Rect;

    public class Cannonball extends GameElement {
        private float velocityX;
        private boolean onScreen;

         // constructor
         public Cannonball(CannonView view, int color, int soundId, int x,
             int y, int radius, float velocityX, float velocityY) {
            super(view, color, soundId, x, y,
                    2 * radius, 2 * radius, velocityY);
             this.velocityX = velocityX;
             onScreen = true;
         }

        // get Cannonball's radius
        private int getRadius() {
             return (shape.right - shape.left) / 2;
             }

        // test whether Cannonball collides with the given GameElement
        public boolean collidesWith(GameElement element) {
            return (Rect.intersects(shape, element.shape) && velocityX > 0);
            }

        // returns true if this Cannonball is on the screen
         public boolean isOnScreen() {
             return onScreen;
             }

         // reverses the Cannonball's horizontal velocity
         public void reverseVelocityX() {
             velocityX *= -1;
             }
        // updates the Cannonball's position
        @Override
        public void update(double interval) {
            super.update(interval);
            shape.offset((int) (velocityX * interval), 0);

            if (shape.top < 0 || shape.right < 0 ||
                    shape.bottom > view.getScreenHeight() ||
                    shape.right > view.getScreenWidth()) {
                onScreen = false;
            }
        }

         // draws the Cannonball on the given canvas
         @Override
         public void draw(Canvas canvas) {
             // Desenha um círculo usando o raio e a tinta configurada
             canvas.drawCircle(shape.left + getRadius(),
                     shape.top + getRadius(),
                     getRadius(),
                     paint);
         }
}
