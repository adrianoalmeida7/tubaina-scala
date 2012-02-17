package com.ahalmeida.tubaina.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.TubainaException;
import br.com.caelum.tubaina.builder.replacer.Replacer;
import br.com.caelum.tubaina.builder.replacer.TodoReplacer;
import br.com.caelum.tubaina.chunk.TodoChunk;

public class TodoReplacerTest {
	
	private Replacer replacer;
	private List<Chunk> chunks;
	
	@Before
	public void setUp() {
		replacer = ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().todo());
		chunks = new ArrayList<Chunk>();
	}

	@Test
	public void testReplacesLowerCaseTodo() {
		String todo = "[todo something to do] ola resto";
		Assert.assertTrue(replacer.accepts(todo));
		String resto = replacer.execute(todo, chunks);
		Assert.assertEquals(" ola resto", resto);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(TodoChunk.class, chunks.get(0).getClass());
	}

	@Test
	public void testReplacesUpperCaseTodo() {
		String todo = "[TODO something to do2] ola resto";
		Assert.assertTrue(replacer.accepts(todo));
		String resto = replacer.execute(todo, chunks);
		Assert.assertEquals(" ola resto", resto);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(TodoChunk.class, chunks.get(0).getClass());
	}

	@Test(expected=TubainaException.class) 
	public void testReplacesTodoWithNoContent() {
		String todo = "[todo] ola resto";
		Assert.assertFalse(replacer.accepts(todo));
		replacer.execute(todo, chunks);
	}
}
