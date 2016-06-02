package com.greenpepper.maven.plugin.spy;

/**
 * Interface to implements if there is a need to generate some fixture template.
 */
public interface FixtureGenerator {

    String generateFixture(SpyFixture fixture, SpySystemUnderDevelopment systemUnderDevelopment);
}
