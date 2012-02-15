package com.ahalmeida.tubaina.parser

import org.scalatest.FlatSpec
import br.com.caelum.tubaina.Tubaina
import br.com.caelum.tubaina.parser.html.SingleHtmlGenerator
import br.com.caelum.tubaina.parser.html.HtmlParser
import br.com.caelum.tubaina.ParseType
import br.com.caelum.tubaina.parser.RegexConfigurator
import java.io.File

class TubainaParserSpec extends FlatSpec {

  behavior of "Parser"

  it should "parse something" in {
    val tubainaDoc = """
      [chapter Um capítulo novo]

      [section Uma nova section]
      **Isso** daqui deveria ::se__r__:: um texto do chapter 1

      [section Uma nova section2]
      asdflasdfklj
      [code]
        blablabla
      [/code]
      [java]
        String a = "hahaha";
      [/java]
      asdfjalsdkfja
      [chapter Outro capítulo]

      [note]
        Isso é uma nota!
        [code]
           Um Sysout de código nesse note
        [/code]
      [/note]
      Introducao do chapter 2
      [section hahaha]
      [box hahaha]
        [code]asdlfajsdflk[/code]
      [/box]
    """

    val parser = new TubainaParser
    val parsed = parser.faz(tubainaDoc)
    
    val pars = ParseType.HTML.getParser(new RegexConfigurator, true, false)
    new SingleHtmlGenerator(pars, new File("../tubaina/templates/")).generate(parsed, new File("target"));
  }
}