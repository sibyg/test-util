package com.siby.automation.devices;

import io.selendroid.SelendroidConfiguration;
import io.selendroid.SelendroidLauncher;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AndroidDevice extends Devices {

    public static final String SCRIPT_LOCATION = "/src/main/resources/scripts/";
    public static String deviceSerialId;
    public static String currentFolder = System.getProperty("user.dir");
    private static SelendroidLauncher selendroidServer = null;

    public static void setUp() {
        deviceSerialId = getAndroidDevice();
        createShellScriptForAndroidDevice(deviceSerialId);
        startAndroidWebDriverApplication();
    }

    private static String getAndroidDevice() {
        ArrayList<String> devices = new ArrayList<String>();
        try {
            Process p = Runtime.getRuntime().exec(currentFolder + SCRIPT_LOCATION + "adb_devices.sh");
            BufferedReader br2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br2.readLine()) != null)
                if (line.contains("device") && !line.contains("List of") && !line.contains("adb")) {
                    String[] serialNo = line.split("\\s+");
                    devices.add(serialNo[0]);
                }
        } catch (Exception e) {
            //TODO handle exception properly
            e.printStackTrace();
        }
        if (devices.size() == 0) {
            throw new RuntimeException("No devices found");
        }
        String[] devicesArray = devices.toArray(new String[devices.size()]);
        return devicesArray[0];
    }

    private static void createShellScriptForAndroidDevice(String device) {
        String header = "#!/bin/sh \n\n#  start_android.sh";
        //TODO relative paths
        String cdToPlatformtools = "\ncd /Development/android_sdk/platform-tools";
        String startMainActivity = String.format("\n./adb -s %s shell am start -a android.intent.action.MAIN -n org.openqa.selenium.android.app/.MainActivity", device);
        String forwardTo8080 = String.format("\n./adb -s %s forward tcp:8080 tcp:8080", device);
        try {
            File file = new File(currentFolder + SCRIPT_LOCATION + "start_android.sh");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(header);
            output.write(cdToPlatformtools);
            output.write(startMainActivity);
            output.write(forwardTo8080);
            output.close();
        } catch (IOException e) {
            //TODO handle exception properly
            e.printStackTrace();
        }
    }

    private static void startAndroidWebDriverApplication() {
        List<String> lines = runScript(currentFolder + SCRIPT_LOCATION + "start_android.sh");

        for (String li : lines) {
            if (li.startsWith("Error: Activity class {org.openqa.selenium.android.app/org.openqa.selenium.android.app.MainActivity} does not exist.")) {
                createInstallServerScript(deviceSerialId);
                runInstallServerScript();
                lines = runScript(currentFolder + SCRIPT_LOCATION + "start_android.sh");
            }
        }
        //check Main is in process of starting or is started
        if (!listContainsStringThatStartsWith(lines, "Starting: Intent { act=android.intent.action.MAIN")) {
            throw new RuntimeException("Not Started");
        }
    }

    private static void createInstallServerScript(String device) {
        String header = "#!/bin/sh \n\n#  install_android.sh";
        //TODO relative paths
        String cdToPlatformtools = "\ncd /Development/android_sdk/platform-tools";
        String install = String.format("\n./adb -s %s -e install -r  android-server-2.32.0.apk", device);
        try {
            File file = new File(currentFolder + SCRIPT_LOCATION + "install_android.sh");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(header);
            output.write(cdToPlatformtools);
            output.write(install);
            output.close();
        } catch (IOException e) {
            //TODO handle exception properly
            e.printStackTrace();
        }
    }

    private static void runInstallServerScript() {
        List<String> lines = runScript(currentFolder + SCRIPT_LOCATION + "install_android.sh");
        if (!listContainsStringThatStartsWith(lines, "Success")) {
            throw new RuntimeException("Not Started");
        }
    }

    public static boolean listContainsStringThatStartsWith(List<String> list, String prefix) {
        for (String item : list) {
            if (item.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public static void launchSelendroid() {
        String androidHome = System.getenv("ANDROID_HOME");
        if (StringUtils.isEmpty(androidHome))
            throw new RuntimeException("Android home is not set");
        if (selendroidServer == null) {
            SelendroidConfiguration config = new SelendroidConfiguration();
            selendroidServer = new SelendroidLauncher(config);
            selendroidServer.lauchSelendroid();
        }

    }
}
