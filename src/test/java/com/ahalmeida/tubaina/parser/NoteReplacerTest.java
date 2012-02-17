package com.ahalmeida.tubaina.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.TubainaException;
import br.com.caelum.tubaina.builder.replacer.NoteReplacer;
import br.com.caelum.tubaina.builder.replacer.Replacer;
import br.com.caelum.tubaina.chunk.NoteChunk;
import br.com.caelum.tubaina.resources.Resource;

public class NoteReplacerTest {

	private Replacer replacer;
	private List<Chunk> chunks;
	private List<Resource> resources;

	@Before
	public void setUp() {
		replacer = ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().note());
		chunks = new ArrayList<Chunk>();
	}

	@Test
	public void testReplacesCorrectAnswer() {
		String note = "[note]ola mundo[/note] ola resto";
		Assert.assertTrue(replacer.accepts(note));
		String resto = replacer.execute(note, chunks);
		Assert.assertEquals(" ola resto", resto);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(NoteChunk.class, chunks.get(0).getClass());
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutEndTag() {
		String note = "[note]ola mundo ola resto";
		Assert.assertFalse(replacer.accepts(note));
		replacer.execute(note, chunks);
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutBeginTag() {
		String note = "ola mundo[/note] ola resto";
		Assert.assertFalse(replacer.accepts(note));
		replacer.execute(note, chunks);
	}
}
