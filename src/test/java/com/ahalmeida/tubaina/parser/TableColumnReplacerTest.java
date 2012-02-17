package com.ahalmeida.tubaina.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.TubainaException;
import br.com.caelum.tubaina.builder.replacer.Replacer;
import br.com.caelum.tubaina.builder.replacer.TableColumnReplacer;
import br.com.caelum.tubaina.chunk.TableColumnChunk;
import br.com.caelum.tubaina.resources.Resource;

public class TableColumnReplacerTest {
	private Replacer replacer;
	private List<Chunk> chunks;
	private List<Resource> resources;
	
	@Before
	public void setUp() {
		replacer = ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().col());
		chunks = new ArrayList<Chunk>();
		resources = new ArrayList<Resource>();
	}
	
	@Test
	public void testReplacesCorrectly() {
		String cell = "[col]some text[/col] text after";
		Assert.assertTrue(replacer.accepts(cell));
		String rest = replacer.execute(cell, chunks);
		Assert.assertEquals(" text after", rest);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(TableColumnChunk.class, chunks.get(0).getClass());
	}
	
	@Test
	public void testDoesntAcceptWithoutEndTag() {
		String cell = "[col]some text after";
		Assert.assertFalse(replacer.accepts(cell));
		try {
			replacer.execute(cell, chunks);
			Assert.fail("Should raise an exception");
		} catch (TubainaException e) {
			// ok
		}
		Assert.assertEquals(0, chunks.size());
	}
	
	@Test
	public void testDoesntAcceptWithoutBeginTag() {
		String cell = "some text before[/col]";
		Assert.assertFalse(replacer.accepts(cell));
		try {
			replacer.execute(cell, chunks);
			Assert.fail("Should raise an exception");
		} catch (TubainaException e) {
			// ok
		}
		Assert.assertEquals(0, chunks.size());
	}
}
