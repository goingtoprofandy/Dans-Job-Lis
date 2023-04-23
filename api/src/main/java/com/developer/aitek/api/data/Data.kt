package com.developer.aitek.api.data

data class DataDetailPokemon(
    var id: String?,
    var device_id: String?,
    var pokemon_id: String?,
    var name: String?,
    var content: DataContentPokemon,
    var febs: MutableList<Feb>
)

data class Feb(
    var id: String,
    var id_my_pokemon: String,
    var fib: String
)

data class DataContentPokemon(
    var base_experience: Int,
    var height: Int,
    var id: Int,
    var weight: Int,
    var name: String,
    var moves: MutableList<Moves>,
    var types: MutableList<Types>,
    var sprites: Sprites
)

data class Sprites(
    var back_default: String
)

data class Types(
    var slot: Int,
    var type: Type
)

data class Type(
    var name: String,
    var url: String
)

data class Moves(
    var move: Move
)

data class Move(
    var name: String,
    var url: String
)