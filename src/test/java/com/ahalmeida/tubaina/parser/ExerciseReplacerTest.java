package com.ahalmeida.tubaina.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.TubainaException;
import br.com.caelum.tubaina.builder.replacer.Replacer;
import br.com.caelum.tubaina.chunk.ExerciseChunk;
import br.com.caelum.tubaina.resources.Resource;

public class ExerciseReplacerTest {

	private Replacer replacer;
	private List<Chunk> chunks;
	private List<Resource> resources;

	@Before
	public void setUp() {
		resources = new ArrayList<Resource>();
		replacer = ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().exercises());
		chunks = new ArrayList<Chunk>();
	}

	@Test
	public void testReplacesCorrectExerciseWithoutAnswer() {
		String exercise = "[exercise][question]ola mundo[/question][/exercise] ola resto";
		Assert.assertTrue(replacer.accepts(exercise));
		String resto = replacer.execute(exercise, chunks);
		Assert.assertEquals(" ola resto", resto);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(ExerciseChunk.class, chunks.get(0).getClass());
	}

	@Test
	public void testReplacesCorrectExerciseWithInnerAnswer() {
		String exercise = "[exercise][question]ola mundo[answer]ola[/answer][/question][/exercise] ola resto";
		Assert.assertTrue(replacer.accepts(exercise));
		String resto = replacer.execute(exercise, chunks);
		Assert.assertEquals(" ola resto", resto);
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(ExerciseChunk.class, chunks.get(0).getClass());
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutEndTag() {
		String exercise = "[exercise]ola mundo ola resto";
		Assert.assertFalse(replacer.accepts(exercise));
		replacer.execute(exercise, chunks);
	}

	@Test(expected = TubainaException.class)
	public void testDoesntAcceptWithoutBeginTag() {
		String exercise = "ola mundo[/exercise] ola resto";
		Assert.assertFalse(replacer.accepts(exercise));
		replacer.execute(exercise, chunks);
	}
}
