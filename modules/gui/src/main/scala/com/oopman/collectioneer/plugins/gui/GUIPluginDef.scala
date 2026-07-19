package com.oopman.collectioneer.plugins.gui

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.plugins.gui.controls.*
import com.oopman.collectioneer.plugins.{GUIPlugin, PropertyValueEditorGUIPlugin}
import izumi.distage.plugins.PluginDef

object GUIPluginDef extends PluginDef:
  many[PropertyValueEditorGUIPlugin]
    .add[StringPropertyValueEditorGUIPlugin]
    .add[ShortPropertyValueEditorGUIPlugin]
    .add[IntPropertyValueEditorGUIPlugin]
    .add[LongPropertyValueEditorGUIPlugin]
    .add[FloatPropertyValueEditorGUIPlugin]
    .add[DoublePropertyValueEditorGUIPlugin]
    .add[BooleanPropertyValueEditorGUIPlugin]
  many[GUIPlugin]
    .add[StringPropertyValueEditorGUIPlugin]
    .add[ShortPropertyValueEditorGUIPlugin]
    .add[IntPropertyValueEditorGUIPlugin]
    .add[LongPropertyValueEditorGUIPlugin]
    .add[FloatPropertyValueEditorGUIPlugin]
    .add[DoublePropertyValueEditorGUIPlugin]
    .add[BooleanPropertyValueEditorGUIPlugin]
  many[Plugin]
    .add[StringPropertyValueEditorGUIPlugin]
    .add[ShortPropertyValueEditorGUIPlugin]
    .add[IntPropertyValueEditorGUIPlugin]
    .add[LongPropertyValueEditorGUIPlugin]
    .add[FloatPropertyValueEditorGUIPlugin]
    .add[DoublePropertyValueEditorGUIPlugin]
    .add[BooleanPropertyValueEditorGUIPlugin]
