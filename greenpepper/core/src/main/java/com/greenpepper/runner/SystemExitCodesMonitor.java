package com.greenpepper.runner;


/**
 * Monitor the test to get a status code equals to the sum of wrong tests and the exceptions.
 * 
 * @author wattazoum
 *
 */
public class SystemExitCodesMonitor implements SpecificationRunnerMonitor {

    private int exitcode;

    /**
     * Not used.
     */
    @Override
    public void testRunning(String location) {
    }

    @Override
    public void testDone(int rightCount, int wrongCount, int exceptionCount, int ignoreCount) {
        exitcode = wrongCount + exceptionCount;
    }

    /**
     * Not used.
     */
    @Override
    public void exceptionOccured(Throwable t) {
    }

    /**
     * @return the exit code. 0 if everything went fine (no test failed or exception)
     */
    public int getExitcode() {
        return exitcode;
    }

}
