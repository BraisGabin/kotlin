/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.buildtools.internal.arguments

import org.jetbrains.kotlin.buildtools.api.arguments.ExperimentalCompilerArgument
import org.jetbrains.kotlin.buildtools.api.arguments.JvmCompilerArguments
import org.jetbrains.kotlin.buildtools.api.arguments.types.ProfileCompilerCommand
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

@OptIn(ExperimentalCompilerArgument::class)
@Suppress("UNCHECKED_CAST")
internal fun <V, T> getAndMap(value: T, key: JvmCompilerArguments.JvmCompilerArgument<V>): V? {
    if (value == null) return null as V?
    return when (key) {
        JvmCompilerArguments.JDK_HOME -> {
            val pathValue = value as Path
            pathValue.absolutePathStringOrThrow() as V
        }

        JvmCompilerArguments.X_PROFILE -> {
            val profileCompilerCommand = value as ProfileCompilerCommand
            with(profileCompilerCommand) {
                "${profilerPath.toFile().absolutePath}" +
                        "${File.pathSeparator}${command}" +
                        "${File.pathSeparator}" +
                        "${outputDir.toFile().absolutePath}"
            } as V
        }

        else -> value as V
    }
}

@OptIn(ExperimentalCompilerArgument::class)
@Suppress("UNCHECKED_CAST")
internal fun <V, T> setAndMap(value: T, key: JvmCompilerArguments.JvmCompilerArgument<V>): T? {
    if (value == null) return null as T?

    return when (key) {
        JvmCompilerArguments.JDK_HOME -> {
            val stringValue = value as String
            Path(stringValue) as T
        }

        JvmCompilerArguments.X_PROFILE -> {
            val stringValue = value as String
            val parts = stringValue.split(File.pathSeparator)
            require(parts.size == 3) { "Invalid async profiler settings format: $stringValue" }

            ProfileCompilerCommand(Path(parts[0]), parts[1], Path(parts[2])) as T
        }

        else -> value as T
    }
}