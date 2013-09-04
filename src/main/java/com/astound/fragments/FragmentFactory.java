package com.astound.fragments;

import com.astound.fragments.elements.Fragment;
import com.astound.fragments.locators.FragmentLocatorFactory;
import com.astound.fragments.proxy.ElementLoader;
import com.astound.fragments.proxy.EventPublisher;
import com.astound.fragments.proxy.ListLoader;
import net.sf.cglib.proxy.Enhancer;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.List;

import static java.lang.reflect.Proxy.newProxyInstance;

public class FragmentFactory {

    private static final Class[] FRAGMENT_LIST_INTERFACES = new Class[]{List.class};

    private static final Class[] FRAGMENT_INTERFACES = new Class[]{WebElement.class, WrapsElement.class};

    private final ClassLoader classLoader;

    private final JavascriptExecutor jsExecutor;

    public FragmentFactory(JavascriptExecutor jsExecutor) {
        classLoader = getClass().getClassLoader();
        this.jsExecutor = jsExecutor;
    }

    public <F extends Fragment> F createFragment(Class<F> aClass, ElementLocator locator, String name) {
        F fragment = createFragment(aClass, new PageContextSupport(createWebElementProxy(locator), jsExecutor, this, name));

        initFragmentsIn(fragment);

	    return fragment;
//        return wrapWithEventPublishingProxy(fragment);
    }

    public <F extends Fragment> List<F> createList(Class<F> aClass, ElementLocator locator, String name) {
        return (List<F>) newProxyInstance(classLoader, FRAGMENT_LIST_INTERFACES, new ListLoader<>(aClass, locator, this, name));
    }

    private WebElement createWebElementProxy(ElementLocator locator) {
        return (WebElement) newProxyInstance(classLoader, FRAGMENT_INTERFACES, new ElementLoader(locator));
    }

    public <Context extends PageContext> void initFragmentsIn(Context context) {
        initFragmentsIn(context, new FragmentDecorator(new FragmentLocatorFactory(context), this));
    }

    public <Context extends PageContext> void initFragmentsIn(Context context, FragmentDecorator decorator) {
        Class aClass = context.getClass();

        while (isAssignableContext(aClass)) {
            for (Field field : aClass.getDeclaredFields()) {
                assignContextField(context, field, decorator.decorate(classLoader, field));
            }
            aClass = aClass.getSuperclass();
        }
    }

	private static boolean isAssignableContext(Class aClass){
		return PageContext.class.isAssignableFrom(aClass) && !aClass.equals(Fragment.class);
	}

    private void assignContextField(Object context, Field field, Object value) {
        if(value != null){
	        try {
		        field.setAccessible(true);
		        field.set(context, value);
	        } catch (ReflectiveOperationException ex) {

	        }
        }
    }

//    private <F extends Fragment> F wrapWithEventPublishingProxy(F fragment) {
//	    Enhancer enhancer = new Enhancer();
//	    enhancer.setSuperclass(fragment.getClass());
//	    enhancer.setCallback(new EventPublisher(fragment));
//
//	    return (F) enhancer.create(new Class[]{PageContext.class}, new Object[]{fragment});
//    }

    private static <T extends Fragment> T createFragment(Class<T> aClass, PageContext pageContext) {
	    Enhancer enhancer = new Enhancer();
	    enhancer.setSuperclass(aClass);
	    enhancer.setCallback(new EventPublisher());

	    return (T) enhancer.create(new Class[]{PageContext.class}, new Object[]{pageContext});
//
//	    try {
//            return aClass.getConstructor(PageContext.class).newInstance(pageContext);
//        } catch (Exception ex) {
//            throw new IllegalArgumentException(String.format("Failed to create [%s]", aClass), ex);
//        }
    }
}