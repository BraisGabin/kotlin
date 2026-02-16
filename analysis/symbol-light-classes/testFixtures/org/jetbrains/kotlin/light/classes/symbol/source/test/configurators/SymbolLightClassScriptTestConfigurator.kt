/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.light.classes.symbol.base

import org.jetbrains.kotlin.analysis.low.level.api.fir.test.configurators.AnalysisApiFirScriptTestConfigurator
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms

object SymbolLightClassScriptTestConfigurator :
    AnalysisApiFirScriptTestConfigurator(analyseInDependentSession = false, defaultTargetPlatform = JvmPlatforms.defaultJvmPlatform)