package com.example.dictionary.network.definition.response

import com.example.dictionary.network.definition.model.DefinitionDto

data class DefinitionsResponse(

    var definitions: List<DefinitionDto>
)