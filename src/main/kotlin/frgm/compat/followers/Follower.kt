package frgm.compat.followers

import dawnbreaker.data.raw.XTrigger
import dawnbreaker.dsl.ElementBuilder
import dawnbreaker.dsl.SourceBuilder

data class GenericFollower(
    var id: String = "",
    var name: String = "",
    var exalt_title: String = "",
    var aspect: String = "",
    var isMortal: Boolean = true,
    var desire: String = "",
    var desc_a: String = "",
    var desc_b: String = "",
    var desc_c: String = "",
    var desc_d: String = "",
    var desc_p: String = "",
    var desc_r: String = ""
) {
    val acquaintance = "${id}_a"
    val believer = "${id}_b"
    val disciple = "${id}_c"
    val exalt = "${id}_d"

    val prisoner = "${id}_p"
    val troublemaker= "${id}_r"
    val rival = "${id}_r_c"
    val longinthemaking = "${id}_r_d"

    val uniquenessgroup = "uq${id}"
}

fun SourceBuilder.AspectsBuilder.follower(fl: GenericFollower) {
    aspect(fl.uniquenessgroup) {
        label = fl.name
        isHidden = true
    }
}

fun SourceBuilder.ElementsBuilder.follower(fl: GenericFollower) {
    val lust = "follower_lust${fl.desire}"
    val uq = "uq${fl.id}"
    element(fl.acquaintance) {
        label = "${fl.name}, an acquaintance"
        description = fl.desc_a
        aspects {
            "acquaintance" of 1
            lust of 1
        }
        xtriggers {
            "recruiting" of XTrigger.transform("${fl.id}_b")
            "rebellion" of XTrigger.transform("${fl.id}_r")
        }
        mortal(fl.isMortal)
        uniquenessgroup = uq
    }
    element(fl.believer) {
        label = "${fl.name}, a believer"
        description = fl.desc_b
        aspects {
            "follower" of 1
            fl.aspect of 2
            lust of 1
        }
        xtriggers {
            "promotiontodisciple" of XTrigger.transform("${fl.id}_c")
            "capturefollower" of XTrigger.transform("${fl.id}_p")
            "rebellion" of XTrigger.transform("${fl.id}_r")
        }
        mortal(fl.isMortal)
        uniquenessgroup = uq
    }
    element(fl.disciple) {
        label = "${fl.name}, a disciple"
        description = fl.desc_c
        aspects {
            "follower" of 1
            "disciple" of 1
            "potential" of 1
            fl.aspect of 5
            lust of 1
        }
        xtriggers {
            "promotionto_d_${fl.aspect}" of XTrigger.transform("${fl.id}_d")
            "capturefollower" of XTrigger.transform("${fl.id}_p")
            "rebellion" of XTrigger.transform("${fl.id}_r_c")
        }
        mortal(fl.isMortal)
        uniquenessgroup = uq
    }
    element(fl.exalt) {
        label = "${fl.name}, ${fl.exalt_title}"
        description = fl.desc_d
        aspects {
            "follower" of 1
            "disciple" of 1
            "exalted" of 1
            fl.aspect of 10
            lust of 1
        }
        xtriggers {
            "capturefollower" of XTrigger.transform("${fl.id}_p")
            "rebellion" of XTrigger.transform("${fl.id}_r_d")
        }
        mortal(fl.isMortal)
        uniquenessgroup = uq
    }
    element(fl.prisoner) {
        label = "${fl.name}, a prisoner"
        description = fl.desc_p
        aspects {
            "prisoner" of 1
            "grail" of 3
            "heart" of 3
            "lantern" of 3
            fl.aspect of 5
            lust of 1
        }
        xtriggers {
            "capturefollower" of XTrigger.transform("${fl.id}_p")
            "rebellion" of XTrigger.transform("${fl.id}_r_d")
        }
        mortal(fl.isMortal)
        uniquenessgroup = uq
    }
    element(fl.troublemaker) {
        label = "${fl.name}, a troublemaker"
        description = fl.desc_r
        aspects {
            "independent" of 1
            "rivalmarks" of 2
            "suspicious" of 1
        }
        xtriggers {
            "rival_promotion" of XTrigger.transform("${fl.id}_r_c")
            "recruiting" of XTrigger.transform("${fl.id}_b")
        }
        mortal(fl.isMortal)
        uniquenessgroup = uq
    }
    element(fl.rival) {
        label = "${fl.name}, a rival"
        description = fl.desc_r
        aspects {
            "independent" of 1
            "rivalmarks" of 3
            "suspicious" of 1
            "disciple" of 1
        }
        xtriggers {
            "rival_promotion" of XTrigger.transform("${fl.id}_r_d")
            "recruiting" of XTrigger.transform("${fl.id}_c")
        }
        mortal(fl.isMortal)
        uniquenessgroup = uq
    }
    element(fl.longinthemaking) {
        label = "${fl.name}, a Long in the making"
        description = fl.desc_r
        aspects {
            "independent" of 1
            "rivalmarks" of 3
            "suspicious" of 1
            "disciple" of 1
            "exalted" of 1
        }
        xtriggers {
            "recruiting" of XTrigger.transform("${fl.id}_c")
        }
        mortal(fl.isMortal)
        uniquenessgroup = uq
    }
}

fun SourceBuilder.follower(fl: GenericFollower) {
    aspects {
        follower(fl)
    }
    elements {
        follower(fl)
    }
}


fun ElementBuilder.mortal(isMortal: Boolean) {
    if(isMortal) {
        aspects {
            "mortal" of 1
        }
        xtriggers {
            "killmortal" of XTrigger.transform("corpse")
            "derangemortal" of XTrigger.transform("lunatic")
        }
    }
}