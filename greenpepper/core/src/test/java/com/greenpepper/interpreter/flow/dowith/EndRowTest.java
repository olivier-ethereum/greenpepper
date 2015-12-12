package com.greenpepper.interpreter.flow.dowith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.greenpepper.Statistics;
import com.greenpepper.annotation.Annotation;
import com.greenpepper.annotation.ExceptionAnnotation;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.util.Rows;

public class EndRowTest {

    private EndRow endRow;
    
    @Test
    public void testCorrectParsing(){
        endRow = new EndRow(null);
        Rows rows = Rows.parse("[end]");
        
        FakeSpecification table = new FakeSpecification(rows );
        endRow.interpret(table );
        
        Statistics stats = table.stats();
        assertEquals(0, stats.totalCount());
    }
    
    @Test
    public void testTooManyArguments(){
        endRow = new EndRow(null);
        Rows rows = Rows.parse("[end][much more]");
        
        FakeSpecification table = new FakeSpecification(rows );
        endRow.interpret(table );
        
        Statistics stats = table.stats();
        assertEquals(1, stats.totalCount());
        assertEquals(1, stats.exceptionCount());
        Annotation annotation = rows.parts.annotation;
        assertNotNull(annotation);
        assertTrue(annotation instanceof ExceptionAnnotation);
        assertEquals("Wrong Syntax for [end]: This keyword doesn't take any argument", ((ExceptionAnnotation)annotation).getExceptionMessage());
    }
    
    @Test
    public void testLineAfterEndInTheSameTable(){
        endRow = new EndRow(null);
        Rows rows = Rows.parse("[end]\n"
                + "[check][my name]");
        
        FakeSpecification table = new FakeSpecification(rows );
        endRow.interpret(table );
        
        Statistics stats = table.stats();
        assertEquals(1, stats.totalCount());
        assertEquals(1, stats.exceptionCount());
        Annotation annotation = rows.parts.annotation;
        assertNotNull(annotation);
        assertTrue(annotation instanceof ExceptionAnnotation);
        assertEquals("Wrong Syntax for [end]: This keyword should end the table", ((ExceptionAnnotation)annotation).getExceptionMessage());
    }
}
