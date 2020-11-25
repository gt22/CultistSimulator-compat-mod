package frgm.compat.dependecies

import dawnbreaker.data.raw.Recipe
import dawnbreaker.dsl.ModBuilder
import dawnbreaker.vanilla
import frgm.compat.fancyname


fun ModBuilder.dependencies() {
    source("dependencies/elements") {
        elements {
            element("dependencytracker") {
                label = "Dependency tracker"
                description = "Card used by compatibility mod to track dependencies"
                icon = "ascensionenlightenmentb"
                aspects {
                    "mod.frgm.compat" of 1
                }
            }
        }
    }
    source("dependencies/aspects") {
        aspects {
            aspect("mod.frgm.compat") {
                label = fancyname
                description = "$fancyname is loaded"
                icon = "defiancemarks"
            }
        }
    }
    source("dependencies/recipes") {
        recipes {
            sequenceOf("needs", "turn.begin").forEach { name ->
                recipe(name) {
                    linked {
                        prepend {
                            +Recipe("lockdeps.${name.substringBefore('.')}")
                        }
                    }
                }
            }
            sequenceOf("needs", "turn").forEach { name ->
                val res = when(name) {
                    "needs" -> "discardseason"
                    "turn" -> "turn.next"
                    else -> throw IllegalArgumentException()
                }
                recipe("lockdeps.$name") {
                    label = "Locking the dependencies"
                    actionid = "time"
                    startdescription =
                        "[$fancyname] This is a technicality, to prevent dep tracker from cluttering the table"
                    description =
                        "[$fancyname] Dep tracker was NOT FOUND. This may indicate a problem with your mod setup"
                    warmup = 5
                    requirements {
                        "dependencytracker" of "-1"
                    }
                    slot {
                        id = "deps"
                        label = "Tracker"
                        required {
                            "dependencytracker" of 1
                        }
                        greedy = true
                    }
                    linked {
                        +Recipe("lockdeps.failure.$name")
                    }
                    alt {
                        +Recipe("lockdeps.success.$name")
                    }
                }
                recipe("lockdeps.success.$name") {
                    label = "Successfully locked the dependencies"
                    actionid = "time"
                    startdescription =
                        "[$fancyname] This is a technicality, to prevent dep tracker from cluttering the table"
                    description = "[$fancyname] Dep tracker locked successfully"
                    requirements {
                        "dependencytracker" of "1"
                    }
                    linked {
                        +Recipe(res)
                    }
                }
                recipe("lockdeps.failure.$name") {
                    label = "Unable to lock the dependencies"
                    actionid = "time"
                    warmup = 30
                    signalEndingFlavour = "Melancholy"
                    startdescription =
                        "[$fancyname] Unable to find dependency tracker card. It will be created now, but this may indicate a problem with your setup"
                    effects {
                        "dependencytracker" of "1"
                    }
                    linked {
                        +Recipe(res)
                    }
                }
            }
        }
    }
    source("dependencies/legacies") {
        legacies {
            vanilla.legacies.forEach {
                legacy(it.id) {
                    effects {
                        add {
                            "dependencytracker" of 1
                        }
                    }
                }
            }
        }
    }
}