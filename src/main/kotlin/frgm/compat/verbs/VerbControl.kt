package frgm.compat.verbs

import dawnbreaker.dsl.ModBuilder
import dawnbreaker.vanilla


fun aspectAllowedFor(verbId: String) = "modded_${verbId}_allowed"
fun aspectForbiddenFor(verbId: String) = "modded_${verbId}_forbidden"

fun ModBuilder.verbControl() {
    val verbs = vanilla.sources.flatMap { it.value.verbs }
    source("verbs/control/aspects") {
        aspects {
            verbs.forEach {
                aspect(aspectAllowedFor(it.id)) {
                    description = "Something I can ${it.id}"
                    comments = "This aspect allows the element to be placed in '${it.label}' verb as the primary subject"
                    isHidden = true
                    noartneeded = true
                }
                aspect(aspectForbiddenFor(it.id)) {
                    description = "Something I can't ${it.id}"
                    comments = "This aspect prevents the element from being placed in '${it.label}' verb as the primary subject"
                    isHidden = true
                    noartneeded = true
                }
            }
        }
    }
    source("verbs/control/verbs") {
        verbs {
            verbs.forEach {
                verb(it.id) {
                    t.slot = it.slot!!.copy().apply {
                        required[aspectAllowedFor(it.id)] = 1
                        forbidden[aspectForbiddenFor(it.id)] = 1
                    }
                }
            }
        }
    }
}