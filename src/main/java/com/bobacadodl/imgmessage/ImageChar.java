package com.bobacadodl.imgmessage;

/**
 * User: bobacadodl
 * Date: 1/25/14
 * Time: 11:03 PM
 */
public enum ImageChar {
    BLOCK('\u2588');
    private final char c;

    ImageChar(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }
}
