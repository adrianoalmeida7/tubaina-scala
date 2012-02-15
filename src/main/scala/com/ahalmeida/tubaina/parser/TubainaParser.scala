package com.ahalmeida.tubaina.parser

import util.parsing.combinator._
import java.lang.RuntimeException

class TubainaParser extends JavaTokenParsers {

  def document:Parser[Any] = (chapter+) ^^ {
    case list => new TubainaDocument(list)
  }

  def nonBracket:Parser[String] = "[^\\[\\]]+".r ^^ (x => x.trim())

  def chapter:Parser[Chapter] =
    p("[chapter " ~> nonBracket <~ "]") ~ (content?) ~ (section+) ^^ {
      case name ~ Some(intro) ~ sections => Chapter(name, intro, sections)
      case name ~ None ~ sections => Chapter(name, NoContent(), sections)
    }

  def exercises:Parser[Exercises] = "[exercises]" ~> (question+) <~ "[/exercises]" ^^ {
    case questions => Exercises(questions)
  }
  
  def question:Parser[Question] = "[question]" ~> content ~ (answer?) <~ "[/question]" ^^ {
    case content ~ answer => Question (content, answer)
  }
  
  def answer:Parser[Answer] = "[answer]" ~> content <~ "[/answer]" ^^ {
    case content => Answer(content)
  }

  def bold:Parser[Bold] = "**" ~> paragraph <~ "**" ^^ {p => Bold(p)}
  def em:Parser[Em] = "::" ~> paragraph <~ "::" ^^ {p => Em(p)}
  def und:Parser[Und] = "__" ~> paragraph <~ "__" ^^ {p => Und(p)}
  def mono:Parser[Mono] = "%%" ~> paragraph <~ "%%" ^^ {p => Mono(p)}

  def textElem:Parser[TextElement] = text | bold | em | und | mono
  
  def paragraph:Parser[Paragraph] = (textElem+) ^^ { x => Paragraph(x) }


  def code:Parser[Code] = "[code]" ~> nonBracket <~ "[/code]" ^^ (x => Code(x))
  def box:Parser[Box] = p("[box "~> nonBracket <~ "]") ~ content <~ "[/box]" ^^ {case x ~ y => Box(x, y)}
  def java:Parser[Java] = "[java]" ~> nonBracket <~ "[/java]" ^^ (x => Java(x))
  def text:Parser[Text] = "[^\\[]+".r ^^ (x => Text(x.trim()))

  def note:Parser[Note] = "[note]" ~> content <~ "[/note]" ^^ (x => Note(x))

  def elem:Parser[Content] = code | java | paragraph | box | note | exercises
  def content:Parser[Content] =
    elem ~ content ^^ {case x ~ t => x :: t} |
    elem



  def section:Parser[Section] =
    p("[section " ~> nonBracket <~ "]") ~ (content?) ^^ {
      case name ~ Some(content) => Section(name, content)
      case name ~ None => Section(name, NoContent())
    }

  def p(s:Parser[String]) = s

  def faz(toParse:String) = {
    parseAll(document, toParse) match {
      case Success(r, q) => r
      case NoSuccess(message, input) => message match {
        case _ => throw new RuntimeException(message)
      }
    }


  }
}

trait Content {
  def :: (content:Content) = Multi(List(content, this))
}
case class NoContent() extends Content

case class Box(name:String, content:Content) extends Content
case class Code(content:String) extends Content
case class Java(content:String) extends Content

case class Note(content:Content) extends Content

case class Exercises(question:Seq[Question]) extends Content

case class Question(content:Content, answer:Option[Answer]) extends Content

case class Answer(content:Content) extends Content

case class Text(content:String) extends TextElement
case class Bold(text: Paragraph) extends TextElement
case class Em(text: Paragraph) extends TextElement
case class Und(text: Paragraph) extends TextElement
case class Mono(text: Paragraph) extends TextElement

case class Paragraph(texts: Seq[TextElement]) extends Content

case class TextElement extends Content

case class Multi(contents:List[Content]) extends Content {
  override def :: (content:Content) = Multi(content :: contents)
}

case class TubainaDocument(chapter:List[Chapter])

case class Chapter(name:String, intro:Content, sections:List[Section])

case class Section(name:String, content:Content)