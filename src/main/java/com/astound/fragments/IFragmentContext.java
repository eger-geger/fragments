package com.astound.fragments;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.By;

import java.util.List;

public interface IFragmentContext {

    public String getName();

    public Fragment findFragment(By by);

    public List<Fragment> findFragments(By by);

    public <E extends Fragment<?>> E findFragment(By by, Class<E> type);

    public <E extends Fragment<?>> List<E> findFragments(By by, Class<E> type);

    public <E extends Fragment<?>> E findFragment(By by, Class<E> type, String name);

    public <E extends Fragment<?>> List<E> findFragments(By by, Class<E> type, String name);

}
