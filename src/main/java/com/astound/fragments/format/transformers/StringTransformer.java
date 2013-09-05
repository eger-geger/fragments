package com.astound.fragments.format.transformers;

import com.astound.fragments.format.ToStringConverter;

import java.util.Collection;

public interface StringTransformer {

	public Collection<Transformation> transform(String rule, ToStringConverter converter);

}
