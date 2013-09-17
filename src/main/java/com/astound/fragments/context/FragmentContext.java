package com.astound.fragments.context;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/** Named page area which supports javascript execution, locating elements and fragments. */
public interface FragmentContext extends SearchContext, JavascriptExecutor {

    public String getName();

    public WebElement getRootElement();

    public Fragment findFragment(By by);

    public List<Fragment> findFragments(By by);

    public <E extends Fragment> E findFragment(By by, Class<E> aClass);

    public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass);

    public <E extends Fragment> E findFragment(By by, Class<E> aClass, String name);

    public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass, String name);

}
