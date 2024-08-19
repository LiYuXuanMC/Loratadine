package net.moran.loratadine.utils.vector;

import lombok.Generated;

public class Vector3d {
   public double x;
   public double y;
   public double z;

   public Vector3d(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Vector3d add(double x, double y, double z) {
      return new Vector3d(this.x + x, this.y + y, this.z + z);
   }

   public Vector3d add(Vector3d vector) {
      return this.add(vector.x, vector.y, vector.z);
   }

   public Vector3d subtract(double x, double y, double z) {
      return this.add(-x, -y, -z);
   }

   public Vector3d subtract(Vector3d vector) {
      return this.add(-vector.x, -vector.y, -vector.z);
   }

   public double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public Vector3d multiply(double v) {
      return new Vector3d(this.x * v, this.y * v, this.z * v);
   }

   public double distance(Vector3d vector3d) {
      return Math.sqrt(Math.pow(vector3d.x - this.x, 2.0) + Math.pow(vector3d.y - this.y, 2.0) + Math.pow(vector3d.z - this.z, 2.0));
   }

   @Override
   public boolean equals(Object obj) {
      return !(obj instanceof Vector3d vector)
         ? false
         : Math.floor(this.x) == Math.floor(vector.x) && Math.floor(this.y) == Math.floor(vector.y) && Math.floor(this.z) == Math.floor(vector.z);
   }

   @Generated
   public void setX(double x) {
      this.x = x;
   }

   @Generated
   public void setY(double y) {
      this.y = y;
   }

   @Generated
   public void setZ(double z) {
      this.z = z;
   }
}
