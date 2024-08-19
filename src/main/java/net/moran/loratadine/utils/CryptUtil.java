package net.moran.loratadine.utils;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class CryptUtil {
   public static class Base64Crypt {
      public static String decrypt(String message) {
         return new String(Base64.getDecoder().decode(message));
      }

      public static String encrypt(String message) {
         return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
      }
   }

   public static class RSA {
      private static final String RSA_KEY_ALGORITHM = "RSA";

      public static byte[] encryptByPublicKey(byte[] data, String publicKeyStr) {
         try {
            byte[] pubKey = Base64.getDecoder().decode(publicKeyStr);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(1, publicKey);
            byte[] encrypt = cipher.doFinal(Base64.getEncoder().encode(data));
            return Base64.getEncoder().encode(encrypt);
         } catch (Throwable var8) {
            var8.printStackTrace();
            return new byte[0];
         }
      }

      public static String decryptByPrivateKey(byte[] data, String privateKeyStr) {
         try {
            byte[] priKey = Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(2, privateKey);
            return new String(Base64.getDecoder().decode(cipher.doFinal(Base64.getDecoder().decode(data))));
         } catch (Throwable var7) {
            var7.printStackTrace();
            return "";
         }
      }

      public static byte[] decryptByPrivateKeyByte(byte[] data, String privateKeyStr) {
         try {
            byte[] priKey = Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(2, privateKey);
            return Base64.getDecoder().decode(cipher.doFinal(Base64.getDecoder().decode(data)));
         } catch (Throwable var7) {
            var7.printStackTrace();
            return new byte[0];
         }
      }
   }
}
