package com.astound.fragments.events;

import net.sf.cglib.proxy.Enhancer;

public class PublisherFactory {

	private final EventPublisher publisher;

	public PublisherFactory() {
		publisher = new EventPublisher();
	}

	public <T> T createPublishingInstance(Class<T> tClass, Class[] constructorSignature, Object... constructorArguments) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(tClass);
		enhancer.setCallback(publisher);

		return (T) enhancer.create(constructorSignature, constructorArguments);
	}

}
