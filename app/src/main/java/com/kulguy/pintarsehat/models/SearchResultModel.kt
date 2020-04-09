package com.kulguy.pintarsehat.models

class SearchResultModel(var title: String, var category: String, var defaultPortion: String, var summary: MutableMap<String, String>, var portions: Int = 0, var clicked: Int = 0) {}