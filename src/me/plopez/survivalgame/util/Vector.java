package me.plopez.survivalgame.util;

import processing.core.PVector;

public class Vector {
    public static PVector multiplyVectors(PVector a, PVector b) {
        return new PVector(a.x * b.x, a.y * b.y, a.z * b.z);
    }
    public static boolean boxCast(PVector point, PVector position, PVector size) {
        if (point.x >= position.x && point.x <= position.x + size.x){
            return point.y >= position.y && point.y <= position.y + size.y;
        }
        return false;
    }
}

