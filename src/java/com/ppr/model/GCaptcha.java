/*
 * Author: jianqing
 * Date: Aug 4, 2020
 * Description: This document is created for
 */
package com.ppr.model;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.IOException;

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

    public static GCaptcha verifyGCaptchaResult(String token, String secret) throws InterruptedException, IOException
    {
        String gResponse = Commander.executeCommand("curl -d response=" + token + "&secret=" + secret + " -X POST https://www.recaptcha.net/recaptcha/api/siteverify");
        JSONObject response = JSONUtil.parseObj(gResponse);

        return new GCaptcha(response.getBool("success"),
                response.getStr("challenge_ts"),
                response.getStr("hostname"),
                response.getStr("error_codes"),
                response.getDouble("score"));
    }

    @Override
    public String toString()
    {
        return "GCaptcha{" + "success=" + success + ", challenge_ts=" + challenge_ts + ", hostname=" + hostname + ", score=" + score + ", error_codes=" + error_codes + '}';
    }

    public static void main(String[] args)
    {

    }

}
