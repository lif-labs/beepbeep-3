/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2015 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.eml.tuples;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.cep.interpreter.GrammarExtension;
import ca.uqac.lif.cep.interpreter.Interpreter.ParseException;
import ca.uqac.lif.cep.interpreter.InterpreterTestFrontEnd;

public class TuplesEmlBuildTest
{
	protected InterpreterTestFrontEnd m_interpreter;

	@Before
	public void setUp()
	{
		m_interpreter = new InterpreterTestFrontEnd();		
		GrammarExtension ext = new TupleGrammar();
		m_interpreter.extendGrammar(ext);
	}
	
	@Test
	public void testProcList1() throws ParseException
	{
		String expression = "0";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(1, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testProcList2() throws ParseException
	{
		String expression = "0, 1";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(2, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testProcList3() throws ParseException
	{
		String expression = "0 AS matrace";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(1, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testProcList3b() throws ParseException
	{
		String expression = "(0) AS matrace";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(1, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testProcList4() throws ParseException
	{
		String expression = "0 AS matrace, 1 AS matrace";
		Object result = m_interpreter.parseLanguage(expression, "<eml_proc_list>");
		assertTrue(result instanceof ProcessorDefinitionList);
		assertEquals(2, ((ProcessorDefinitionList) result).size());
	}
	
	@Test
	public void testAttributeExpression1() throws ParseException
	{
		String expression = "0";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_expr>");
		assertTrue(result instanceof AttributeExpression);
	}
	
	@Test
	public void testAttributeExpression2() throws ParseException
	{
		String expression = "0";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_expr>");
		assertTrue(result instanceof NumberExpression);
	}
	
	@Test
	public void testAttributeExpression3() throws ParseException
	{
		String expression = "(0) + (0)";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_expr>");
		assertTrue(result instanceof Addition);
	}
	
	@Test
	public void testAttributeExpression4() throws ParseException
	{
		String expression = "t.p";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_expr>");
		assertTrue(result instanceof AttributeNameQualified);
	}
	
	@Test
	public void testAttributeList1() throws ParseException
	{
		String expression = "t.p, t.p";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_list>");
		assertTrue(result instanceof AttributeList);
	}
	
	@Test
	public void testAttributeList2() throws ParseException
	{
		String expression = "t.p, t.p AS q";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_list>");
		assertTrue(result instanceof AttributeList);
	}
	
	@Test
	public void testAttributeList3() throws ParseException
	{
		String expression = "(t.p) + (2) AS w, t.p AS q";
		Object result = m_interpreter.parseLanguage(expression, "<eml_att_list>");
		assertTrue(result instanceof AttributeList);
	}
	
	@Test
	public void testSelect1() throws ParseException
	{
		String expression = "SELECT (t.p) + (2) AS w, t.p AS q FROM (0)";
		Object result = m_interpreter.parseLanguage(expression, "<eml_select>");
		assertTrue(result instanceof Select);
	}
}
