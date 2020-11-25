package frgm.compat.decks

import dawnbreaker.dsl.ModBuilder
import dawnbreaker.vanilla

fun getexternalizedName(recipeId: String) = "internal.deck.${recipeId}"

fun ModBuilder.externalizeDecks() {
    val relevantRecipes = vanilla.sources
        .flatMap { it.value.recipes }
        .filter { it.internaldeck != null }
    val elements = mutableSetOf<String>()
    source("decks/externalized/decks") {
        decks {
            relevantRecipes.forEach {
                add(it.internaldeck!!.copy(id = getexternalizedName(it.id)))
                it.internaldeck!!.spec.forEach {  el ->
                    elements.add(el)
                }
            }
        }
    }
    source("decks/externalized/recipes") {
        recipes {
            relevantRecipes.forEach {
                require(it.deckeffects.isEmpty()) { "Encountered a recipe with both internal and external decks, unhandled case." }
                recipe(it.id) {
                    internaldeck {
                        draws = -1
                        comments = "UNUSED, use ${getexternalizedName(it.id)}"
                    }
                    deckeffects {
                        getexternalizedName(it.id) of it.internaldeck!!.draws
                    }
                }
            }
        }
    }
    source("decks/externalized/elements") {
        elements {
            elements.forEach {
                element(it) {}
            }
        }
    }
}
