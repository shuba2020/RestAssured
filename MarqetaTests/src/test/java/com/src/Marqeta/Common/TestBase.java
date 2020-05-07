package com.src.Marqeta.Common;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class TestBase {

    protected String serviceName = null;
    public static HashMap<String,String> cacheMap = new HashMap<>();
    Response response;

    public TestBase(String serviceName) {

        this.serviceName = serviceName;
    }

    public enum REQUEST_TYPE {
        GET, POST, PUT, DELETE
    }

    protected static final HashMap<String, String> valueMap = new HashMap<String, String>();

    public void validateRequest(String testCaseId, REQUEST_TYPE type) {

        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification reqSpec = null;
        baseURI=valueMap.get("endPoint");
        String b = valueMap.get("payloadData");
        StringBuffer sb = new StringBuffer();


        //Substitute values for previously cached values such as tokens
        if (b.contains("<")){

            //Use the String between '<' and '>' as key to retrieve token value from cacheMap
            String patternString = "<(" + StringUtils.join(cacheMap.keySet(), "|") + ")>";
            //Replace the Token name with token value
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(b);
            while(matcher.find()) {
                matcher.appendReplacement(sb, cacheMap.get(matcher.group(1)));
            }
            matcher.appendTail(sb);

        }else{
            //If there are no cached values use the entire request body as such
            sb = sb.append(b);
        }


        response  = given().relaxedHTTPSValidation()
                .headers(resolveHeaders(valueMap.get("headers")))
                .body(sb.toString())
                .post(baseURI)
                .then().extract().response();

        String s =response.path("token");
        System.out.println(response.asString());

        String cache = valueMap.get("cacheResponsefields");

        //if any fields are needed from the response body - store in the cacheMap for use with the next APIs
        if (cache.length() > 0){
             cacheMap.put(testCaseId+"."+cache,s);
        }

        validateResponse(response);


    }

    private void validateResponse(Response r) {
        String[] validationPairs = valueMap.get("validations").split(";");
        System.out.println("valiedateion pari"+validationPairs[0]);
        if (valueMap.get("validations").contains("statusCode")){

            String v = validationPairs [0];
            String s = v.substring(v.lastIndexOf(',') + 1).trim();

            Assert.assertEquals(Integer.parseInt(s), response.getStatusCode());

            for (int i =1;i<=validationPairs.length-1;i++){
                System.out.println("Shuba pari"+validationPairs[i]);

                String [] l =validationPairs[i].split(",");

                JsonPath jsonPathEvaluator = r.jsonPath();

                String field = jsonPathEvaluator.get(l[0]);
                System.out.println("field is "+field);
                Assert.assertTrue(field.contains(l[1]));


            }

        }
    }


    public Map<String, Object> resolveHeaders(String r) {

        String[] headerPairs = r.split(";");
        Map<String,Object> m = new HashMap<>();

        for (int i = 0; i + 1 <= headerPairs.length; i++) {

            String[] singleHeader = headerPairs[i].split(",", 2);

            m.put(singleHeader[0].replace('\"', ' ').trim(), singleHeader[1].replace('\"', ' ').trim());


        }
        return m;

    }


}
