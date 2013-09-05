package com.astound.fragments.format.transformers;

import com.astound.fragments.format.ToStringConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexpTransformer implements StringTransformer {

	private final Pattern regexp;

	public RegexpTransformer(Pattern regexp) {
		this.regexp = regexp;
	}

	@Override final public Collection<Transformation> transform(String rule, ToStringConverter converter) {
		Matcher matcher = regexp.matcher(rule);

		List<Transformation> transformations = new ArrayList<>();

		while (matcher.find()) {
			transformations.add(new Transformation(matcher.start(), matcher.end(), transformMatch(new Match(matcher), converter)));
		}

		return transformations;
	}

	protected abstract String transformMatch(Match match, ToStringConverter converter);

	protected static class Match {

		private final Matcher matcher;

		public Match(Matcher matcher) {
			this.matcher = matcher;
		}

		public Pattern pattern() {return matcher.pattern();}

		public int start() {return matcher.start();}

		public int end() {return matcher.end();}

		public String group() {return matcher.group();}

		public String group(int group) {return matcher.group(group);}

		public String group(String name) {return matcher.group(name);}

		public int groupCount() {return matcher.groupCount();}

		public int regionStart() {return matcher.regionStart();}

		public int regionEnd() {return matcher.regionEnd();}

		public boolean hasTransparentBounds() {return matcher.hasTransparentBounds();}

		public boolean hasAnchoringBounds() {return matcher.hasAnchoringBounds();}

		public boolean hitEnd() {return matcher.hitEnd();}

		public boolean requireEnd() {return matcher.requireEnd();}
	}
}
