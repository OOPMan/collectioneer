package com.oopman.collectioneer

import java.util.UUID

/**
 * This enumeration contains the UUIDs associated with the core properties in the application
 */
enum CoreProperties(val uuid: UUID) {
  case name extends CoreProperties(UUID.fromString("f65d1d21-3542-48f9-a9d5-f96921e4ba15"))
  case description extends CoreProperties(UUID.fromString("ea3c3226-2a04-45f0-a50e-1db9c4aa0072"))
  case default_value extends CoreProperties(UUID.fromString("7b7b9577-da92-41d2-9ff8-a174e191b030"))
  case min_values extends CoreProperties(UUID.fromString("df39b389-5055-46f7-82f3-53c736fb6950"))
  case max_values extends CoreProperties(UUID.fromString("158a2358-b5d1-4e73-a6bf-c93d0587590d"))
}
