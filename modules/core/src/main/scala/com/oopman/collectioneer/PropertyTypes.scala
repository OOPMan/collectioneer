package com.oopman.collectioneer

enum PropertyTypes {
  case varchar extends PropertyTypes
  case varbinary extends PropertyTypes
  case tinyint extends PropertyTypes
  case smallint extends PropertyTypes
  case int extends PropertyTypes
  case bigint extends PropertyTypes
  case numeric extends PropertyTypes
  case float extends PropertyTypes
  case double extends PropertyTypes
  case boolean extends PropertyTypes
  case date extends PropertyTypes
  case time extends PropertyTypes
  case timestamp extends PropertyTypes
  case clob extends PropertyTypes
  case blob extends PropertyTypes
  case uuid extends PropertyTypes
  case json extends PropertyTypes
}

