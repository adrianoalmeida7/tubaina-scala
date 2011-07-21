package com.ahalmeida.tubaina.parser

import util.parsing.combinator._
import java.lang.RuntimeException

class TubainaParser extends JavaTokenParsers {

  def document:Parser[Any] = (chapter+) ^^ {
    case list => new TubainaDocument(list)
  }

  def nonBracket:Parser[String] = "[^\\[\\]]+".r ^^ (x => x.trim())

  def chapter:Parser[Chapter] =
    p("[chapter " ~> nonBracket <~ "]") ~ (content?) ~ rep(section) ^^ {
      case name ~ Some(intro) ~ sections => Chapter(name, intro, sections)
      case name ~ None ~ sections => Chapter(name, NoContent(), sections)
    }

  def code:Parser[Code] = "[code]" ~> nonBracket <~ "[/code]" ^^ (x => Code(x))
  def box:Parser[Box] = p("[box "~> nonBracket <~ "]") ~ content <~ "[/box]" ^^ {case x ~ y => Box(x, y)}
  def java:Parser[Java] = "[java]" ~> nonBracket <~ "[/java]" ^^ (x => Java(x))
  def text:Parser[Text] = "[^\\[]+".r ^^ (x => Text(x.trim()))

  def note:Parser[Note] = "[note]" ~> content <~ "[/note]" ^^ (x => Note(x))

  def elem:Parser[Content] = code | java | text | box | note
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

case class Box(val name:String, val content:Content) extends Content
case class Code(val content:String) extends Content
case class Java(val content:String) extends Content

case class Text(val content:String) extends Content

case class Note(val content:Content) extends Content

case class Multi(val contents:List[Content]) extends Content {
  override def :: (content:Content) = Multi(content :: contents)
}

case class TubainaDocument(val chapter:List[Chapter])

case class Chapter(val name:String, val intro:Content, val sections:List[Section])

case class Section(val name:String, val content:Content)