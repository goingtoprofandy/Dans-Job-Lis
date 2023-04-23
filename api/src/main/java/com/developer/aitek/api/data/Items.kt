package com.developer.aitek.api.data

data class ItemJob(
    var title: String,
    var type: String,
    var created_at: String,
    var company: String,
    var company_url: String,
    var location: String,
    var description: String,
    var how_to_apply: String,
    var company_logo: String,
    var url: String,
    var id: String
)

data class ItemMyPokemon(
    var name: String,
    var id: String,
    var pokemon_id: String,
    var device_id: String
)
