package com.greenpepper.maven.plugin.spy;

import java.io.File;

/**
 * Interface to implements if there is a need to generate some fixture template.
 */
public interface FixtureGenerator {

    File generateFixture(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment, File fixtureSourceDirectory) throws Exception;
}
