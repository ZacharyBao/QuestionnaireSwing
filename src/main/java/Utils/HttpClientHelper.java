package Utils;

import Entities.QuestionEntity;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpClientHelper {
    public List<QuestionEntity> getQuestionsByQuestionnaireId(Long qid) {
        List<QuestionEntity> allQuestions = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // create httpget.
            HttpGet httpget = new HttpGet("http://localhost:8080/questions/" + qid);
            System.out.println("executing request " + httpget.getURI());
            // execute get request.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // get response entity.
                HttpEntity entity = response.getEntity();
                //allQuestions=(List<QuestionEntity>)entity;
                System.out.println("--------------------------------------");
                // print response status.
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    String strResult = EntityUtils.toString(entity);
                    // print response content length.
                    //System.out.println("Response content length: " + entity.getContentLength());
                    // print response content.
                    //System.out.println("Response content: " + strResult);
                    allQuestions = JSONObject.parseArray(strResult, QuestionEntity.class);//transfer JSON String into List<QuestionEntity>
                    //System.out.println(allQuestions.get(0).getQuestionOptions());
                }
                System.out.println("------------------------------------");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close connection and resource
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allQuestions;
    }

    public void saveAnswer(Map<String, Object> answerInfo) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            //Create HttpPost
            HttpPost httpPost = new HttpPost("http://localhost:8080/answer");
            //create parameter list
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            Iterator iterator = answerInfo.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                formParams.add(new BasicNameValuePair(elem.getKey(), String.valueOf(elem.getValue())));
            }
            if(formParams.size()>0) {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
                httpPost.setEntity(formEntity);
            }
            System.out.println("executing request: " + httpPost.getURI());

            //execute post request
            CloseableHttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            System.out.println("-------------------------");
            //print response status
            System.out.println("Status: " + response.getStatusLine());
            //print Server
            Header[] headers = response.getHeaders("Server");
            if (headers != null && headers.length > 0) {
                System.out.println("Server: " + response.getFirstHeader("Server").getValue());
            }

            if (entity != null) {
                //print response content length
                System.out.println("Response content length: " + entity.getContentLength());

                //print response content
                System.out.println("Response content: " + EntityUtils.toString(entity));
            }

            response.close();
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
