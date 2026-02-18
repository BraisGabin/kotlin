/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.apple

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

const val SYNTHETIC_IMPORT_TARGET_MAGIC_NAME = "_internal_linkage_SwiftPMImport"

fun createLocalSwiftPackage(
    localPackageDir: Path,
    packageName: String = "LocalSwiftPackage",
    productName: String = packageName,
    targetName: String = productName,
) {
    localPackageDir.resolve("Sources/$targetName").createDirectories()
    localPackageDir.resolve("Package.swift").writeText(
        """
                // swift-tools-version: 5.9
                import PackageDescription

                let package = Package(
                    name: "$packageName",
                    platforms: [.iOS(.v15)],
                    products: [
                        .library(name: "$productName", targets: ["$targetName"]),
                    ],
                    targets: [
                        .target(name: "$targetName"),
                    ]
                )
            """.trimIndent()
    )

    localPackageDir.resolve("Sources/$targetName/$targetName.swift").writeText(
        """
                import Foundation

                @objc public class LocalHelper: NSObject {
                    @objc public static func greeting() -> String {
                        return "Hello from $packageName"
                    }
                }
            """.trimIndent()
    )
}
