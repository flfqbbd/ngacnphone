package gov.pianzong.util;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by Administrator on 13-6-30.
 */
public class HtppUtil {


    public static String getHtml(String uri, String cookie,String host,int timeout) {
        InputStream is = null;
        HostnameVerifier allHostsValid = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(!StringUtil.isEmpty(cookie))
                conn.setRequestProperty("Cookie", cookie);
            //conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept-Charset", "GBK");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            if(!StringUtil.isEmpty(host)){
                conn.setRequestProperty("Host", host);
            }
            if(timeout>0){
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout*2);
            }

            conn.connect();
            is = conn.getInputStream();
            if( "gzip".equals(conn.getHeaderField("Content-Encoding")) )
                is = new GZIPInputStream(is);
            String encoding =  getCharset( conn, "GBK");

            return IOUtils.toString(is, encoding);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    private static String getCharset(HttpURLConnection conn, String defaultValue){
        if(conn== null)
            return defaultValue;
        String contentType = conn.getHeaderField("Content-Type");
        if(StringUtil.isEmpty(contentType))
            return defaultValue;
        String startTag = "charset=";
        String endTag = " ";
        int start = contentType.indexOf(startTag);
        if( -1 == start)
            return defaultValue;
        start += startTag.length();
        int end = contentType.indexOf( endTag, start);
        if(-1==end)
            end = contentType.length();

        return contentType.substring(start, end);
    }
}
