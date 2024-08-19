package net.moran.loratadine.utils.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class WebUtils {
   public static String get(String url, boolean preventRedirect) throws IOException {
      HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
      con.setInstanceFollowRedirects(!preventRedirect);
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      StringBuilder response = new StringBuilder();

      String inputLine;
      while ((inputLine = in.readLine()) != null) {
         response.append(inputLine);
         response.append("\n");
      }

      in.close();
      return response.toString();
   }

   public static String readContent(String stringURL) throws IOException {
      HttpURLConnection httpConnection = (HttpURLConnection)new URL(stringURL).openConnection();
      httpConnection.setConnectTimeout(10000);
      httpConnection.setReadTimeout(10000);
      httpConnection.setRequestMethod("GET");
      httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
      HttpURLConnection.setFollowRedirects(true);
      httpConnection.setDoOutput(true);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
      StringBuilder stringBuilder = new StringBuilder();

      String line;
      while ((line = bufferedReader.readLine()) != null) {
         stringBuilder.append(line).append("\n");
      }

      bufferedReader.close();
      return stringBuilder.toString();
   }

   public static boolean isNetworkConnected() {
      try {
         URL url = new URL("https://www.baidu.com");
         URLConnection connection = url.openConnection();
         connection.connect();
         return true;
      } catch (IOException var2) {
         return false;
      }
   }
}
