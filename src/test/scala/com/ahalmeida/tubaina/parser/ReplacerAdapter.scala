package com.ahalmeida.tubaina.parser
import br.com.caelum.tubaina.builder.replacer.Replacer
import br.com.caelum.tubaina.Chunk
import java.util.List
import scala.util.parsing.combinator.Parsers
import br.com.caelum.tubaina.TubainaException

object ReplacerAdapterFactory {
  val parser = new TubainaParser("")
  import parser._
  def replacerFor[T <: Chunk](p:Parser[T]):Replacer = new Replacer {
    def execute(text:String, chunks:List[Chunk]) = {
	  parse(p, text) match {
	    case Success(ps, rest) => 
	      chunks.add(ps)
	      text.substring(rest.offset)
	    case NoSuccess(msg, input) =>
	      throw new TubainaException(msg)
	  }
	}

	def accepts(text:String) = parse(p, text).successful
  }
	
}