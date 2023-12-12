package com.example.asynctaskwithapiexample.utilities;

import com.example.asynctaskwithapiexample.parsers.FloatRatesXmlParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.asynctaskwithapiexample.utilities.Constants.FLOATRATES_API_URL;

public class ApiDataReader
{
    public static String getValuesFromApi(String apiCode) throws IOException
    {
        InputStream apiContentStream = null;
        String result = "";
        try
        {
            if (apiCode.equals(FLOATRATES_API_URL))
            {
                apiContentStream = downloadUrlContent(FLOATRATES_API_URL);
                result = FloatRatesXmlParser.getCurrencyRatesBaseUsd(apiContentStream);
            }
        }
        finally
        {
            if (apiContentStream != null)
            {
                apiContentStream.close();
            }
        }
        return result;
    }

    private static InputStream downloadUrlContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}
