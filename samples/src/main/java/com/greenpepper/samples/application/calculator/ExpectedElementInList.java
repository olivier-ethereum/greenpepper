package com.greenpepper.samples.application.calculator;

/**
 * <p>ExpectedElementInList class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class ExpectedElementInList {
	   private String expectedElement;
	   
	   /**
	    * <p>Constructor for ExpectedElementInList.</p>
	    *
	    * @param element a {@link java.lang.String} object.
	    */
	   public ExpectedElementInList(String element) {
		   expectedElement = element;
	   }

	   /**
	    * <p>Getter for the field <code>expectedElement</code>.</p>
	    *
	    * @return a {@link java.lang.String} object.
	    */
	   public String getExpectedElement() {
	      return expectedElement;
	   }
	   
	   
	   
	   /**
	    * <p>parse.</p>
	    *
	    * @param val a {@link java.lang.String} object.
	    * @return a {@link com.greenpepper.samples.application.calculator.ExpectedElementInList} object.
	    */
	   public static ExpectedElementInList parse(String val) {
		   return new ExpectedElementInList(val);
	   } 

		/**
		 * <p>toString.</p>
		 *
		 * @param value a {@link com.greenpepper.samples.application.calculator.ExpectedElementInList} object.
		 * @return a {@link java.lang.String} object.
		 */
		public static String toString(ExpectedElementInList value) {
			return value.getExpectedElement();
		}
}
