package frgm.compat.subversion

import dawnbreaker.*
import dawnbreaker.data.raw.Element
import dawnbreaker.dsl.ElementBuilder
import dawnbreaker.dsl.SourceBuilder

val minorinfluences = listOf("dread", "fascination", "fleeting", "contentment")
val skillupgrades = listOf("vitality", "erudition", "glimmering");

fun SourceBuilder.subvertAspects() = aspects {
    principles_sh.forEach { pid ->
        val p = vanilla.lookup<Element>(pid)!!
        sequenceOf(pid.lores(), pid.influences()).flatten()
            .mapNotNull { vanilla.lookup<Element>(it) }
            .forEach { l ->
                aspect("upgrades.${l.id}") {
                    label = "Upgrades '${l.label}'"
                    icon = "project_upgrade_$pid"
                    description = "This can be used to upgrade or subvert '${l.label}'"
                }
            }
        aspect("knowledge.$pid") {
            label = "Knowledge of ${p.label} influences"
            icon = "project_upgrade_$pid"
            description = "This is a knowledge, that be used to upgrade or subvert ${p.label} influences"
        }
    }
    sequenceOf(minorinfluences, skillupgrades).flatten().mapNotNull { vanilla.lookup<Element>(it) }
        .forEach { l ->
            aspect("upgrades.${l.id}") {
                label = "Upgrades '${l.label}'"
                icon = l.id
                description = "This can be used to upgrade or subvert '${l.label}'"
            }
        }
    aspect("upgrades.dread") {
        icon = "grim"
    }
    aspect("upgrades.fascination") {
        icon = "idealist"
    }
    aspect("upgrades.contentment") {
        icon = "comfort"
    }
    aspect("upgrades.fleeting") {
        icon = "meticulous"
    }
    aspect("upgrades.vitality") {
        icon = "healthskill"
    }
    aspect("upgrades.erudition") {
        icon = "reasonskill"
    }
    aspect("upgrades.glimmering") {
        icon = "passionskill"
    }
}

fun SourceBuilder.applySubvertAspects() = elements {
    principles_sh.forEach { pid ->
        val sp = subvertedBy(pid)
        sequenceOf(pid.lores(), pid.influences()).flatten()
            .mapNotNull { vanilla.lookup<Element>(it) }
            .forEach { l ->
                val d = Desc(l.id)
                element(l.id) {
                    aspects {
                        add {
                            if (d.tier != 7) {
                                "upgrades.$d" of 1
                                if (pid != sp) {
                                    "upgrades.${d.copy(principle = "knock")}" of 1
                                    "upgrades.${d.copy(principle = sp)}" of 1
                                }
                            }
                            if (d.type == "fragment" && d.tier >= 3) {
                                "knowledge.$pid" of 1
                            }
                        }
                    }
                    if (d.type == "influence" && d.tier == 7) {
                        val b = vanilla.lookup<Element>(d.copy(tier = 1).toString())!!
                        modifyUpgradeSlot(base = b)
                        modifyKnowledgeSlot(base = b)
                    } else {
                        if (l.slots.isNotEmpty()) {
                            modifyUpgradeSlot()
                        }
                        if (l.slots.size > 1) {
                            modifyKnowledgeSlot()
                        }
                    }
                }
            }
    }
    upgrades("erudition", "influencelantern")
    upgrades("eruditionplus", "influencelantern")
    bigrades("fleeting", "influencelantern")
    bigrades("contentment", "influencelantern")
    bigrades("fascination", "influencelantern")

    bigrades("dread", "influenceedge")

    upgrades("vitality", "influencewinter")
    upgrades("vitalityplus", "influencewinter")

    upgrades("vitality", "influenceheart")
    upgrades("vitalityplus", "influenceheart")
    bigrades("contentment", "influenceheart")

    upgrades("glimmering", "influencegrail")
    upgrades("glimmeringplus", "influencegrail")

    upgrades("erudition", "influencemoth")
    upgrades("eruditionplus", "influencemoth")
    upgrades("glimmering", "influencemoth")
    upgrades("glimmeringplus", "influencemoth")
    bigrades("fleeting", "influencemoth")
    bigrades("fascination", "influencemoth")

    upgrades("erudition", "influenceknock")
    upgrades("eruditionplus", "influenceknock")
    upgrades("glimmering", "influenceknock")
    upgrades("glimmeringplus", "influenceknock")
    upgrades("vitality", "influenceknock")
    upgrades("vitalityplus", "influenceknock")

    upgrades("vitality", "vitality")
    upgrades("influenceheart", "vitality")

    upgrades("erudition", "erudition")
    upgrades("influencelantern", "erudition")

    upgrades("glimmering", "glimmering")
    upgrades("influencemoth", "glimmering")

    minorinfluences.forEach { lid ->
        element(lid) {
            modifyUpgradeSlot()
            val p = when (lid) {
                "dread" -> "edge"
                "fascination", "fleeting" -> "moth"
                "contentment" -> "heart"
                else -> throw IllegalArgumentException(lid);
            }
            modifyKnowledgeSlot(p)
        }
    }

    skillupgrades.forEach { eid ->
        val base = vanilla.lookup<Element>(eid)!!
        element(eid) {
            slots {
                val w = base.slots[0]
                val req = mutableMapOf(
                    "upgrades.$eid" to 1,
                )
                +w.copy(required = req)
            }
        }
    }

}


fun ElementBuilder.modifyUpgradeSlot(id: String = this.id, base: Element = vanilla.lookup(id)!!) = slots {
    base.slots.filter { it.actionid != "study" }.forEach { +it.copy(); }
    val w = base.slots.filter { it.actionid == "study" }[0]
    val req = mutableMapOf(
        "upgrades.$id" to 1,
    )
    if ("reason" in w.required) {
        req["reason"] = w.required["reason"]!!
    }
    +w.copy(required = req)
}

fun ElementBuilder.modifyKnowledgeSlot(p: String = Desc(id).principle, base: Element = vanilla.lookup(id)!!) = slots {
    val w = base.slots.filter { it.actionid == "study" }[1]
    val req = mutableMapOf(
        "knowledge.$p" to 1
    )
    +w.copy(required = req)
}

fun SourceBuilder.ElementsBuilder.bigrades(a: String, b: String) {
    upgrades(a, b);
    upgrades(b, a);
}

fun SourceBuilder.ElementsBuilder.upgrades(a: String, b: String) = element(a) {
    aspects {
        add {
            "upgrades.$b" of 1
        }
    }
}