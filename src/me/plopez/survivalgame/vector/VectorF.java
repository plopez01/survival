package me.plopez.survivalgame.vector;

import processing.core.PApplet;

public class VectorF implements Vector {
    public float x;
    public float y;
    public float z;
    protected transient float[] array;

    public VectorF() {
    }

    public VectorF(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VectorF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public VectorF set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public VectorF set(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0.0F;
        return this;
    }

    public VectorF set(VectorF v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public VectorF set(float[] source) {
        if (source.length >= 2) {
            this.x = source[0];
            this.y = source[1];
        }

        if (source.length >= 3) {
            this.z = source[2];
        } else {
            this.z = 0.0F;
        }

        return this;
    }

    public static VectorF random2D() {
        return random2D((VectorF)null, (PApplet)null);
    }

    public static VectorF random2D(PApplet parent) {
        return random2D((VectorF)null, parent);
    }

    public static VectorF random2D(VectorF target) {
        return random2D(target, (PApplet)null);
    }

    public static VectorF random2D(VectorF target, PApplet parent) {
        return parent == null ? fromAngle((float)(Math.random() * Math.PI * 2.0), target) : fromAngle(parent.random(6.2831855F), target);
    }

    public static VectorF random3D() {
        return random3D((VectorF)null, (PApplet)null);
    }

    public static VectorF random3D(PApplet parent) {
        return random3D((VectorF)null, parent);
    }

    public static VectorF random3D(VectorF target) {
        return random3D(target, (PApplet)null);
    }

    public static VectorF random3D(VectorF target, PApplet parent) {
        float angle;
        float vz;
        if (parent == null) {
            angle = (float)(Math.random() * Math.PI * 2.0);
            vz = (float)(Math.random() * 2.0 - 1.0);
        } else {
            angle = parent.random(6.2831855F);
            vz = parent.random(-1.0F, 1.0F);
        }

        float vx = (float)(Math.sqrt((double)(1.0F - vz * vz)) * Math.cos((double)angle));
        float vy = (float)(Math.sqrt((double)(1.0F - vz * vz)) * Math.sin((double)angle));
        if (target == null) {
            target = new VectorF(vx, vy, vz);
        } else {
            target.set(vx, vy, vz);
        }

        return target;
    }

    public static VectorF fromAngle(float angle) {
        return fromAngle(angle, (VectorF)null);
    }

    public static VectorF fromAngle(float angle, VectorF target) {
        if (target == null) {
            target = new VectorF((float)Math.cos((double)angle), (float)Math.sin((double)angle), 0.0F);
        } else {
            target.set((float)Math.cos((double)angle), (float)Math.sin((double)angle), 0.0F);
        }

        return target;
    }

    public VectorF copy() {
        return new VectorF(this.x, this.y, this.z);
    }

    /** @deprecated */
    @Deprecated
    public VectorF get() {
        return this.copy();
    }

    public float[] get(float[] target) {
        if (target == null) {
            return new float[]{this.x, this.y, this.z};
        } else {
            if (target.length >= 2) {
                target[0] = this.x;
                target[1] = this.y;
            }

            if (target.length >= 3) {
                target[2] = this.z;
            }

            return target;
        }
    }

    public float mag() {
        return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
    }

    public float magSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public VectorF add(VectorF v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public VectorF add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public VectorF add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public static VectorF add(VectorF v1, VectorF v2) {
        return add(v1, v2, (VectorF)null);
    }

    public static VectorF add(VectorF v1, VectorF v2, VectorF target) {
        if (target == null) {
            target = new VectorF(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }

        return target;
    }

    public VectorF sub(VectorF v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public VectorF sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public VectorF sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public static VectorF sub(VectorF v1, VectorF v2) {
        return sub(v1, v2, (VectorF)null);
    }

    public static VectorF sub(VectorF v1, VectorF v2, VectorF target) {
        if (target == null) {
            target = new VectorF(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }

        return target;
    }

    public VectorF mult(float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        return this;
    }

    public static VectorF mult(VectorF v, float n) {
        return mult(v, n, (VectorF)null);
    }

    public static VectorF mult(VectorF v, float n, VectorF target) {
        if (target == null) {
            target = new VectorF(v.x * n, v.y * n, v.z * n);
        } else {
            target.set(v.x * n, v.y * n, v.z * n);
        }

        return target;
    }

    public VectorF div(float n) {
        this.x /= n;
        this.y /= n;
        this.z /= n;
        return this;
    }

    public static VectorF div(VectorF v, float n) {
        return div(v, n, (VectorF)null);
    }

    public static VectorF div(VectorF v, float n, VectorF target) {
        if (target == null) {
            target = new VectorF(v.x / n, v.y / n, v.z / n);
        } else {
            target.set(v.x / n, v.y / n, v.z / n);
        }

        return target;
    }

    public float dist(VectorF v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        float dz = this.z - v.z;
        return (float)Math.sqrt((double)(dx * dx + dy * dy + dz * dz));
    }

    public static float dist(VectorF v1, VectorF v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        float dz = v1.z - v2.z;
        return (float)Math.sqrt((double)(dx * dx + dy * dy + dz * dz));
    }

    public float dot(VectorF v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public float dot(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public static float dot(VectorF v1, VectorF v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public VectorF cross(VectorF v) {
        return this.cross(v, (VectorF)null);
    }

    public VectorF cross(VectorF v, VectorF target) {
        float crossX = this.y * v.z - v.y * this.z;
        float crossY = this.z * v.x - v.z * this.x;
        float crossZ = this.x * v.y - v.x * this.y;
        if (target == null) {
            target = new VectorF(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }

        return target;
    }

    public static VectorF cross(VectorF v1, VectorF v2, VectorF target) {
        float crossX = v1.y * v2.z - v2.y * v1.z;
        float crossY = v1.z * v2.x - v2.z * v1.x;
        float crossZ = v1.x * v2.y - v2.x * v1.y;
        if (target == null) {
            target = new VectorF(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }

        return target;
    }

    public VectorF normalize() {
        float m = this.mag();
        if (m != 0.0F && m != 1.0F) {
            this.div(m);
        }

        return this;
    }

    public VectorF normalize(VectorF target) {
        if (target == null) {
            target = new VectorF();
        }

        float m = this.mag();
        if (m > 0.0F) {
            target.set(this.x / m, this.y / m, this.z / m);
        } else {
            target.set(this.x, this.y, this.z);
        }

        return target;
    }

    public VectorF limit(float max) {
        if (this.magSq() > max * max) {
            this.normalize();
            this.mult(max);
        }

        return this;
    }

    public VectorF setMag(float len) {
        this.normalize();
        this.mult(len);
        return this;
    }

    public VectorF setMag(VectorF target, float len) {
        target = this.normalize(target);
        target.mult(len);
        return target;
    }

    public float heading() {
        float angle = (float)Math.atan2((double)this.y, (double)this.x);
        return angle;
    }

    /** @deprecated */
    @Deprecated
    public float heading2D() {
        return this.heading();
    }

    public VectorF setHeading(float angle) {
        float m = this.mag();
        this.x = (float)((double)m * Math.cos((double)angle));
        this.y = (float)((double)m * Math.sin((double)angle));
        return this;
    }

    public VectorF rotate(float theta) {
        float temp = this.x;
        this.x = this.x * PApplet.cos(theta) - this.y * PApplet.sin(theta);
        this.y = temp * PApplet.sin(theta) + this.y * PApplet.cos(theta);
        return this;
    }

    public VectorF lerp(VectorF v, float amt) {
        this.x = PApplet.lerp(this.x, v.x, amt);
        this.y = PApplet.lerp(this.y, v.y, amt);
        this.z = PApplet.lerp(this.z, v.z, amt);
        return this;
    }

    public static VectorF lerp(VectorF v1, VectorF v2, float amt) {
        VectorF v = v1.copy();
        v.lerp(v2, amt);
        return v;
    }

    public VectorF lerp(float x, float y, float z, float amt) {
        this.x = PApplet.lerp(this.x, x, amt);
        this.y = PApplet.lerp(this.y, y, amt);
        this.z = PApplet.lerp(this.z, z, amt);
        return this;
    }

    public static float angleBetween(VectorF v1, VectorF v2) {
        if (v1.x == 0.0F && v1.y == 0.0F && v1.z == 0.0F) {
            return 0.0F;
        } else if (v2.x == 0.0F && v2.y == 0.0F && v2.z == 0.0F) {
            return 0.0F;
        } else {
            double dot = (double)(v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
            double v1mag = Math.sqrt((double)(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z));
            double v2mag = Math.sqrt((double)(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z));
            double amt = dot / (v1mag * v2mag);
            if (amt <= -1.0) {
                return 3.1415927F;
            } else {
                return amt >= 1.0 ? 0.0F : (float)Math.acos(amt);
            }
        }
    }

    public String toString() {
        return "[ " + this.x + ", " + this.y + ", " + this.z + " ]";
    }

    public float[] array() {
        if (this.array == null) {
            this.array = new float[3];
        }

        this.array[0] = this.x;
        this.array[1] = this.y;
        this.array[2] = this.z;
        return this.array;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof VectorF p)) {
            return false;
        } else {
            return this.x == p.x && this.y == p.y && this.z == p.z;
        }
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + Float.floatToIntBits(this.x);
        result = 31 * result + Float.floatToIntBits(this.y);
        result = 31 * result + Float.floatToIntBits(this.z);
        return result;
    }
}
