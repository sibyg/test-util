package com.siby.automation.devices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Devices {

    public static List<String> runScript(String script) {
        List<String> outputLines = new ArrayList<String>();
        try {
            Process process = Runtime.getRuntime().exec(script);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                outputLines.add(line);
            }
            br.close();
        } catch (IOException e) {
            //TODO handle exception properly
            e.printStackTrace();
        }
        return outputLines;
    }
}
