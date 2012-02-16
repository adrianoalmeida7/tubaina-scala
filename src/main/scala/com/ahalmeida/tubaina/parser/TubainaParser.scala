package com.ahalmeida.tubaina.parser

import util.parsing.combinator._
import java.lang.RuntimeException
import br.com.caelum.tubaina.chunk._
import br.com.caelum.tubaina.Chunk
import scala.collection.JavaConversions._
import br.com.caelum.tubaina.Chapter
import br.com.caelum.tubaina.Section
import br.com.caelum.tubaina.Book
import scala.annotation.varargs
import br.com.caelum.tubaina.TubainaException

class TubainaParser(bookName:String) extends JavaTokenParsers {

  var _skip = true
  override def skipWhitespace = _skip
  
  def verbose[T](p:Parser[T]) = new Parser[T] {
    def apply(in:Input) = {
      _skip = false
      val x = p(in)
      _skip = true
      x
    }
  }
  def nonBracket:Parser[String] = "[^\\[\\]]+".r

  def chapter:Parser[Chapter] =
    p("[chapter " ~> nonBracket <~ "]") ~ (content?) ~ (section*) ^^ {
      case name ~ Some(intro) ~ sections => new Chapter(name.trim(), new IntroductionChunk(intro), sections, Seq())
      case name ~ None ~ sections => new Chapter(name.trim(), new IntroductionChunk(Seq()), sections, Seq())
    }
  
  def section:Parser[Section] =
    p("[section " ~> nonBracket <~ "]") ~ (content?) ^^ {
      case name ~ Some(content) => new Section(name.trim(), content)
      case name ~ None => new Section(name.trim(), Seq())
    }

  def exercises:Parser[ExerciseChunk] = "[exercises]" ~> (question+) <~ "[/exercises]" ^^ {
    case questions => new ExerciseChunk(questions)
  }
  
  def question:Parser[QuestionChunk] = "[question]" ~> content ~ (answer?) <~ "[/question]" ^^ {
    case content ~ answer => new QuestionChunk(content ++ answer)
  }
  
  def answer:Parser[AnswerChunk] = "[answer]" ~> content <~ "[/answer]" ^^ {
    case content => new AnswerChunk(content)
  }

//  def bold:Parser[Bold] = "**" ~> paragraph <~ "**" ^^ {p => Bold(p)}
//  def em:Parser[Em] = "::" ~> paragraph <~ "::" ^^ {p => Em(p)}
//  def und:Parser[Und] = "__" ~> paragraph <~ "__" ^^ {p => Und(p)}
//  def mono:Parser[Mono] = "%%" ~> paragraph <~ "%%" ^^ {p => Mono(p)}
  def text:Parser[String] = "((?!\n\n)[^\\[])+".r ^^ {x => x.trim()}

  //  def textElem:Parser[TextElement] = bold | em | und | mono | text
  
  def paragraph:Parser[ParagraphChunk] = (text)  ^^ { x => new ParagraphChunk(x) }

  def code:Parser[CodeChunk] = p("[code " ~> nonBracket <~ "]" | "[code]") ~ verbose(nonBracket) <~ "[/code]" ^^ {
  	case "[code]" ~ content => new CodeChunk(content, "") 
    case opts ~ content => new CodeChunk(content, opts) 
  }
  def java:Parser[JavaChunk] = p("[java " ~> nonBracket <~"]" | "[java]") ~ verbose(nonBracket) <~ "[/java]" ^^ {
    case "[java]" ~ content => new JavaChunk("", content)
    case opts ~ content => new JavaChunk(opts, content)
  }
  def ruby:Parser[RubyChunk] = p("[ruby " ~> nonBracket <~"]" | "[ruby]") ~ verbose(nonBracket) <~ "[/ruby]" ^^ {
    case "[ruby]" ~ content => new RubyChunk(content, "")
    case opts ~ content => new RubyChunk(content, opts)
  }
  def box:Parser[BoxChunk] = p("[box " ~> nonBracket <~ "]") ~ content <~ "[/box]" ^^ {
    case title ~ chunks => new BoxChunk(title, chunks)
  }
  
  def note:Parser[NoteChunk] = "[note]" ~> content <~ "[/note]" ^^ (x => new NoteChunk(Seq(), x))
  
  def item:Parser[ItemChunk] = "^\\s*\\* ".r ~> "(.(?!^\\s*\\*|\\[/list\\]))+.".r ^^ {
    x => new ItemChunk(parseAll(content, x).get)
  }
  
  def list:Parser[ListChunk] = "[list]" ~> (item+) <~ "[/list]" ^^ (x => new ListChunk("", x))
  
  def col:Parser[TableColumnChunk] = "[col]" ~> content <~ "[/col]" ^^ (x => new TableColumnChunk(x))
    
  def row:Parser[TableRowChunk] = "[row]" ~> (col+) <~ "[/row]" ^^ (x => new TableRowChunk(x))
  
  def table:Parser[TableChunk] = "[table]" ~> (row+) <~ "[/table]" ^^ (x => new TableChunk("", x))
  
  def center:Parser[CenteredParagraphChunk] = "[center]" ~> ("[^\\[]+".r) <~ "[/center]" ^^ (x => new CenteredParagraphChunk(x))

  def elem:Parser[Chunk] = center | table | list | code | java | ruby | paragraph | box | note | exercises
  
  def content:Parser[Seq[Chunk]] = elem+

  def p(s:Parser[String]) = s

  @varargs
  def generate(toParse:String*) = {
	  val chapters = toParse.map { chap =>
      parseAll(chapter, chap) match {
	    case Success(r, q) => r
	    case NoSuccess(message, input) => message match {
	      case _ => throw new TubainaException(message)
	    }
	  }
    }
    
    new Book(bookName, chapters, false)
  }
}
