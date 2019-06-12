package zeusees.tracking;

import android.graphics.Rect;

import java.io.Serializable;
import java.util.Arrays;

public class Face implements Serializable {
    private static final long serialVersionUID = 1779335963296893484L;
    private int ID;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private int height;
    private int width;
    private int[] landmarks;

    public Face() {
    }

    public Face(int x1, int y1, int x2, int y2) {
        this.left = x1;
        this.top = y1;
        this.right = x2;
        this.bottom = y2;
        this.height = y2 - y1;
        this.width = x2 - x1;
        this.landmarks = new int[106 * 2];
    }


    public Face(int id, int x1, int y1, int _width, int _height) {
        this.ID = id;
        this.left = x1;
        this.top = y1;
        this.right = x1 + _width;
        this.bottom = y1 + _height;
        this.width = _width;
        this.height = _height;
    }

    public Face(int id, int x1, int y1, int _width, int _height, int[] landmark) {
        this.ID = id;
        this.left = x1;
        this.top = y1;
        this.right = x1 + _width;
        this.bottom = y1 + _height;
        this.width = _width;
        this.height = _height;
        this.landmarks = landmark;
    }

    public Rect toRect() {
        return new Rect(left, top, right, bottom);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int[] getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(int[] landmarks) {
        this.landmarks = landmarks;
    }

    @Override
    public String toString() {
        return "Face{" +
                "ID=" + ID +
                ", left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", height=" + height +
                ", width=" + width +
                ", landmarks=" + Arrays.toString(landmarks) +
                '}';
    }

}
