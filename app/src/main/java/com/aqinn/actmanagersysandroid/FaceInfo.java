package com.aqinn.actmanagersysandroid;

import java.util.Arrays;

public class FaceInfo {
    public float x1;
    public float y1;
    public float x2;
    public float y2;
    public float score;
    public float[] landmarks;
    public float[][] keypoints;

    @Override
    public String toString() {
        return "FaceInfo{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", score=" + score +
                ", landmarks=" + Arrays.toString(landmarks) +
                ", keypoints=" + Arrays.toString(keypoints) +
                '}';
    }
}
