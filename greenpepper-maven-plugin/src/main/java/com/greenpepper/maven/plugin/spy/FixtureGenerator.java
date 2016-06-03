package com.greenpepper.maven.plugin.spy;

import java.io.File;

/**
 * Interface to implements if there is a need to generate some fixture template.
 */
public interface FixtureGenerator {

    enum ActionDone {
        CREATED,UPDATED,NONE;
    }

    class Result {
        ActionDone action;
        File fixtureFile;

        /**
         * @param action CREATED if the file were created from scratch. UPDATED if the file were updated. NONE if nothing were done.
         * @param fixtureFile the file holding the new fixture source code. This should not be null.
         */
        public Result(ActionDone action, File fixtureFile) {
            this.action = action;
            this.fixtureFile = fixtureFile;
        }

        public ActionDone getAction() {
            return action;
        }

        public File getFixtureFile() {
            return fixtureFile;
        }
    }

    /**
     * Generates a Fixture source code in a fixtureSourceDirectory directory.
     *
     * @param fixture The fixture information to help generating the source code.
     * @param systemUnderDevelopment additionnal information linked to the overall specification.
     * @param fixtureSourceDirectory the location were to generate sources or search for existing sources.
     * @return the result of the process.
     * @throws Exception in case of any issue.
     */
    Result generateFixture(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment, File fixtureSourceDirectory) throws Exception;
}
