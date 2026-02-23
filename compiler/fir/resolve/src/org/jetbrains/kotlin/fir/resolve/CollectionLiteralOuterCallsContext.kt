/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.resolve

import org.jetbrains.kotlin.fir.resolve.calls.candidate.Candidate
import org.jetbrains.kotlin.fir.resolve.calls.candidate.CheckerSink

class CollectionLiteralOuterCallsContext(
    /**
     * [Candidate] whose constraint system must be expanded by the CL's system.
     * CL might be arbitrarily deep relatively to it.
     */
    val containingCandidate: Candidate,
    /**
     * [CheckerSink] of outermost candidate: for nested CL in `foo([[]])` it will be a checker sink of candidate for `foo`.
     * Only non-`null` when CL is expanded as part of the overload resolution of some outer call.
     */
    val checkerSink: CheckerSink? = null,
)
