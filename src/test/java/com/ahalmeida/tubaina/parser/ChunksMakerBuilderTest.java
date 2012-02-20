package com.ahalmeida.tubaina.parser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.TubainaException;
import br.com.caelum.tubaina.builder.ChunksMaker;
import br.com.caelum.tubaina.builder.ChunksMakerBuilder;
import br.com.caelum.tubaina.builder.replacer.Replacer;
import br.com.caelum.tubaina.chunk.AnswerChunk;
import br.com.caelum.tubaina.chunk.BoxChunk;
import br.com.caelum.tubaina.chunk.CenteredParagraphChunk;
import br.com.caelum.tubaina.chunk.CodeChunk;
import br.com.caelum.tubaina.chunk.ExerciseChunk;
import br.com.caelum.tubaina.chunk.ImageChunk;
import br.com.caelum.tubaina.chunk.IndexChunk;
import br.com.caelum.tubaina.chunk.ItemChunk;
import br.com.caelum.tubaina.chunk.JavaChunk;
import br.com.caelum.tubaina.chunk.ListChunk;
import br.com.caelum.tubaina.chunk.NoteChunk;
import br.com.caelum.tubaina.chunk.ParagraphChunk;
import br.com.caelum.tubaina.chunk.QuestionChunk;
import br.com.caelum.tubaina.chunk.RubyChunk;
import br.com.caelum.tubaina.chunk.TableChunk;
import br.com.caelum.tubaina.chunk.TableColumnChunk;
import br.com.caelum.tubaina.chunk.TableRowChunk;
import br.com.caelum.tubaina.chunk.TodoChunk;
import br.com.caelum.tubaina.chunk.XmlChunk;
import br.com.caelum.tubaina.resources.Resource;
import br.com.caelum.tubaina.resources.ResourceLocator;

public class ChunksMakerBuilderTest {


	private List<Resource> resources;

	private String exampleBox = "[box title]a box[/box]\n";
	private String exampleParagraph = "some text\n\n";
	private String exampleCode = "[code]some code[/code]\n";
	private String exampleJava = "[java]\nSystem.out.println(\"some java code\");\n[/java]\n";
	private String exampleListItem = "* an item\n";
	private String exampleList = "[list]\n" + exampleListItem + exampleListItem
			+ "[/list]\n";
	private String exampleNote = "[note]a note to the instructor[/note]\n";
	private String exampleXml = "[xml]<tag>xml</tag>[/xml]\n";
	private String exampleIndex = "[index an index]\n";
	private String exampleTodo = "[todo something to do]\n";
	private String exampleRuby = "[ruby]\nputs 'some ruby code'\n[/ruby]\n";
	private String exampleTableColumn = "[col]a column[/col]\n";
	private String exampleTableRow = "[row]\n" + exampleTableColumn
			+ exampleTableColumn + "[/row]\n";
	private String exampleTable = "[table]" + exampleTableRow + exampleTableRow
			+ "[/table]\n";
	private String exampleCenteredText = "[center]some centered text[/center]\n";
	private String exampleAnswer = "[answer]42[/answer]\n";
	private String exampleQuestion = "[question]\na question\n" + exampleAnswer
			+ "[/question]\n";
	private String exampleExercise = "[exercise]\n" + exampleQuestion
			+ "[/exercise]\n";
	private String exampleImage = "[img src/test/resources/baseJpgImage.jpg]\n";

	@Before
	public void setUp() {
		resources = new ArrayList<Resource>();
		ResourceLocator.initialize(".");
	}

