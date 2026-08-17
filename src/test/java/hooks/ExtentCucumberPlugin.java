package hooks;

import com.aventstack.extentreports.ExtentTest;
import framework.reports.ExtentManager;
import framework.reports.ExtentTestManager;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class ExtentCucumberPlugin implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
    }

    private void onTestCaseStarted(TestCaseStarted event) {
        String scenarioName = event.getTestCase().getName();
        ExtentTest test = ExtentManager.getExtentReports().createTest(scenarioName);

        // Tags as categories
        event.getTestCase().getTags().forEach(tag -> test.assignCategory(tag));

        ExtentTestManager.setTest(test);
    }

    private void onTestStepFinished(TestStepFinished event) {
        // Only log Gherkin steps (skip hooks)
        if (!(event.getTestStep() instanceof PickleStepTestStep step)) return;

        ExtentTest test = ExtentTestManager.getTest();
        if (test == null) return;

        String stepText = step.getStep().getKeyword() + step.getStep().getText();

        Status status = event.getResult().getStatus();
        Throwable error = event.getResult().getError();

        switch (status) {
            case PASSED -> test.pass(stepText);
            case SKIPPED -> test.skip(stepText);
            case PENDING, UNDEFINED, AMBIGUOUS, FAILED -> {
                if (error != null) test.fail(stepText).fail(error);
                else test.fail(stepText);
            }
            default -> test.info(stepText + " (" + status + ")");
        }
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        // Extent flush is handled in Hooks.afterSuite(), but safe to unload here
        ExtentTestManager.unload();
    }
}
