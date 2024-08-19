package net.moran.loratadine.utils;

import cn.lzq.injection.Fucker;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteUtil {
   private static final Logger log = LoggerFactory.getLogger(ByteUtil.class);
   private final Channel channel;
   public static final String receivePrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC4xVR6yFhaTWnfe9SUy2YAIY+SqdB/WOCgsMcN4A0YwbfrZjlvBCRKxlJ32yDTF8tN/HBaOo/lpHX3VRwhpvQ0yL5IFBBSUY4JIc2utIuVdLc3ZaLDuhuh08hOrvrHCWlFi523mJvB4Xzi+6hau6FFs7qfp/SRfGK2tjS7sA7oqky3PVH3nBV+X6f3rI7/a3POFqboSwdm0jnqTvEpNr75H1gy47/V7L2oAgGIBspFvqLj8VgtKNX8clVTva9UTlnLtKG+rDf1nCr6fiRDotXP+QnD1kzVEl6Xndw8pdXpBQo/GsUdIixAdBN5mW/wz/4tdPbK9jmh7CFWqbEM6IPZAgMBAAECggEBAIn0q9LQ60bhLf9y0ZIXG51VcYEr0USD85OG6dhuRNkLKmtT8+XzzbUWxlQ/BA8YYO9lX+2+c9oeJQX6QfrOUN9oUso2UMllowsmdg/PNbjtYC64cAJ7Xk5BdflppEwftr36NFP0Wbc0yK4g/95e4VbNjrXODUji+kE5Yb4RAdCssbzd3Z5ftqK1U9eB5ASKgYuo3w9bkG6du7iFnGI+BeSHOkjVLzY2ETIq9oPknMTQAif1JzXvLGH2OefqTF8To8kkxn17BrJncBzwmExkrCPOxqc2DoMrCeuN/x28GbYxS40f+9cGBC0EtGg0rK7O4uMivAqATA8YBl3p8AMuYUECgYEA2dB227WxQjsoqHhe0qa2Aq1YIJRsFJmfiwW3D8VMAzvJTSmuSjKT2ttHhI7wWVbKxqksugpwsCb0K+omXSPSaqf5MvtXy/4lbWzmfG4bALlDmuu11EDqm8nj6tXzYHuyXBg5Cff+lqmLaEtU0CoArmKFZ0gIeWn57OoxeR0EyG0CgYEA2SneiTH2xO+R3uN+Od/3ukxdSJzZvRzFpCqhBVKeXroGFlYTlBcLwIp+YduLFbSFNCThy1e8YkPj2MT/yYIJXWEA5Cwv4dylKg7ACcnJFHldQTEq4jY6EP6HTnxhrvGAy1UXTJJuVvsfqEm9U5Ar8lfUK+R3uugBKPIQ5E6SXZ0CgYBEfO8SsLPW7oEfUBIIzJDIkLb4L5M4ewGWlip0lAYNsjvevm9mNzcUhwSa4tMiVE8YXlOJAAVk7iqysEJ14PClxsFtzWhS5Uvhd2+Vyo1FEfv294zJ+8uJRtcanUGUofB4UsmEn+z2dMM3/Q/jEIH8U1A9JII9oxwJ6a26tmwtlQKBgCFTLIQvN6gm/2KN1Iv7E5/yIgqHj15W8PltVUJk2Eq/DzoUQXLjSnlkh1pq/1/4UMycsE2tDAqkUm2sZXg9zUQYI7PgGAT4AByBIPUfkwziRu4/Jk6KdcSv2oGv0qmvA82wJCArBGWyqbwAfN467JOG6NdHexwiiDMJWpA+gnV9AoGBAJBk+33lsD9NEyRHdwnTXaoNHreifNux8XdH24YqaAdVCkym1RBtbD4DoNC2aZVFlRfrWP3alQQ98jzqm80HRRic1uqqvdOgAxDzCa+ZOBm4UgiWtbsfDRlZHvuJxd+j0jn0hBbLZJCkUCxBqutTHjnp8lscv2u1Q4nfSftDnKIz";
   private static final String sendPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnFjDlblXYSsBa5/9XgV3o85V+6k1xdzRgXTuwp7cROwnFOKcZ7uksaxCR5Tu1JmSIpKqVsmAMy64MqrwlC4d1GJyj1AcThixV9s42UH6gkLJiXQKdNUqOtwcSPPVH5ZA01qtMqgCk+7VybXihNCLkAKnhYsoNnKltKcRS43kaJ+POTvNcT07Qh3zNwOCpop+/8DbQHg7GLBK2L5zfJQXwONHpaaJ72L1+z/yu7VHpoCEVBrlHIQBIZ3iwUgD0WQP4wZBMDPT6t1i18YzlDEos2MmQdYXOgFnzj6bnQkfwMrhfnQUwPb6bDLzA14ZEvRrh3cxcVsE9Nlh43BOV5FsQQIDAQAB";
   private static final int SLICE_SIZE = 128;

   public ByteUtil(Channel channel) {
      this.channel = channel;
   }

   public static String read(Object msg) {
      ByteBuf byteBuf = (ByteBuf)msg;
      byte[] bytes = new byte[byteBuf.readableBytes()];
      byteBuf.readBytes(bytes);
      return CryptUtil.RSA.decryptByPrivateKey(
         bytes,
         "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC4xVR6yFhaTWnfe9SUy2YAIY+SqdB/WOCgsMcN4A0YwbfrZjlvBCRKxlJ32yDTF8tN/HBaOo/lpHX3VRwhpvQ0yL5IFBBSUY4JIc2utIuVdLc3ZaLDuhuh08hOrvrHCWlFi523mJvB4Xzi+6hau6FFs7qfp/SRfGK2tjS7sA7oqky3PVH3nBV+X6f3rI7/a3POFqboSwdm0jnqTvEpNr75H1gy47/V7L2oAgGIBspFvqLj8VgtKNX8clVTva9UTlnLtKG+rDf1nCr6fiRDotXP+QnD1kzVEl6Xndw8pdXpBQo/GsUdIixAdBN5mW/wz/4tdPbK9jmh7CFWqbEM6IPZAgMBAAECggEBAIn0q9LQ60bhLf9y0ZIXG51VcYEr0USD85OG6dhuRNkLKmtT8+XzzbUWxlQ/BA8YYO9lX+2+c9oeJQX6QfrOUN9oUso2UMllowsmdg/PNbjtYC64cAJ7Xk5BdflppEwftr36NFP0Wbc0yK4g/95e4VbNjrXODUji+kE5Yb4RAdCssbzd3Z5ftqK1U9eB5ASKgYuo3w9bkG6du7iFnGI+BeSHOkjVLzY2ETIq9oPknMTQAif1JzXvLGH2OefqTF8To8kkxn17BrJncBzwmExkrCPOxqc2DoMrCeuN/x28GbYxS40f+9cGBC0EtGg0rK7O4uMivAqATA8YBl3p8AMuYUECgYEA2dB227WxQjsoqHhe0qa2Aq1YIJRsFJmfiwW3D8VMAzvJTSmuSjKT2ttHhI7wWVbKxqksugpwsCb0K+omXSPSaqf5MvtXy/4lbWzmfG4bALlDmuu11EDqm8nj6tXzYHuyXBg5Cff+lqmLaEtU0CoArmKFZ0gIeWn57OoxeR0EyG0CgYEA2SneiTH2xO+R3uN+Od/3ukxdSJzZvRzFpCqhBVKeXroGFlYTlBcLwIp+YduLFbSFNCThy1e8YkPj2MT/yYIJXWEA5Cwv4dylKg7ACcnJFHldQTEq4jY6EP6HTnxhrvGAy1UXTJJuVvsfqEm9U5Ar8lfUK+R3uugBKPIQ5E6SXZ0CgYBEfO8SsLPW7oEfUBIIzJDIkLb4L5M4ewGWlip0lAYNsjvevm9mNzcUhwSa4tMiVE8YXlOJAAVk7iqysEJ14PClxsFtzWhS5Uvhd2+Vyo1FEfv294zJ+8uJRtcanUGUofB4UsmEn+z2dMM3/Q/jEIH8U1A9JII9oxwJ6a26tmwtlQKBgCFTLIQvN6gm/2KN1Iv7E5/yIgqHj15W8PltVUJk2Eq/DzoUQXLjSnlkh1pq/1/4UMycsE2tDAqkUm2sZXg9zUQYI7PgGAT4AByBIPUfkwziRu4/Jk6KdcSv2oGv0qmvA82wJCArBGWyqbwAfN467JOG6NdHexwiiDMJWpA+gnV9AoGBAJBk+33lsD9NEyRHdwnTXaoNHreifNux8XdH24YqaAdVCkym1RBtbD4DoNC2aZVFlRfrWP3alQQ98jzqm80HRRic1uqqvdOgAxDzCa+ZOBm4UgiWtbsfDRlZHvuJxd+j0jn0hBbLZJCkUCxBqutTHjnp8lscv2u1Q4nfSftDnKIz"
      );
   }

   public void send(String text) {
      byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
      int totalLength = textBytes.length;
      int offset = 0;

      while (offset < totalLength) {
         int sliceLength = Math.min(128, totalLength - offset);
         byte[] slice = new byte[sliceLength];
         System.arraycopy(textBytes, offset, slice, 0, sliceLength);
         byte[] encryptedSlice = CryptUtil.RSA.encryptByPublicKey(
            slice,
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnFjDlblXYSsBa5/9XgV3o85V+6k1xdzRgXTuwp7cROwnFOKcZ7uksaxCR5Tu1JmSIpKqVsmAMy64MqrwlC4d1GJyj1AcThixV9s42UH6gkLJiXQKdNUqOtwcSPPVH5ZA01qtMqgCk+7VybXihNCLkAKnhYsoNnKltKcRS43kaJ+POTvNcT07Qh3zNwOCpop+/8DbQHg7GLBK2L5zfJQXwONHpaaJ72L1+z/yu7VHpoCEVBrlHIQBIZ3iwUgD0WQP4wZBMDPT6t1i18YzlDEos2MmQdYXOgFnzj6bnQkfwMrhfnQUwPb6bDLzA14ZEvRrh3cxcVsE9Nlh43BOV5FsQQIDAQAB"
         );
         ByteBuf buffer = this.channel.alloc().buffer();
         boolean hasMoreSlices = offset + sliceLength < totalLength;
         buffer.writeInt(encryptedSlice.length + 1);
         buffer.writeBoolean(hasMoreSlices);
         buffer.writeBytes(encryptedSlice);
         this.channel.writeAndFlush(buffer);
         offset += sliceLength;
      }
   }

   public void online(String name) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("Type", "online");
      jsonObject.addProperty("Player", CryptUtil.Base64Crypt.encrypt(name));
      this.send(jsonObject.toString());
   }

   public void jumping() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("Type", "jumping");
      this.send(jsonObject.toString());
   }

   public void login() {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("Type", "login");
      JsonObject loginRequest = new JsonObject();
      loginRequest.addProperty("a", Fucker.loginUsername);
      loginRequest.addProperty("ap", Fucker.loginPassword);
      if (Fucker.requestText != null) {
         loginRequest.addProperty("aa", Fucker.requestText);
      }

      loginRequest.addProperty("ab", "2");
      loginRequest.addProperty("b", HWIDUtil.getUUID());
      jsonObject.addProperty("a", CryptUtil.Base64Crypt.encrypt(loginRequest.toString()));
      this.send(jsonObject.toString());
   }

   public void message(String text) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("Type", "message");
      jsonObject.addProperty("Text", CryptUtil.Base64Crypt.encrypt(text));
      this.send(jsonObject.toString());
   }
}