	@Test
	public void testChunksMakerBuilderForAnswer() throws Exception{
		String text = exampleBox + exampleCode + exampleImage + exampleJava
				+ exampleList + exampleNote + exampleXml + exampleIndex
				+ exampleTodo + exampleRuby + exampleTable
				+ exampleCenteredText + exampleParagraph;
		List<Chunk> chunks = createChunks("[answer]" + text + "[/answer]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().answer()));
		Assert.assertEquals(13, chunks.size());
		Assert.assertEquals(BoxChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(CodeChunk.class, chunks.get(1).getClass());
		Assert.assertEquals(ImageChunk.class, chunks.get(2).getClass());
		Assert.assertEquals(JavaChunk.class, chunks.get(3).getClass());
		Assert.assertEquals(ListChunk.class, chunks.get(4).getClass());
		Assert.assertEquals(NoteChunk.class, chunks.get(5).getClass());
		Assert.assertEquals(XmlChunk.class, chunks.get(6).getClass());
		Assert.assertEquals(IndexChunk.class, chunks.get(7).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(8).getClass());
		Assert.assertEquals(RubyChunk.class, chunks.get(9).getClass());
		Assert.assertEquals(TableChunk.class, chunks.get(10).getClass());
		Assert.assertEquals(CenteredParagraphChunk.class, chunks.get(11)
				.getClass());
		Assert.assertEquals(ParagraphChunk.class, chunks.get(12).getClass());
	}

	@Test
	public void testChunksMakerBuilderForBox() throws Exception {
		String text = exampleCode + exampleImage + exampleJava + exampleList
				+ exampleNote + exampleXml + exampleIndex + exampleTodo
				+ exampleRuby + exampleTable + exampleCenteredText
				+ exampleParagraph;
		List<Chunk> chunks = createChunks("[box title]" + text + "[/box]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().box()));
		Assert.assertEquals(12, chunks.size());
		Assert.assertEquals(CodeChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(ImageChunk.class, chunks.get(1).getClass());
		Assert.assertEquals(JavaChunk.class, chunks.get(2).getClass());
		Assert.assertEquals(ListChunk.class, chunks.get(3).getClass());
		Assert.assertEquals(NoteChunk.class, chunks.get(4).getClass());
		Assert.assertEquals(XmlChunk.class, chunks.get(5).getClass());
		Assert.assertEquals(IndexChunk.class, chunks.get(6).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(7).getClass());
		Assert.assertEquals(RubyChunk.class, chunks.get(8).getClass());
		Assert.assertEquals(TableChunk.class, chunks.get(9).getClass());
		Assert.assertEquals(CenteredParagraphChunk.class, chunks.get(10)
				.getClass());
		Assert.assertEquals(ParagraphChunk.class, chunks.get(11).getClass());
	}

	@Test
	public void testChunksMakerBuilderForExercise() throws Exception {
		String text = exampleTodo + exampleQuestion;
		List<Chunk> chunks = createChunks("[exercise]" + text + "[/exercise]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().exercises()));
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals(TodoChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(QuestionChunk.class, chunks.get(1).getClass());
	}

	@Test
	public void testChunksMakerBuilderForItem() throws Exception {
		String text = exampleBox + exampleCode + exampleExercise + exampleImage
				+ exampleJava + exampleList + exampleNote + exampleXml
				+ exampleIndex + exampleTodo + exampleRuby
				+ exampleCenteredText + exampleParagraph;
		List<Chunk> chunks = createChunks("* " + text, ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().item()));
		Assert.assertEquals(13, chunks.size());
		Assert.assertEquals(BoxChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(CodeChunk.class, chunks.get(1).getClass());
		Assert.assertEquals(ExerciseChunk.class, chunks.get(2).getClass());
		Assert.assertEquals(ImageChunk.class, chunks.get(3).getClass());
		Assert.assertEquals(JavaChunk.class, chunks.get(4).getClass());
		Assert.assertEquals(ListChunk.class, chunks.get(5).getClass());
		Assert.assertEquals(NoteChunk.class, chunks.get(6).getClass());
		Assert.assertEquals(XmlChunk.class, chunks.get(7).getClass());
		Assert.assertEquals(IndexChunk.class, chunks.get(8).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(9).getClass());
		Assert.assertEquals(RubyChunk.class, chunks.get(10).getClass());
		Assert.assertEquals(CenteredParagraphChunk.class, chunks.get(11)
				.getClass());
		Assert.assertEquals(ParagraphChunk.class, chunks.get(12).getClass());
	}

	@Test
	public void testChunksMakerBuilderForList() throws Exception {
		String text = exampleListItem;
		List<Chunk> chunks = createChunks("[list]" + text + "[/list]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().list()));
		Assert.assertEquals(1, chunks.size());
		Assert.assertEquals(ItemChunk.class, chunks.get(0).getClass());
	}

	@Test
	public void testChunksMakerBuilderForNote() throws Exception {
		String text = exampleCode + exampleImage + exampleJava + exampleList
				+ exampleXml + exampleIndex + exampleTodo + exampleRuby
				+ exampleTable + exampleCenteredText + exampleParagraph;
		List<Chunk> chunks = createChunks("[note]" + text + "[/note]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().note()));
		Assert.assertEquals(11, chunks.size());
		Assert.assertEquals(CodeChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(ImageChunk.class, chunks.get(1).getClass());
		Assert.assertEquals(JavaChunk.class, chunks.get(2).getClass());
		Assert.assertEquals(ListChunk.class, chunks.get(3).getClass());
		Assert.assertEquals(XmlChunk.class, chunks.get(4).getClass());
		Assert.assertEquals(IndexChunk.class, chunks.get(5).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(6).getClass());
		Assert.assertEquals(RubyChunk.class, chunks.get(7).getClass());
		Assert.assertEquals(TableChunk.class, chunks.get(8).getClass());
		Assert.assertEquals(CenteredParagraphChunk.class, chunks.get(9)
				.getClass());
		Assert.assertEquals(ParagraphChunk.class, chunks.get(10).getClass());
	}

	@Test
	public void testChunksMakerBuilderForQuestion() throws Exception {
		String text = exampleAnswer + exampleBox + exampleCode + exampleImage
				+ exampleJava + exampleList + exampleNote + exampleXml
				+ exampleIndex + exampleTodo + exampleRuby + exampleTable
				+ exampleCenteredText + exampleParagraph;
		List<Chunk> chunks = createChunks("[question]" + text + "[/question]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().question()));
		Assert.assertEquals(14, chunks.size());
		Assert.assertEquals(AnswerChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(BoxChunk.class, chunks.get(1).getClass());
		Assert.assertEquals(CodeChunk.class, chunks.get(2).getClass());
		Assert.assertEquals(ImageChunk.class, chunks.get(3).getClass());
		Assert.assertEquals(JavaChunk.class, chunks.get(4).getClass());
		Assert.assertEquals(ListChunk.class, chunks.get(5).getClass());
		Assert.assertEquals(NoteChunk.class, chunks.get(6).getClass());
		Assert.assertEquals(XmlChunk.class, chunks.get(7).getClass());
		Assert.assertEquals(IndexChunk.class, chunks.get(8).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(9).getClass());
		Assert.assertEquals(RubyChunk.class, chunks.get(10).getClass());
		Assert.assertEquals(TableChunk.class, chunks.get(11).getClass());
		Assert.assertEquals(CenteredParagraphChunk.class, chunks.get(12)
				.getClass());
		Assert.assertEquals(ParagraphChunk.class, chunks.get(13).getClass());
	}

	@Test
	public void testChunksMakerBuilderForTable() throws Exception {
		String text = exampleTableRow + exampleTodo;
		List<Chunk> chunks = createChunks("[table]" + text + "[/table]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().table()));
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals(TableRowChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(1).getClass());
	}

	@Test
	public void testChunksMakerBuilderForTableRow() throws Exception {
		String text = exampleTableColumn + exampleTodo;
		List<Chunk> chunks = createChunks("[row]" + text + "[/row]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().row()));
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals(TableColumnChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(1).getClass());
	}

	@Test
	public void testChunksMakerBuilderForTableColumn() throws Exception {
		String text = exampleBox + exampleCode + exampleExercise + exampleImage
				+ exampleJava + exampleList + exampleNote + exampleXml
				+ exampleTodo + exampleRuby + exampleParagraph;
		List<Chunk> chunks = createChunks("[col]" + text + "[/col]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().col()));
		Assert.assertEquals(11, chunks.size());
		Assert.assertEquals(BoxChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(CodeChunk.class, chunks.get(1).getClass());
		Assert.assertEquals(ExerciseChunk.class, chunks.get(2).getClass());
		Assert.assertEquals(ImageChunk.class, chunks.get(3).getClass());
		Assert.assertEquals(JavaChunk.class, chunks.get(4).getClass());
		Assert.assertEquals(ListChunk.class, chunks.get(5).getClass());
		Assert.assertEquals(NoteChunk.class, chunks.get(6).getClass());
		Assert.assertEquals(XmlChunk.class, chunks.get(7).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(8).getClass());
		Assert.assertEquals(RubyChunk.class, chunks.get(9).getClass());
		Assert.assertEquals(ParagraphChunk.class, chunks.get(10).getClass());
	}

	@Test
	public void testChunksMakerBuilderForAll() {
		String text = exampleBox + exampleCode + exampleExercise + exampleImage
				+ exampleJava + exampleList + exampleNote + exampleXml
				+ exampleIndex + exampleTodo + exampleRuby + exampleTable
				+ exampleCenteredText + exampleParagraph;
		List<Chunk> chunks = ReplacerAdapterFactory.parseContent(text);
		Assert.assertEquals(14, chunks.size());
		Assert.assertEquals(BoxChunk.class, chunks.get(0).getClass());
		Assert.assertEquals(CodeChunk.class, chunks.get(1).getClass());
		Assert.assertEquals(ExerciseChunk.class, chunks.get(2).getClass());
		Assert.assertEquals(ImageChunk.class, chunks.get(3).getClass());
		Assert.assertEquals(JavaChunk.class, chunks.get(4).getClass());
		Assert.assertEquals(ListChunk.class, chunks.get(5).getClass());
		Assert.assertEquals(NoteChunk.class, chunks.get(6).getClass());
		Assert.assertEquals(XmlChunk.class, chunks.get(7).getClass());
		Assert.assertEquals(IndexChunk.class, chunks.get(8).getClass());
		Assert.assertEquals(TodoChunk.class, chunks.get(9).getClass());
		Assert.assertEquals(RubyChunk.class, chunks.get(10).getClass());
		Assert.assertEquals(TableChunk.class, chunks.get(11).getClass());
		Assert.assertEquals(CenteredParagraphChunk.class, chunks.get(12)
				.getClass());
		Assert.assertEquals(ParagraphChunk.class, chunks.get(13).getClass());
	}

	@Test(expected=TubainaException.class)
	public void testChunksMakerBuilderForAllDoesntAcceptQuestionTagOutsideExercise() {
		String exercise = exampleQuestion;
		List<Chunk> list = ReplacerAdapterFactory.parseContent(exercise);
		for (Chunk chunk : list) {
			System.out.println(chunk.getClass().getName());
		}
		Assert.fail("Should not accept question tag outside exercise tag");
	}

	@Test(expected=TubainaException.class)
	public void testChunksMakerBuilderDoesntAcceptNoteInsideExerciseOutsideQuestion() throws Exception{
		String exercise = exampleNote + exampleQuestion;
		createChunks("[exercise]" + exercise + "[/exercise]", ReplacerAdapterFactory.replacerFor(ReplacerAdapterFactory.parser().exercises()));
		Assert.fail("Should not accept notes inside exercise tag but outside question tag");
	}
	
	private List<Chunk> createChunks(String text, Replacer replacer) throws Exception {
		
		List<Chunk> chunks = new ArrayList<Chunk>();
		Assert.assertEquals("", replacer.execute(text, chunks).trim());
		Chunk chunk = chunks.get(0);
		
		if (chunk instanceof NoteChunk) {
			Field body = chunk.getClass().getDeclaredField("body");
			body.setAccessible(true);
			return (List<Chunk>) body.get(chunk);
		}
		for (Field field : chunk.getClass().getDeclaredFields()) {
			if (field.getGenericType() instanceof ParameterizedType) {
				ParameterizedType type = (ParameterizedType) field.getGenericType();
				field.setAccessible(true);
				if (Chunk.class.equals(type.getActualTypeArguments()[0])) {
					return (List<Chunk>) field.get(chunk);
				}
			}
		}
		throw new AssertionError("O chunk não tem field List<Chunk>");
	}
}
