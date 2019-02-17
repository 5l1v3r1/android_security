package com.desrumaux.androidtoolbox.model.rooting;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.*;
import java.util.*;

public class BasicVerif {
    private final Context mContext;
    public String TAG = "BasicVerif";

    public BasicVerif(Context context){
        mContext = context;
    }

    /**
     * Run all the checks.
     * To run the same check but without looking for the busybox binary to avoid a false positive for certain devices please
     * see {@link #isRootedWithoutBusyBoxCheck() isRootedWithoutBusyBoxCheck}
     *
     * @return true, we think there's a good *indication* of root | false good *indication* of no root (could still be cloaked)
     */
    public boolean isRooted() {

        return detectRootManagementApps() || detectPotentiallyDangerousApps() || checkForSuBinary()
                || checkForBinary("busybox") || checkForDangerousProps() || checkForRWPaths()
                || detectTestKeys() || checkSuExists() || checkForMagiskBinary();
    }

    /**
     * Run all the checks apart from checking for the busybox binary. This is because it can sometimes be a false positive
     * as some manufacturers leave the binary in production builds.
     * @return true, we think there's a good *indication* of root | false good *indication* of no root (could still be cloaked)
     */
    public boolean isRootedWithoutBusyBoxCheck() {

        return detectRootManagementApps() || detectPotentiallyDangerousApps() || checkForSuBinary()
                || checkForDangerousProps() || checkForRWPaths()
                || detectTestKeys() || checkSuExists() || checkForMagiskBinary();
    }



