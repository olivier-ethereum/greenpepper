package com.greenpepper.extensions.selenium;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * <p>SeleniumRemoteControlFixture class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SeleniumRemoteControlFixture
{
	private static final String DEFAULT_WAIT_TIMEOUT = "30000";
	
    private Selenium selenium;
    
    private Map<String, String> variables = new HashMap<String, String>();
    
    /**
     * <p>Constructor for SeleniumRemoteControlFixture.</p>
     */
    public SeleniumRemoteControlFixture()
    {
    }

    /**
     * <p>Constructor for SeleniumRemoteControlFixture.</p>
     *
     * @param selenium a {@link com.thoughtworks.selenium.Selenium} object.
     */
    public SeleniumRemoteControlFixture(Selenium selenium)
    {
        this.selenium = selenium;
    }

    /**
     * <p>getSystemUnderTest.</p>
     *
     * @return a {@link com.thoughtworks.selenium.Selenium} object.
     */
    @com.greenpepper.reflect.SystemUnderTest
    public Selenium getSystemUnderTest()
    {
        return selenium;
    }
    
    /**
     * <p>startBrowserWithSeleniumConsoleOnAtPortAndScriptsAt.</p>
     *
     * @param browserName a {@link java.lang.String} object.
     * @param serverHost a {@link java.lang.String} object.
     * @param serverPort a int.
     * @param browserUrl a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean startBrowserWithSeleniumConsoleOnAtPortAndScriptsAt(String browserName, String serverHost, int serverPort, String browserUrl)
	{
        selenium = new DefaultSelenium(serverHost, serverPort, "*" + browserName, browserUrl);
        selenium.start();
        return true;
	}
    
    /**
     * <p>userOpensUrl.</p>
     *
     * @param url a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean userOpensUrl(String url)
    {
    	selenium.open(url);
    	return true;
    }
    
    /**
     * <p>shutdownBrowser.</p>
     *
     * @return a boolean.
     */
    public boolean shutdownBrowser()
    {
    	selenium.stop();
    	return true;
    }
    
    /**
     * <p>text.</p>
     *
     * @param locator a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String text(String locator)
    {
    	return selenium.getText(locator);
    }
    
    /**
     * <p>value.</p>
     *
     * @param locator a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String value(String locator)
    {
    	return selenium.getValue(locator);
    }

    
    // =====================================
    // Selenium commands
    // =====================================
    
    
    /**
     * <p>assertAlert.</p>
     *
     * @param alert a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean assertAlert(String alert)
    {
    	return selenium.getAlert().equals(alert);
    }
    
    /**
     * <p>assertAlertPresent.</p>
     *
     * @return a boolean.
     */
    public boolean assertAlertPresent()
    {
    	return selenium.isAlertPresent();
    }
    
    /**
     * <p>assertAlertNotPresent.</p>
     *
     * @return a boolean.
     */
    public boolean assertAlertNotPresent()
    {
    	return !assertAlertPresent();
    }

    /**
     * <p>assertElementPresent.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean assertElementPresent (String text)
    {
        return selenium.isElementPresent(text);
    }

    /**
     * <p>assertElementNotPresent.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean assertElementNotPresent (String text)
    {
        return !assertElementPresent(text);
    }

    /**
     * <p>assertExpression.</p>
     *
     * @param variableNameExpression a {@link java.lang.String} object.
     * @param expectedValue a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean assertExpression(String variableNameExpression, String expectedValue)
    {
    	Expression expression = new Expression(variableNameExpression);
    	return expectedValue.equals(this.variables.get(expression.parseVariableName()));
    }
    
    /**
     * <p>assertFailureOnNext.</p>
     */
    public void assertFailureOnNext()
    {
    	
    }
    
	/**
	 * <p>assertTextPresent.</p>
	 *
	 * @param text a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean assertTextPresent (String text)
    {
        return selenium.isTextPresent(text);
    }

    /**
     * <p>assertTextNotPresent.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean assertTextNotPresent (String text)
    {
        return !assertTextPresent(text);
    }

    /**
     * <p>assertTitle.</p>
     *
     * @param title a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean assertTitle (String title)
    {
        return title.equals(selenium.getTitle());
    }

    /**
     * <p>assertNotTitle.</p>
     *
     * @param title a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean assertNotTitle(String title)
    {
    	return !assertTitle(title);
    }
    
    /**
     * <p>clickAndWait.</p>
     *
     * @param element a {@link java.lang.String} object.
     */
    public void clickAndWait (String element)
	{
	    selenium.click(element);
	    selenium.waitForPageToLoad(DEFAULT_WAIT_TIMEOUT);
	}
    
    /**
     * <p>storeAlert.</p>
     *
     * @param variableName a {@link java.lang.String} object.
     */
    public void storeAlert(String variableName)
    {
    	this.variables.put(variableName, selenium.getAlert());
    }
    
    /**
     * <p>verifyAlert.</p>
     *
     * @param alert a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean verifyAlert(String alert)
    {
    	return assertAlert(alert);
    }
    
    /**
     * <p>verifyAlertPresent.</p>
     *
     * @return a boolean.
     */
    public boolean verifyAlertPresent()
    {
    	return assertAlertPresent();
    }
    
    /**
     * <p>verifyAlertNotPresent.</p>
     *
     * @return a boolean.
     */
    public boolean verifyAlertNotPresent()
    {
    	return !verifyAlertPresent();
    }

    /**
     * <p>verifyElementPresent.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean verifyElementPresent (String text)
    {
        return assertElementPresent(text);
    }

    /**
     * <p>verifyElementNotPresent.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean verifyElementNotPresent (String text)
    {
        return !verifyElementPresent(text);
    }

    /**
     * <p>verifyExpression.</p>
     *
     * @param expression a {@link java.lang.String} object.
     * @param expectedValue a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean verifyExpression(String expression, String expectedValue)
    {
    	return assertExpression(expression, expectedValue);
    }

    /**
     * <p>verifyTextPresent.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean verifyTextPresent (String text)
    {
        return assertTextPresent(text);
    }

    /**
     * <p>verifyTextNotPresent.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean verifyTextNotPresent (String text)
    {
        return !verifyTextPresent(text);
    }
    
    /**
     * <p>verifyTitle.</p>
     *
     * @param title a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean verifyTitle(String title)
    {
    	return assertTitle(title);
    }
    
    /**
     * <p>verifyNotTitle.</p>
     *
     * @param title a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean verifyNotTitle(String title)
    {
    	return !verifyTitle(title);
    }
    
    /**
     * <p>waitForAlertPresent.</p>
     */
    public void waitForAlertPresent()
    {
    	selenium.waitForCondition("selenium.browserbot.hasAlerts()", DEFAULT_WAIT_TIMEOUT);
    }

}
