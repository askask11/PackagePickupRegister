/*
 * Author: jianqing
 * Date: Aug 4, 2020
 * Description: This document is created for
 */
package com.ppr.model;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.faces.convert.DateTimeConverter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jianqing
 */
public class GCaptcha
{
    private boolean success;
    private String challenge_ts;
    private String hostname;
    
    private Double score;
    
    private String error_codes;

    public GCaptcha(boolean success, String challenge_ts, String hostname, String error_codes, Double score)
    {
        this.success = success;
        this.challenge_ts = challenge_ts;
        this.hostname = hostname;
        this.error_codes = error_codes;
        this.score = score;
    }

    public GCaptcha()
    {
         this.success = false;
        this.challenge_ts = null;
        this.hostname = "";
        this.error_codes = "";
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getChallenge_ts()
    {
        return challenge_ts;
    }

    public void setChallenge_ts(String challenge_ts)
    {
        this.challenge_ts = challenge_ts;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public Double getScore()
    {
        return score;
    }

    public void setScore(Double score)
    {
        this.score = score;
    }

    public String getError_codes()
    {
        return error_codes;
    }

    public void setError_codes(String error_codes)
    {
        this.error_codes = error_codes;
    }
    
    public static GCaptcha verifyGCaptchaResult(String token, String secret) throws InterruptedException, IOException, ParseException
    {
        String gResponse = Commander.executeCommand("curl -d response="+token+"&secret="+secret+" -X POST https://www.recaptcha.net/recaptcha/api/siteverify");
        JSONParser parser = new JSONParser();
        //System.out.println(gResponse);
        JSONObject response = (JSONObject)parser.parse(gResponse);
        
        return new GCaptcha((Boolean)response.get("success"),
                (String)response.get("challenge_ts"),
                (String)response.get("hostname"),
                (String)response.get("error_codes"),
            (Double)response.get("score"));
        
    }

    @Override
    public String toString()
    {
        return "GCaptcha{" + "success=" + success + ", challenge_ts=" + challenge_ts + ", hostname=" + hostname + ", score=" + score + ", error_codes=" + error_codes + '}';
    }

    

    public static void main(String[] args) throws InterruptedException, InterruptedException, IOException, ParseException
    {
        System.out.println(verifyGCaptchaResult(""
                +"03AGdBq25ol13dLxKIScG9KwHKQO8mgwMuFQplPJM_6tqVXR3-SIH_ns4WEkcxEtNSPY234TwOGWpxQ7WecdPkRBBQlJq7oAxX_GIUWm8n46i8Rf_38M_KhyZBI_NpEo3YzzLDfqZIk1TOvLxU2EjX_YxL4btgWrdS2Fb2IRE1rer89f_K4xwVJ9Khvz0eXBDa-anz4yaXV8y3W-j4QKOyuMayyrFojHVPDnwHsCx_bM4rv6QaH1gv4T4KIyEH3Ge01RqGil9rOxdYVfyA32GvIImI9gvXH_7ObkHEmjsT9UDutLoUCT8nTLmoh8_VmoKmPXQevU-pURluCu3MfaWnA_kG95bXyiIennDeRugq-D6J9UqRmNVYgfxTCTjI3WtYqu0Hojrv0DLM"
                + "", ""
                        + "6LdSILoZAAAAAE_UL3FASfKZ6UTGGWgLTodbPX58"
                        + ""));
    }
    
    
    
    
}
