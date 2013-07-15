package com.astound.fragments.utils;

import com.astound.fragments.IFragmentContext;
import com.astound.fragments.PageWithFragments;
import com.astound.fragments.elements.Fragment;

public class PageUtils {

    public static PageWithFragments getParentPage(IFragmentContext context) {
        if (context instanceof PageWithFragments) {
            return (PageWithFragments) context;
        }

        if (context instanceof Fragment) {
            return getParentPage(((Fragment<?>) context).getParent());
        }

        throw new IllegalArgumentException("Nor page fragment nor page.");
    }

}