    public boolean detectTestKeys() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }



    public boolean detectRootManagementApps() {
        return detectRootManagementApps(null);
    }

    /**
     * Using the PackageManager, check for a list of well known root apps. @link {Const.knownRootAppsPackages}
     * @param additionalRootManagementApps - array of additional packagenames to search for
     * @return true if one of the apps it's installed
     */
    public boolean detectRootManagementApps(String[] additionalRootManagementApps) {

        // Create a list of package names to iterate over from constants any others provided
        ArrayList<String> packages = new ArrayList<>();
        packages.addAll(Arrays.asList(Const.knownRootAppsPackages));
        if (additionalRootManagementApps!=null && additionalRootManagementApps.length>0){
            packages.addAll(Arrays.asList(additionalRootManagementApps));
        }

        return isAnyPackageFromListInstalled(packages);
    }

    public boolean detectPotentiallyDangerousApps() {
        return detectPotentiallyDangerousApps(null);
    }

    /**
     * Using the PackageManager, check for a list of well known apps that require root. @link {Const.knownRootAppsPackages}
     * @param additionalDangerousApps - array of additional packagenames to search for
     * @return true if one of the apps it's installed
     */
    public boolean detectPotentiallyDangerousApps(String[] additionalDangerousApps) {

        // Create a list of package names to iterate over from constants any others provided
        ArrayList<String> packages = new ArrayList<>();
        packages.addAll(Arrays.asList(Const.knownDangerousAppsPackages));
        if (additionalDangerousApps!=null && additionalDangerousApps.length>0){
            packages.addAll(Arrays.asList(additionalDangerousApps));
        }

        return isAnyPackageFromListInstalled(packages);
    }

    /**
     * Using the PackageManager, check for a list of well known root cloak apps. @link {Const.knownRootAppsPackages}
     * @param additionalRootCloakingApps - array of additional packagenames to search for
     * @return true if one of the apps it's installed
     */
    public boolean detectRootCloakingApps(String[] additionalRootCloakingApps) {

        // Create a list of package names to iterate over from constants any others provided
        ArrayList<String> packages = new ArrayList<>();
        packages.addAll(Arrays.asList(Const.knownRootCloakingPackages));
        if (additionalRootCloakingApps!=null && additionalRootCloakingApps.length>0){
            packages.addAll(Arrays.asList(additionalRootCloakingApps));
        }
        return isAnyPackageFromListInstalled(packages);
    }

    public boolean checkForSuBinary(){
        return checkForBinary("su");
    }

    /**
     * Checks various (Const.suPaths) common locations for the magisk binary (a well know root level program)
     * @return true if found
     */
    public boolean checkForMagiskBinary(){ return checkForBinary("magisk"); }

    /**
     * Checks various (Const.suPaths) common locations for the busybox binary (a well know root level program)
     * @return true if found
     */
    public boolean checkForBusyBoxBinary(){
        return checkForBinary("busybox");
    }

    public boolean checkForBinary(String filename) {

        String[] pathsArray = Const.suPaths;

        boolean result = false;

        for (String path : pathsArray) {
            String completePath = path + filename;
            File f = new File(path, filename);
            boolean fileExists = f.exists();
            if (fileExists) {
                result = true;
            }
        }

        return result;
    }


    private String[] propsReader() {
        try {
            InputStream inputstream = Runtime.getRuntime().exec("getprop").getInputStream();
            if (inputstream == null) return null;
            String propVal = new Scanner(inputstream).useDelimiter("\\A").next();
            return propVal.split("\n");
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] mountReader() {
        try {
            InputStream inputstream = Runtime.getRuntime().exec("mount").getInputStream();
            if (inputstream == null) return null;
            String propVal = new Scanner(inputstream).useDelimiter("\\A").next();
            return propVal.split("\n");
        } catch (IOException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if any package in the list is installed
     * @param packages - list of packages to search for
     * @return true if any of the packages are installed
     */
    private boolean isAnyPackageFromListInstalled(List<String> packages){
        boolean result = false;

        PackageManager pm = mContext.getPackageManager();

        for (String packageName : packages) {
            try {
                // Root app detected
                pm.getPackageInfo(packageName, 0);
                Log.i(TAG, packageName + " ROOT management app detected!");
                result = true;
            } catch (PackageManager.NameNotFoundException e) {
                // Exception thrown, package is not installed into the system
            }
        }

        return result;
    }

    /**
     * Checks for several system properties for
     * @return - true if dangerous props are found
     */
    public boolean checkForDangerousProps() {

        final Map<String, String> dangerousProps = new HashMap<>();
        dangerousProps.put("ro.debuggable", "1");
        dangerousProps.put("ro.secure", "0");

        boolean result = false;

        String[] lines = propsReader();

        if (lines == null){
            // Could not read, assume false;
            return result;
        }

        for (String line : lines) {
            for (String key : dangerousProps.keySet()) {
                if (line.contains(key)) {
                    String badValue = dangerousProps.get(key);
                    badValue = "[" + badValue + "]";
                    if (line.contains(badValue)) {
                        Log.i(TAG, key + " = " + badValue + " detected!");
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * When you're root you can change the permissions on common system directories, this method checks if any of these patha Const.pathsThatShouldNotBeWrtiable are writable.
     * @return true if one of the dir is writable
     */
    public boolean checkForRWPaths() {

        boolean result = false;

        String[] lines = mountReader();

        if (lines == null){
            // Could not read, assume false;
            return result;
        }

        for (String line : lines) {

            // Split lines into parts
            String[] args = line.split(" ");

            if (args.length < 4){
                // If we don't have enough options per line, skip this and log an error
                Log.i(TAG,"Error formatting mount line: "+line);
                continue;
            }

            String mountPoint = args[1];
            String mountOptions = args[3];

            for(String pathToCheck: Const.pathsThatShouldNotBeWrtiable) {
                if (mountPoint.equalsIgnoreCase(pathToCheck)) {

                    // Split options out and compare against "rw" to avoid false positives
                    for (String option : mountOptions.split(",")){

                        if (option.equalsIgnoreCase("rw")){
                            Log.i(TAG, pathToCheck+" path is mounted with rw permissions! "+line);
                            result = true;
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }


    /**
     * A variation on the checking for SU, this attempts a 'which su'
     * @return true if su found
     */
    public boolean checkSuExists() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }


}
