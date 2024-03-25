package me.plopez.survivalgame.util;


import me.plopez.survivalgame.vector.VectorF;

public class Vector {
    public static VectorF multiplyVectors(VectorF a, VectorF b) {
        return new VectorF(a.x * b.x, a.y * b.y, a.z * b.z);
    }
    public static boolean boxCast(VectorF point, VectorF position, VectorF size) {
        if (point.x >= position.x && point.x <= position.x + size.x){
            return point.y >= position.y && point.y <= position.y + size.y;
        }
        return false;
    }
}

