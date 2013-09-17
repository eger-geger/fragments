package com.astound.fragments.format.transformers;

public class Transformation {

    private final int start, end;

    private final String content;

    public Transformation(int start, int end, String content) {
        this.start = start;
        this.end = end;
        this.content = content;
    }

    /** @return position inside original pattern, transformation should be applied from */
    public int start() { return start; }

    /** @return position inside original, transformation should be applied to */
    public int end() { return end; }

    /** @return actual transformation */
    public String content() { return content; }

    /** @return shift in original pattern which will cause transformation application */
    public int shift() { return content.length() - (end - start); }
}
