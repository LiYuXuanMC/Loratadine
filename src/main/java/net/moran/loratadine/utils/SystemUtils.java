package net.moran.loratadine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemUtils {
   public static boolean isServiceExist(String serviceName) {
      try {
         Process process = Runtime.getRuntime().exec("tasklist /SVC");
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

         String line;
         while ((line = reader.readLine()) != null) {
            if (line.contains(serviceName)) {
               return true;
            }
         }
      } catch (IOException var4) {
         System.out.println("Error executing command.");
      }

      return false;
   }
}
