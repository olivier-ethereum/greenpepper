package com.greenpepper.confluence.utils;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;

public class HtmlUtilsTest extends TestCase {
	private String emptyTextArea = "";
	private SortedSet<String> elementSet = new TreeSet<String>();
	
	public void testEmptySetGivesEmptyTextArea() {
		assertEquals(emptyTextArea, HtmlUtils.stringSetToTextArea(new HashSet<String>()));
		assertEquals(emptyTextArea, HtmlUtils.stringSetToTextArea(null));
	}
	
	public void testOneElementInSetGivesOneElementInTextArea() {
		givenElement("foo");
		assertEquals("foo", HtmlUtils.stringSetToTextArea(elementSet));
	}
	
	public void testMultipleElementsInSetGivesElementsInTextAreaSeparatedByLF() {
		givenElement("foo");
		givenElement("bar");
		givenElement("toto");
		assertEquals("bar\nfoo\ntoto", HtmlUtils.stringSetToTextArea(elementSet));
	}

	private void givenElement(String element) {
		elementSet.add(element);
	}
}
