package com.oopman.collectioneer.plugins.postgresbackend.gui

import com.oopman.collectioneer.Plugin
import com.oopman.collectioneer.plugins.postgresbackend.gui.PostgresDatabaseBackendUIPluginDef.many
import com.oopman.collectioneer.plugins.{DatabaseBackendGUIPlugin, GUIPlugin}
import izumi.distage.plugins.PluginDef

object PostgresDatabaseBackendUIPluginDef extends PluginDef:
  many[DatabaseBackendGUIPlugin]
    .add[PostgresDatabaseBackendUIPlugin]
    .add[EmbeddedPostgresDatabaseBackendUIPlugin]
  many[GUIPlugin]
    .add[PostgresDatabaseBackendUIPlugin]
    .add[EmbeddedPostgresDatabaseBackendUIPlugin]
  many[Plugin]
    .add[PostgresDatabaseBackendUIPlugin]
    .add[EmbeddedPostgresDatabaseBackendUIPlugin]
