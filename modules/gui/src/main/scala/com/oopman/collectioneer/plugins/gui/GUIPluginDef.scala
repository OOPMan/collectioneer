package com.oopman.collectioneer.plugins.gui

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.plugins.gui.controls.{IntPropertyValueEditorGUIPlugin, SmallintPropertyValueEditorGUIPlugin, TextPropertyValueEditorGUIPlugin}
import com.oopman.collectioneer.plugins.{GUIPlugin, PropertyValueEditorGUIPlugin}
import izumi.distage.plugins.PluginDef

object GUIPluginDef extends PluginDef:
  many[PropertyValueEditorGUIPlugin]
    .add[TextPropertyValueEditorGUIPlugin]
    .add[IntPropertyValueEditorGUIPlugin]
    .add[SmallintPropertyValueEditorGUIPlugin]
  many[GUIPlugin]
    .add[TextPropertyValueEditorGUIPlugin]
    .add[IntPropertyValueEditorGUIPlugin]
    .add[SmallintPropertyValueEditorGUIPlugin]
  many[Plugin]
    .add[TextPropertyValueEditorGUIPlugin]
    .add[IntPropertyValueEditorGUIPlugin]
    .add[SmallintPropertyValueEditorGUIPlugin]
