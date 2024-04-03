package com.oopman.collectioneer.db.scalikejdbc

import scalikejdbc.DBConnection


type DBConnectionProvider = () => DBConnection
