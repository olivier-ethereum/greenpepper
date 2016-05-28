package com.greenpepper.extensions.selenium;

import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * <p>Expression class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Expression {

	private String asString;

	/**
	 * <p>Constructor for Expression.</p>
	 *
	 * @param asString a {@link java.lang.String} object.
	 */
	public Expression(String asString) {
		super();
		this.asString = asString;
	}
	
    /**
     * <p>parseVariableName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String parseVariableName() {
    	if(asString == null)
    		return null;
    	
    	String var = asString.trim();
    	if(!var.startsWith("$"))
    		throw new IllegalStateException("This expression is not a variable reference");
    	
    	Scanner scanner = new Scanner(var.substring(1));
    	scanner.findInLine("\\{\\s*(\\w+)\\s*\\}");
    	MatchResult matchResults = scanner.match();
    	if(matchResults.groupCount() != 1)
    		throw new IllegalStateException("Malformed variable name expression.");
    		
		return matchResults.group(1);
	}
	
}
