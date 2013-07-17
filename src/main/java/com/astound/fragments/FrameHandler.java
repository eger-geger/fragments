package com.astound.fragments;

import com.astound.fragments.elements.Fragment;

import static com.astound.fragments.utils.PageUtils.getParentPage;

public abstract class FrameHandler<T extends Fragment> {

    protected abstract void doPerform(T frame);

    public void performAction(T frame) {
        PageWithFragments page = getParentPage(frame);

        page.switchToFrame(frame);

        doPerform(frame);

        page.switchToPage();
    }
}
