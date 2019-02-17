package com.desrumaux.androidtoolbox.model.rooting;

/* This class was make by RootBeer. All credentials goes to them
    https://github.com/scottyab/rootbeer/blob/master/rootbeerlib/src/main/java/com/scottyab/rootbeer/Const.java
    The class describe all application which can have a dangerous potential in term of root escalation privileges
 */


public class Const {

    private Const() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static final String[] knownRootAppsPackages = {
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.topjohnwu.magisk"
    };

    public static final String[] knownDangerousAppsPackages = {
            "com.koushikdutta.rommanager",
            "com.koushikdutta.rommanager.license",
            "com.dimonvideo.luckypatcher",
            "com.chelpus.lackypatch",
            "com.ramdroid.appquarantine",
            "com.ramdroid.appquarantinepro",
            "com.android.vending.billing.InAppBillingService.COIN",
            "com.chelpus.luckypatcher"
    };

    public static final String[] knownRootCloakingPackages = {
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "de.robv.android.xposed.installer",
            "com.saurik.substrate",
            "com.zachspong.temprootremovejb",
            "com.amphoras.hidemyroot",
            "com.amphoras.hidemyrootadfree",
            "com.formyhm.hiderootPremium",
            "com.formyhm.hideroot"
    };

    public static final String[] suPaths ={
            "/data/local/",
            "/data/local/bin/",
            "/data/local/xbin/",
            "/sbin/",
            "/su/bin/",
            "/system/bin/",
            "/system/bin/.ext/",
            "/system/bin/failsafe/",
            "/system/sd/xbin/",
            "/system/usr/we-need-root/",
            "/system/xbin/",
            "/cache",
            "/data",
            "/dev"
    };


    public static final String[] pathsThatShouldNotBeWrtiable = {
            "/system",
            "/system/bin",
            "/system/sbin",
            "/system/xbin",
            "/vendor/bin",
            //"/sys",
            "/sbin",
            "/etc",
            //"/proc",
            //"/dev"
    };
}
