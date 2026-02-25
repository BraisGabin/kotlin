/*
 * Copyright 2010-2025 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.diagnostics

import org.gradle.api.Project
import java.io.Serializable

/**
 * Immutable diagnostics metadata extracted from a Gradle [Project].
 *
 * The context can be safely passed through task/build-service parameters without keeping
 * a direct reference to [Project].
 */
internal data class ToolingDiagnosticsContext(
    /** Gradle path of the project that reports diagnostics (for example, `:app`). */
    val projectPath: String,
    /** Human-readable project name used in diagnostic messages. */
    val projectName: String,
    /** Rendering settings resolved for this project. */
    val renderingOptions: ToolingDiagnosticRenderingOptions,
) : Serializable {
    companion object {
        /**
         * Builds a [ToolingDiagnosticsContext] from [project].
         */
        fun fromProject(project: Project): ToolingDiagnosticsContext {
            return ToolingDiagnosticsContext(
                projectPath = project.path,
                projectName = project.name,
                renderingOptions = ToolingDiagnosticRenderingOptions.forProject(project),
            )
        }
    }
}

/**
 * Returns diagnostics context derived from this [Project].
 */
internal val Project.toolingDiagnosticsContext: ToolingDiagnosticsContext
    get() = ToolingDiagnosticsContext.fromProject(this)
