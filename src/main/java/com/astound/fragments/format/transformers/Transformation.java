package com.astound.fragments.format.transformers;

public class Transformation {

	private final int start, end;

	private final String content;

	public Transformation(int start, int end, String content) {
		this.start = start;
		this.end = end;
		this.content = content;
	}

	public int start() { return start; }

	public int end() { return end; }

	public String content() { return content; }

	public int shift() { return content.length() - (end - start); }
}
