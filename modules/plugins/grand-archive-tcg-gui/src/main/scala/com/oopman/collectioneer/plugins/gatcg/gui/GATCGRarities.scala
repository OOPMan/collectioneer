package com.oopman.collectioneer.plugins.gatcg.gui

enum GATCGRarities(val label: String, val shortLabel: String):
  case Common extends             GATCGRarities("Common", "C")
  case Uncommon extends           GATCGRarities("Uncommon", "U")
  case Rare extends               GATCGRarities("Rare", "R")
  case SuperRare extends          GATCGRarities("Super Rare", "SR")
  case UltraRare extends          GATCGRarities("Ultra Rare", "UR")
  case PromotionalRare extends    GATCGRarities("Promotional Rare", "PR")
  case CollectorSuperRare extends GATCGRarities("Collector Super Rare", "CSR")
  case CollectorUltraRare extends GATCGRarities("Collector Ultra Rare", "CUR")
  case CollectorPromoRare extends GATCGRarities("Collector Promotional Rare", "CPR")
  
object GATCGRarities:  
  def fromRarity(rarity: Short): GATCGRarities =
    fromOrdinal(rarity - 1)
  