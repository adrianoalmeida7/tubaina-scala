package com.ahalmeida.tubaina.parser

import org.scalatest.FlatSpec

class TubainaParserSpec extends FlatSpec {

  behavior of "Parser"

  it should "parse something" in {
    val tubainaDoc = """
      [chapter Um capítulo novo]

      [section Uma nova section]
      Isso daqui deveria ser um texto do chapter 1

      [section Uma nova section2]
      asdflasdfklj
      [code]
        blablabla
      [/code]
      [java]
        hahaha
      [/java]
      asdfjalsdkfja
      [chapter Outro capítulo]

      [note]
        Isso é uma nota!
      [/note]
      Introducao do chapter 2
      [section hahaha]
      [box hahaha]
        [code]asdlfajsdflk[/code]
      [/box]
    """

    val parser = new TubainaParser
    parser.faz(tubainaDoc)
  }
}