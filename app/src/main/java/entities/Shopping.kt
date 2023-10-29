package entities

data class Shopping(
    var urlImage: String? = null,
    var name: String? = null,
    var total: Double? = 0.0,
    var itemCount: Int? = 0,
){
  constructor() : this (null, null, 0.0, 0)
}
