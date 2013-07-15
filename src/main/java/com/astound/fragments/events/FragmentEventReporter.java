package com.astound.fragments.events;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;

import java.util.Map;

public class FragmentEventReporter implements StepListener {


    private ThreadLocal<StringBuilder> threadLocalSB = new ThreadLocal<StringBuilder>() {
        protected StringBuilder initialValue() { return new StringBuilder(); }
    };

    private final StepEventBus stepEventBus;

    public FragmentEventReporter() {
        FragmentEventBus.getEventBus().register(this);
        stepEventBus = StepEventBus.getEventBus();
    }

    @Subscribe @AllowConcurrentEvents
    public void handleEvent(FragmentEvent event) {
        threadLocalSB.get()
                .append("<div>")
                .append("<span>").append(event.getFragmentName()).append("</span>")
                .append("<span>").append(event.getFragmentLocator()).append("</span>")
                .append("<span>").append(event.getEventDescription()).append("</span>")
                .append("</div>");

        stepEventBus.updateCurrentStepTitle(threadLocalSB.get().toString());
    }

    /**
     * Ignore
     *
     * @param storyClass
     */
    public void testSuiteStarted(Class<?> storyClass) {}

    /**
     * Ignore
     *
     * @param story
     */
    public void testSuiteStarted(Story story) {}

    /**
     * Ignore
     */
    public void testSuiteFinished() {}

    /**
     * Ignore
     *
     * @param description
     */
    public void testStarted(String description) {}

    /**
     * Ignore
     *
     * @param result
     */
    public void testFinished(TestOutcome result) {}

    public void stepStarted(ExecutedStepDescription description) {
        threadLocalSB.set(new StringBuilder(description.getTitle()));
    }

    /**
     * Ignore
     *
     * @param description
     */
    public void skippedStepStarted(ExecutedStepDescription description) {
        threadLocalSB.set(new StringBuilder(description.getTitle()));
    }

    /**
     * Ignore
     *
     * @param failure
     */
    public void stepFailed(StepFailure failure) {
        threadLocalSB.remove();
    }

    /**
     * Ignore
     *
     * @param failure
     */
    public void lastStepFailed(StepFailure failure) {
        threadLocalSB.remove();
    }

    /**
     * Ignore
     */
    public void stepIgnored() {
        threadLocalSB.remove();
    }

    /**
     * Ignore
     */
    public void stepPending() {
        threadLocalSB.remove();
    }

    /**
     * Ignore
     *
     * @param message
     */
    public void stepPending(String message) {
        threadLocalSB.remove();
    }

    /**
     * Ignore
     */
    public void stepFinished() {
        threadLocalSB.remove();
    }

    /**
     * Ignore
     *
     * @param testOutcome
     * @param cause
     */
    public void testFailed(TestOutcome testOutcome, Throwable cause) {}

    /**
     * Ignore
     */
    public void testIgnored() {}

    /**
     * Ignore
     */
    public void notifyScreenChange() {}

    /**
     * Ignore
     *
     * @param table
     */
    public void useExamplesFrom(DataTable table) {}

    /**
     * Ignore
     *
     * @param data
     */
    public void exampleStarted(Map<String, String> data) {}

    /**
     * Ignore
     */
    public void exampleFinished() {}

    /**
     * Ignore
     *
     * @param message
     */
    public void assumptionViolated(String message) {}
}
