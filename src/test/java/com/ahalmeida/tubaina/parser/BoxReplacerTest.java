package com.ahalmeida.tubaina.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.TubainaException;
import br.com.caelum.tubaina.builder.replacer.BoxReplacer;
import br.com.caelum.tubaina.builder.replacer.Replacer;
import br.com.caelum.tubaina.chunk.BoxChunk;
import br.com.caelum.tubaina.resources.Resource;

public class BoxReplacerTest {

	private Replacer replacer;
	private List<Chunk> chunks;

	@Before
	public void setUp() {
		replacer = ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().box());
		chunks = new ArrayList<Chunk>();
	}

	@Test
	public void testReplacesCorrectBox() {
		String box = "[box boxName]ola mundo[/box] ola resto";
		Assert.assertTrue(replacer.accepts(box));
		String resto = replacer.execute(box, chunks);
		Assert.assertEquals(" ola resto", resto);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(BoxChunk.class, chunks.get(0).getClass());
	}

	@Test(expected = TubainaException.class)
	public void testReplacesBoxWithNoTitle() {
		String box = "[box]ola mundo[/box] ola resto";
		Assert.assertFalse(replacer.accepts(box));
		replacer.execute(box, chunks);
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutEndTag() {
		String box = "[box]ola mundo ola resto";
		Assert.assertFalse(replacer.accepts(box));
		replacer.execute(box, chunks);
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutBeginTag() {
		String box = "ola mundo[/box] ola resto";
		Assert.assertFalse(replacer.accepts(box));
		replacer.execute(box, chunks);
	}
}
