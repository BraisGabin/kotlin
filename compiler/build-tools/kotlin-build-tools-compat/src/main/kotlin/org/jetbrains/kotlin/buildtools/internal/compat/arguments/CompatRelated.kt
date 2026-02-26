/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.buildtools.internal.compat.arguments

import org.jetbrains.kotlin.buildtools.api.arguments.JvmCompilerArguments
import java.nio.file.Path
import kotlin.io.path.Path

@Suppress("UNCHECKED_CAST")
internal fun <V, T> getAndMap(value: T, key: JvmCompilerArguments.JvmCompilerArgument<V>): V? =
    when (key) {
        JvmCompilerArguments.JDK_HOME -> {
            val pathValue = value as Path?
            return pathValue?.absolutePathStringOrThrow() as V
        }

        else -> value as V?
    }

@Suppress("UNCHECKED_CAST")
internal fun <V, T> setAndMap(value: T, key: JvmCompilerArguments.JvmCompilerArgument<V>): T? =
    when (key) {
        JvmCompilerArguments.JDK_HOME -> {
            val stringValue = value as String?
            return stringValue?.let { Path(it) } as T
        }

        else -> value as T?
    }