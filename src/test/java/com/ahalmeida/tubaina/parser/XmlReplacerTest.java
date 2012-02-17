package com.ahalmeida.tubaina.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.TubainaException;
import br.com.caelum.tubaina.builder.replacer.Replacer;
import br.com.caelum.tubaina.builder.replacer.XmlReplacer;
import br.com.caelum.tubaina.chunk.XmlChunk;

public class XmlReplacerTest {

	private Replacer replacer;

	private List<Chunk> chunks;

	@Before
	public void setUp() {
		replacer = ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().xml());
		chunks = new ArrayList<Chunk>();
	}

	@Test
	public void testReplacesCorrectXml() {
		String xml = "[xml]<bla slkdj> skj</bla>[/xml] oi resto";
		Assert.assertTrue(replacer.accepts(xml));
		String resto = replacer.execute(xml, chunks);
		Assert.assertEquals(" oi resto", resto);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(XmlChunk.class, chunks.get(0).getClass());
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutEndTag() {
		String xml = "[xml]<bla slkdj> skj</bla> oi resto";
		Assert.assertFalse(replacer.accepts(xml));
		replacer.execute(xml, chunks);
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutBeginTag() {
		String xml = "<bla slkdj> skj</bla>[/xml] oi resto";
		Assert.assertFalse(replacer.accepts(xml));
		replacer.execute(xml, chunks);
	}
}
