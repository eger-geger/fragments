package com.astound.fragments.format;

import com.astound.fragments.format.transformers.StringTransformer;
import com.astound.fragments.format.transformers.Transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Formatter {

	private final ToStringConverter converter;

	public Formatter(ToStringConverter converter) {
		this.converter = converter;
	}

	public String format(String formattingString, StringTransformer... transformers) {
		return format(formattingString, Arrays.asList(transformers));
	}

	public String format(String formattingString, List<StringTransformer> transformers) {
		List<Transformation> transformations = new ArrayList<>();

		for (StringTransformer transformer : transformers) {
			transformations.addAll(transformer.transform(formattingString, converter));
		}

		StringBuilder stringBuilder = new StringBuilder(formattingString);

		int shift = 0;

		for (Transformation transformation : transformations) {
			stringBuilder.replace(transformation.start() + shift, transformation.end() + shift, transformation.content());
			shift += transformation.shift();
		}

		return stringBuilder.toString();
	}

}
