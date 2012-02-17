package com.ahalmeida.tubaina.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.TubainaException;
import br.com.caelum.tubaina.builder.replacer.JavaReplacer;
import br.com.caelum.tubaina.builder.replacer.Replacer;
import br.com.caelum.tubaina.chunk.JavaChunk;

public class JavaReplacerTest {

	private Replacer replacer;
	private List<Chunk> chunks;

	@Before
	public void setUp() {
		replacer = ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().java());
		chunks = new ArrayList<Chunk>();
	}

	@Test
	public void testReplacesCorrectJava() {
		String java = "[java]public class Teste()[/java] ola resto";
		Assert.assertTrue(replacer.accepts(java));
		String resto = replacer.execute(java, chunks);
		Assert.assertEquals(" ola resto", resto);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(JavaChunk.class, chunks.get(0).getClass());
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutEndTag() {
		String java = "[java]public class Teste() ola resto";
		Assert.assertFalse(replacer.accepts(java));
		replacer.execute(java, chunks);
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutBeginTag() {
		String java = "public class Teste()[/java] ola resto";
		Assert.assertFalse(replacer.accepts(java));
		replacer.execute(java, chunks);
	}
}
