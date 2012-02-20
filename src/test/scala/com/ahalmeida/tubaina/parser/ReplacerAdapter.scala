package com.ahalmeida.tubaina.parser
import br.com.caelum.tubaina.builder.replacer.Replacer
import br.com.caelum.tubaina.Chunk
import java.util.List
import scala.util.parsing.combinator.Parsers
import br.com.caelum.tubaina.TubainaException
import scala.collection.JavaConversions._

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
	      throw new TubainaException(msg + input.source.subSequence(Math.max(input.offset - 7, 0), input.source.length()))
	  }
	}

	def accepts(text:String) = parse(p, text).successful
  }
  
  def parseContent(text:String):List[Chunk] = parse(content, text) match {
       case Success(ps, rest) =>
         ps
	   case NoSuccess(msg, input) =>
	      throw new TubainaException(msg + input.source.subSequence(Math.max(input.offset - 7, 0), input.source.length()))
  }
	
}