package com.astound.fragments;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;

import java.util.List;

public interface PageContext extends SearchContext, JavascriptExecutor {

    public String getName();

    public Fragment findFragment(By by);

    public List<Fragment> findFragments(By by);

    public <E extends Fragment> E findFragment(By by, Class<E> aClass);

    public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass);

    public <E extends Fragment> E findFragment(By by, Class<E> aClass, String name);

    public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass, String name);

}
