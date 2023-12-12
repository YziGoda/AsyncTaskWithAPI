package com.example.asynctaskwithapiexample.parsers;

import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ComponentActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.example.asynctaskwithapiexample.MainActivity;
import com.example.asynctaskwithapiexample.R;

public class FloatRatesXmlParser
{
    public static String getCurrencyRatesBaseUsd(InputStream stream) throws IOException
    {
        String fieldText = MainActivity.editText.getText().toString();
        String result = new String();

        try
        {
            DocumentBuilderFactory xmlDocFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlDocBuilder = xmlDocFactory.newDocumentBuilder();
            Document doc = xmlDocBuilder.parse(stream);

            NodeList rateNodes = doc.getElementsByTagName("item");
            for (int i = 0; i < rateNodes.getLength(); ++i)
            {
                Element rateNode = (Element) rateNodes.item(i);
                String currencyName = rateNode.getElementsByTagName("targetCurrency").item(0).getFirstChild().getNodeValue();
                String rate = rateNode.getElementsByTagName("exchangeRate").item(0).getFirstChild().getNodeValue();
                if (!fieldText.isEmpty())
                {
                    if (fieldText.equals(currencyName)) {
                        result = (String.format("Currency name: %s, rate %s", currencyName, rate));
                        break;
                    }
                    else
                    {
                        System.out.println("No such exchange code");
                        result += (String.format("Currency name: %s, rate %s \n", currencyName, rate));
                    }
                }
                else
                {
                    result += (String.format("Currency name: %s, rate %s \n", currencyName, rate));
                }
            }
        }
        catch (ParserConfigurationException | SAXException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
