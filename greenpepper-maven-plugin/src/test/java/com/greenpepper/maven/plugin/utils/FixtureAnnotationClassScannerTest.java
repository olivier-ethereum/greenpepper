package com.greenpepper.maven.plugin.utils;

import com.google.common.base.Predicates;
import com.greenpepper.maven.plugin.schemas.FixtureType;
import com.greenpepper.maven.plugin.schemas.Fixtures;
import com.greenpepper.maven.plugin.schemas.TypeLanguage;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FixtureAnnotationClassScannerTest {
    @Test
    public void scanShouldNotFindAnything() throws Exception {
        FixtureAnnotationClassScanner fixtureScanner = new FixtureAnnotationClassScanner();
        Fixtures scanned = fixtureScanner.scan("my.fake.pakage");
        assertEquals(0, scanned.getFixture().size());
        assertEquals(TypeLanguage.JAVA, scanned.getLanguage());
    }

    @Test
    public void scanShouldFindOnlyBankFixtures() throws Exception {
        FixtureAnnotationClassScanner fixtureScanner = new FixtureAnnotationClassScanner();
        Fixtures scanned = fixtureScanner.scan("com.greenpepper.fixtures.application.bank");
        assertEquals("The result has 3 fixtures and 1 SubFixture", 4, scanned.getFixture().size());
    }

    @Test
    public void scanShouldFindOnlyMortgageFixtures() throws Exception {
        FixtureAnnotationClassScanner fixtureScanner = new FixtureAnnotationClassScanner();
        Fixtures scanned = fixtureScanner.scan("com.greenpepper.fixtures.application.mortgage");
        assertEquals(2, scanned.getFixture().size());
    }

    @Test
    public void scanShouldFindBothBankAndMortgageFixtures() throws Exception {
        FixtureAnnotationClassScanner fixtureScanner = new FixtureAnnotationClassScanner();
        Fixtures scanned = fixtureScanner.scan("com.greenpepper.fixtures.application");
        List<FixtureType> fixtures = scanned.getFixture();
        assertEquals("The result has 5 fixtures and 1 SubFixture", 6, fixtures.size());
    }
}