package com.oopman.collectioneer.gui

import scalafx.css.Styleable

trait StyleClasses(styleClasses: String*) extends Styleable:
  styleClass.addAll(styleClasses)
