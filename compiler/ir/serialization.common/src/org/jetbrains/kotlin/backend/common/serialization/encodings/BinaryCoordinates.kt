/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common.serialization.encodings

import org.jetbrains.kotlin.KtOffsetsOnlySourceElement

object BinaryCoordinatesEncoding {
    fun encode(startOffset: Int, endOffset: Int): Long {
        assert(startOffset <= endOffset)
        return BinaryLattice.encode(startOffset, endOffset - startOffset)
    }

    fun decode(code: Long): KtOffsetsOnlySourceElement {
        val decoded = BinaryLattice.decode(code)
        val start = decoded.first
        return KtOffsetsOnlySourceElement(start, start + decoded.second)
    }
}