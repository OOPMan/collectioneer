package com.oopman.collectioneer.gui

import java.nio.file.Path
import fr.brouillard.oss.cssfx.api.URIToPathConverter

/**
 * CSSFX URIToPathConverter implementation compatible with Scala SBT
 */
object ScalaSBTURIToPathConverter extends URIToPathConverter:
  // TODO: Replacements should include src/test options
  val replacements = "src/main/resources" :: "src/main/scala" :: Nil
  val patterns = Seq("target/scala-[^/]+/classes", "target/scala-[^/]+/test-classes").map(_.r.unanchored)

  def convert(uri: String): Path =
    val uriOption =
      for
        uri <- Option(uri)
        if uri.startsWith("file:/")
      yield uri.stripPrefix("file:/")
    val uriMatchingPatternOption =
      for
        uri <- uriOption
        pattern <- patterns.find(_.matches(uri))
      yield uri -> pattern
    val pathOption =
      for
        (uri, pattern) <- uriMatchingPatternOption
        updatedUris = replacements.map(pattern.replaceAllIn(uri, _))
        paths = updatedUris.map(os.Path.apply)
        path <- paths.find(os.exists(_))
      yield path
    pathOption.map(_.toNIO).orNull