package me.plopez.survivalgame.vector;

import processing.core.PApplet;

public class VectorI implements Vector {
    public int x;
    public int y;
    public int z;
    protected transient float[] array;

    public VectorI() {
    }

    public VectorI(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VectorI(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public VectorI set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public VectorI set(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        return this;
    }

    public VectorI set(VectorI v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        return this;
    }

    public VectorI set(int[] source) {
        if (source.length >= 2) {
            this.x = source[0];
            this.y = source[1];
        }

        if (source.length >= 3) {
            this.z = source[2];
        } else {
            this.z = 0;
        }

        return this;
    }

    public static VectorI random2D() {
        return random2D((VectorI)null, (PApplet)null);
    }

    public static VectorI random2D(PApplet parent) {
        return random2D((VectorI)null, parent);
    }

    public static VectorI random2D(VectorI target) {
        return random2D(target, (PApplet)null);
    }

    public static VectorI random2D(VectorI target, PApplet parent) {
        return parent == null ? fromAngle((float)(Math.random() * Math.PI * 2.0), target) : fromAngle(parent.random(6.2831855F), target);
    }

    public static VectorI random3D() {
        return random3D((VectorI)null, (PApplet)null);
    }

    public static VectorI random3D(PApplet parent) {
        return random3D((VectorI)null, parent);
    }

    public static VectorI random3D(VectorI target) {
        return random3D(target, (PApplet)null);
    }

    public static VectorI random3D(VectorI target, PApplet parent) {
        float angle;
        int vz;
        if (parent == null) {
            angle = (float)(Math.random() * Math.PI * 2.0);
            vz = (int)(Math.random() * 2.0 - 1.0);
        } else {
            angle = parent.random(6.2831855F);
            vz = (int)parent.random(-1.0F, 1.0F);
        }

        int vx = (int)(Math.sqrt((double)(1.0F - vz * vz)) * Math.cos((double)angle));
        int vy = (int)(Math.sqrt((double)(1.0F - vz * vz)) * Math.sin((double)angle));
        if (target == null) {
            target = new VectorI(vx, vy, vz);
        } else {
            target.set(vx, vy, vz);
        }

        return target;
    }

    public static VectorI fromAngle(float angle) {
        return fromAngle(angle, (VectorI)null);
    }

    public static VectorI fromAngle(float angle, VectorI target) {
        if (target == null) {
            target = new VectorI((int)Math.cos((double)angle), (int)Math.sin((double)angle), 0);
        } else {
            target.set((int)Math.cos((double)angle), (int)Math.sin((double)angle), 0);
        }

        return target;
    }

    public VectorI copy() {
        return new VectorI(this.x, this.y, this.z);
    }

    /** @deprecated */
    @Deprecated
    public VectorI get() {
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

    public int mag() {
        return (int)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
    }

    public float magSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public VectorI add(VectorI v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public VectorI add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public VectorI add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public static VectorI add(VectorI v1, VectorI v2) {
        return add(v1, v2, (VectorI)null);
    }

    public static VectorI add(VectorI v1, VectorI v2, VectorI target) {
        if (target == null) {
            target = new VectorI(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        } else {
            target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        }

        return target;
    }

    public VectorI sub(VectorI v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    public VectorI sub(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public VectorI sub(int x, int y, int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public static VectorI sub(VectorI v1, VectorI v2) {
        return sub(v1, v2, (VectorI)null);
    }

    public static VectorI sub(VectorI v1, VectorI v2, VectorI target) {
        if (target == null) {
            target = new VectorI(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }

        return target;
    }

    public VectorI mult(int n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        return this;
    }

    public static VectorI mult(VectorI v, int n) {
        return mult(v, n, (VectorI)null);
    }

    public static VectorI mult(VectorI v, int n, VectorI target) {
        if (target == null) {
            target = new VectorI(v.x * n, v.y * n, v.z * n);
        } else {
            target.set(v.x * n, v.y * n, v.z * n);
        }

        return target;
    }

    public VectorI div(int n) {
        this.x /= n;
        this.y /= n;
        this.z /= n;
        return this;
    }

    public static VectorI div(VectorI v, int n) {
        return div(v, n, (VectorI)null);
    }

    public static VectorI div(VectorI v, int n, VectorI target) {
        if (target == null) {
            target = new VectorI(v.x / n, v.y / n, v.z / n);
        } else {
            target.set(v.x / n, v.y / n, v.z / n);
        }

        return target;
    }

    public float dist(VectorI v) {
        float dx = this.x - v.x;
        float dy = this.y - v.y;
        float dz = this.z - v.z;
        return (float)Math.sqrt((double)(dx * dx + dy * dy + dz * dz));
    }

    public static float dist(VectorI v1, VectorI v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        float dz = v1.z - v2.z;
        return (float)Math.sqrt((double)(dx * dx + dy * dy + dz * dz));
    }

    public float dot(VectorI v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public float dot(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public static float dot(VectorI v1, VectorI v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public VectorI cross(VectorI v) {
        return this.cross(v, (VectorI)null);
    }

    public VectorI cross(VectorI v, VectorI target) {
        int crossX = this.y * v.z - v.y * this.z;
        int crossY = this.z * v.x - v.z * this.x;
        int crossZ = this.x * v.y - v.x * this.y;
        if (target == null) {
            target = new VectorI(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }

        return target;
    }

    public static VectorI cross(VectorI v1, VectorI v2, VectorI target) {
        int crossX = v1.y * v2.z - v2.y * v1.z;
        int crossY = v1.z * v2.x - v2.z * v1.x;
        int crossZ = v1.x * v2.y - v2.x * v1.y;
        if (target == null) {
            target = new VectorI(crossX, crossY, crossZ);
        } else {
            target.set(crossX, crossY, crossZ);
        }

        return target;
    }

    public VectorI normalize() {
        int m = this.mag();
        if (m != 0.0F && m != 1.0F) {
            this.div(m);
        }

        return this;
    }

    public VectorI normalize(VectorI target) {
        if (target == null) {
            target = new VectorI();
        }

        int m = this.mag();
        if (m > 0) {
            target.set(this.x / m, this.y / m, this.z / m);
        } else {
            target.set(this.x, this.y, this.z);
        }

        return target;
    }

    public VectorI limit(int max) {
        if (this.magSq() > max * max) {
            this.normalize();
            this.mult(max);
        }

        return this;
    }

    public VectorI setMag(int len) {
        this.normalize();
        this.mult(len);
        return this;
    }

    public VectorI setMag(VectorI target, int len) {
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

    public VectorI setHeading(float angle) {
        float m = this.mag();
        this.x = (int)((double)m * Math.cos((double)angle));
        this.y = (int)((double)m * Math.sin((double)angle));
        return this;
    }

    public VectorI rotate(float theta) {
        float temp = this.x;
        this.x = (int) (this.x * PApplet.cos(theta) - this.y * PApplet.sin(theta));
        this.y = (int) (temp * PApplet.sin(theta) + this.y * PApplet.cos(theta));
        return this;
    }

    public VectorI lerp(VectorI v, float amt) {
        this.x = (int) PApplet.lerp(this.x, v.x, amt);
        this.y = (int) PApplet.lerp(this.y, v.y, amt);
        this.z = (int) PApplet.lerp(this.z, v.z, amt);
        return this;
    }

    public static VectorI lerp(VectorI v1, VectorI v2, float amt) {
        VectorI v = v1.copy();
        v.lerp(v2, amt);
        return v;
    }

    public VectorI lerp(int x, int y, int z, float amt) {
        this.x = (int) PApplet.lerp(this.x, x, amt);
        this.y = (int) PApplet.lerp(this.y, y, amt);
        this.z = (int) PApplet.lerp(this.z, z, amt);
        return this;
    }

    public static float angleBetween(VectorI v1, VectorI v2) {
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
        if (!(obj instanceof VectorI p)) {
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
