package com.src.Marqeta.Transact;

import com.src.Marqeta.Common.TestBase;
import com.src.Marqeta.utils.CommonUtilities;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;




public class Transact extends TestBase {

    public Transact() {
        super("Transact");
    }

    @Test(enabled = true, dataProvider = "regressionSet", groups = {"Regression"})
    public void executeRegressionTests(String testCaseId, String method, String endpoint, String headers,  String payloadData, String cacheResponsefields, String validations) {



        valueMap.put("testCaseId", testCaseId);
        valueMap.put("method", method);
        valueMap.put("endPoint", endpoint);
        valueMap.put("headers", headers);
        valueMap.put("payloadData", payloadData);
        valueMap.put("cacheResponsefields", cacheResponsefields);
        valueMap.put("validations", validations);
        validateRequest(testCaseId, REQUEST_TYPE.valueOf(method));


    }

    @DataProvider
    public Object[][] regressionSet() throws Exception {

        Object[][] testdata= CommonUtilities.getTableArray(System.getProperty("user.dir") + "/src/test/resources/Test/" + serviceName + ".xlsx", "Regression");
        return testdata;
    }

}
