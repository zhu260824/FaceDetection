package com.zl.face;

import android.graphics.Rect;

import java.util.Arrays;

public class MTFaceInfo {
    private Rect rect;
    private float score;
    private float[] points;

    public MTFaceInfo() {
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float[] getPoints() {
        return points;
    }

    public void setPoints(float[] points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "MTFaceInfo{" +
                "rect=" + rect +
                ", score=" + score +
                ", ponit=" + Arrays.toString(points) +
                '}';
    }
}
