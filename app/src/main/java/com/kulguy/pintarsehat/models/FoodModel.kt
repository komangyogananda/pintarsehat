package com.kulguy.pintarsehat.models

class FoodModel (val title: String, val category: String, val default_portion: String, val portions: MutableMap<String, PortionModel>){
}