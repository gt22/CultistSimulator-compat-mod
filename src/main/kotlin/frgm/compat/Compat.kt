package frgm.compat

import dawnbreaker.dsl.mod
import dawnbreaker.loadVanilla
import frgm.compat.decks.externalizeDecks
import frgm.compat.dependecies.dependencies
import frgm.compat.followers.GenericFollower
import frgm.compat.followers.follower
import frgm.compat.subversion.subversion
import frgm.compat.verbs.aspectAllowedFor
import frgm.compat.verbs.aspectForbiddenFor
import frgm.compat.verbs.verbControl
import java.nio.file.Paths

val fancyname = "Frgm's Compatibility Mod"

fun main() {
    loadVanilla(Paths.get("content_norm"))
    val cmp = compat()
    cmp.saveTo(Paths.get("compat.generated"))
    cmp.saveTo(Paths.get("/Users/gt22/Library/Application Support/Weather Factory/Cultist Simulator/mods/compat"))
}

fun compat() = mod {

    synopsis {
        name = "frgm.compat"
        author = "Frgm"
        version = "0.1.0"
        description = "Mod compatibility tool"
    }
    images = Paths.get("compat.images")
    additionalfiles = Paths.get("compat.additional")
    subversion()
    dependencies()
    externalizeDecks()
    verbControl()
    val frgm = GenericFollower(
        "frgm",
        "Frgm",
        "a Seer",
        "lantern",
        true,
        "enlightenment",
        "test_acq",
        "test_bel",
        "test_dis",
        "test_exl",
        "test_prs",
        "test_rvl",
    )
    source("followers_aspects") {
        aspects {
            follower(frgm)
        }
    }
    source("followers_elements") {
        elements {
            follower(frgm)
            element(frgm.exalt) {
                label = "True Seer"
                aspects {
                    aspectAllowedFor("work") of 1
                    aspectForbiddenFor("talk") of 1
                }
            }
        }
    }
}