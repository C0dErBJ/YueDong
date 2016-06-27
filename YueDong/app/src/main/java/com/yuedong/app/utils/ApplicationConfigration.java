package com.yuedong.app.utils;

/**
 * Created by C0dEr on 16/1/17.
 */
public class ApplicationConfigration {

    private final static String Remote = "192.168.1.104";

    private final static String Port = "9090";

    private final static String Scheme = "http://";

    public static String getRemoteUrl() {
        return Scheme + Remote + ":" + Port + "/";
    }
}
