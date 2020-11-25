package frgm.compat.subversion

import dawnbreaker.dsl.ModBuilder


fun ModBuilder.subversion() {
    source("subversion/aspects") {
        subvertAspects()
    }
    source("subversion/elements") {
        applySubvertAspects()
    }
}