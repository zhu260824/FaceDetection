package com.zl.face;


import android.graphics.Rect;

import java.io.Serializable;

public class YSQFaceInfo implements Serializable {
    private static final long serialVersionUID = 5146425624356504203L;
    private Rect faceRect;
    private int faceConfidence;
    private int faceAngle;

    public YSQFaceInfo() {
    }

    public YSQFaceInfo(Rect faceRect, int faceConfidence, int faceAngle) {
        this.faceRect = faceRect;
        this.faceConfidence = faceConfidence;
        this.faceAngle = faceAngle;
    }

    public Rect getFaceRect() {
        return faceRect;
    }

    public void setFaceRect(Rect faceRect) {
        this.faceRect = faceRect;
    }

    public int getFaceConfidence() {
        return faceConfidence;
    }

    public void setFaceConfidence(int faceConfidence) {
        this.faceConfidence = faceConfidence;
    }

    public int getFaceAngle() {
        return faceAngle;
    }

    public void setFaceAngle(int faceAngle) {
        this.faceAngle = faceAngle;
    }

    @Override
    public String toString() {
        return "YSQFaceInfo{" +
                "faceRect=" + faceRect +
                ", faceConfidence=" + faceConfidence +
                ", faceAngle=" + faceAngle +
                '}';
    }
}
