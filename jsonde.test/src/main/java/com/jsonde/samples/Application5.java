package com.jsonde.samples;

public class Application5 {

    public Application5(String[] strings, int arg1, int arg2) {

    }

    public static void main(String... arguments) throws Exception {

        new Application5(null, 0, 0);
        new Application5(new String[]{"bla"}, 0, 0);
        new Application5(new String[]{null}, 0, 0);

    }

}